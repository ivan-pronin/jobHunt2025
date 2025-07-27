package com.ivan.pronin.job.hunt.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VirtualThreadPoolLimitedTest {

    private static class VirtualThreadPoolLimited {

        private final ExecutorService executor;

        private final List<Future<?>> submittedTasks = Collections.synchronizedList(new ArrayList<>());

        private final Semaphore permits;

        private volatile boolean isShutdown = false;

        public VirtualThreadPoolLimited(int maxParallelism) {
            this.executor = Executors.newVirtualThreadPerTaskExecutor();
            this.permits = new Semaphore(maxParallelism);
        }

        public <T> Future<T> submit(Callable<T> task) {
            if (isShutdown) throw new RejectedExecutionException("Pool is shut down");

            Callable<T> wrappedTask = () -> {
                permits.acquire(); // limit parallelism
                try {
                    return task.call();
                } finally {
                    permits.release();
                }
            };

            Future<T> future = executor.submit(wrappedTask);
            submittedTasks.add(future);
            return future;
        }

        public Future<?> submit(Runnable task) throws InterruptedException {
            return submit(Executors.callable(task));
        }

        public void shutdown() {
            isShutdown = true;
            executor.shutdown();
        }

        public void awaitTermination() {
            for (Future<?> f : submittedTasks) {
                try {
                    f.get(); // wait task
                } catch (CancellationException | InterruptedException | ExecutionException ignored) {
                }
            }
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ignored) {
            }
        }

        public void cancelAll() {
            for (Future<?> f : submittedTasks) {
                f.cancel(true); // interrupt if running
            }
        }

        public boolean isShutdown() {
            return isShutdown;
        }

        public boolean isTerminated() {
            return executor.isTerminated();
        }

    }

    @Test
    void testLimitedParallelism() throws InterruptedException {
        VirtualThreadPoolLimited pool = new VirtualThreadPoolLimited(2);
        AtomicInteger running = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            pool.submit(() -> {
                int currentlyRunning = running.incrementAndGet();
                assertTrue(currentlyRunning <= 2); // max 2 in parallel
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                running.decrementAndGet();
                latch.countDown();
            });
        }

        latch.await();
        pool.shutdown();
        pool.awaitTermination();
    }

    @Test
    void testCancelTasks() throws Exception {
        VirtualThreadPoolLimited pool = new VirtualThreadPoolLimited(2);

        Future<String> f1 = pool.submit(() -> {
            Thread.sleep(1000);
            return "OK";
        });

        Future<String> f2 = pool.submit(() -> {
            Thread.sleep(1000);
            return "Still OK";
        });

        pool.cancelAll();

        assertTrue(f1.isCancelled() || f1.isDone());
        assertTrue(f2.isCancelled() || f2.isDone());

        pool.shutdown();
        pool.awaitTermination();
    }

}
