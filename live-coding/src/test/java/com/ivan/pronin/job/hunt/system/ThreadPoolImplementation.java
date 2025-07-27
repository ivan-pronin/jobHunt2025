package com.ivan.pronin.job.hunt.system;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 16.07.2025
 */
public class ThreadPoolImplementation {

    /*
     * 🔒 Что такое Thread Pool?
     * Thread Pool (пул потоков) — это коллекция потоков, которые могут быть переиспользованы для выполнения задач.
     * * * ✅ Решение 1: Простой Thread Pool
     * 📌 Идея:
     * Thread Pool позволяет создавать ограниченное количество потоков, которые могут выполнять задачи асинхронно.
     * * * Пример: обработка задач в фоновом режиме, например, загрузка файлов или выполнение запросов к базе данных.
     * * * Преимущества:
     * - Повышает производительность за счёт переиспользования потоков
     * - Снижает накладные расходы на создание и уничтожение потоков
     * * * * Недостатки:
     * - Ограничивает количество одновременно выполняемых задач
     */
    private static class SimpleThreadPool {

        private final ExecutorService executor;

        public SimpleThreadPool(int threadCount) {
            this.executor = Executors.newFixedThreadPool(threadCount);
        }

        public void submitTask(Runnable task) {
            executor.submit(task);
        }

        public void shutdown() {
            executor.shutdown();
        }

    }

    @Test
    void testSubmitTasks() throws InterruptedException {
        var pool = new SimpleThreadPool(3);
        AtomicInteger counter = new AtomicInteger(0);

        for (int i = 0; i < 5; i++) {
            pool.submitTask(() -> counter.incrementAndGet());
        }

        Thread.sleep(500); // wait for tasks
        pool.shutdown();

        assertEquals(5, counter.get());
    }

    /*
     * 🧠 Цель задачи
     * Реализовать простой Thread Pool с возможностью:
     * submit(task) — добавить задачу в очередь
     * fetchTask() — получить следующую задачу для выполнения
     * stop() — остановить пул и завершить все потоки
     * ✅ Решение 2: Простой Thread Pool с ручным управлением
     * 📌 Идея
     * Используем собственную реализацию пула потоков с очередью задач.
     *
     */
    private static class ThreadPool2 {

        private final List<Worker> workers = new LinkedList<>();

        private final Queue<Runnable> taskQueue = new LinkedList<>();

        private boolean isStopped = false;

        public ThreadPool2(int numThreads) {
            for (int i = 0; i < numThreads; i++) {
                Worker worker = new Worker();
                worker.start();
                workers.add(worker);
            }
        }

        public synchronized void submit(Runnable task) {
            if (isStopped) throw new IllegalStateException("ThreadPool is stopped");
            taskQueue.offer(task);
            notify();
        }

        public synchronized Runnable fetchTask() throws InterruptedException {
            while (taskQueue.isEmpty()) {
                wait();
            }
            return taskQueue.poll();
        }

        public synchronized void stop() {
            isStopped = true;
            for (Worker w : workers) {
                w.interrupt();
            }
        }

        private class Worker extends Thread {

            public void run() {
                while (!isInterrupted()) {
                    Runnable task;
                    try {
                        task = fetchTask();
                    } catch (InterruptedException e) {
                        break;
                    }
                    try {
                        task.run();
                    } catch (Exception ignored) {
                    }
                }
            }

        }

    }

    @Test
    void testThreadPoolExecutesTasks() throws InterruptedException {
        ThreadPool2 pool = new ThreadPool2(2);
        AtomicInteger counter = new AtomicInteger(0);

        for (int i = 0; i < 4; i++) {
            pool.submit(counter::incrementAndGet);
        }

        Thread.sleep(500);
        pool.stop();

        assertEquals(4, counter.get());
    }

    private static  class ThreadPoolWithFuture {
        private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
        private final List<Thread> workers = new ArrayList<>();
        private volatile boolean isShutdown = false;

        public ThreadPoolWithFuture(int numThreads) {
            for (int i = 0; i < numThreads; i++) {
                Thread worker = new Thread(this::workerLoop);
                worker.start();
                workers.add(worker);
            }
        }

        public <T> Future<T> submit(Callable<T> task) {
            if (isShutdown) throw new RejectedExecutionException("Thread pool is shut down");
            FutureTask<T> futureTask = new FutureTask<>(task);
            taskQueue.offer(futureTask);
            return futureTask;
        }

        public void shutdown() {
            isShutdown = true;
            // Wake up any waiting workers
            for (Thread worker : workers) {
                worker.interrupt();
            }
        }

        public void awaitTermination() {
            for (Thread worker : workers) {
                try {
                    worker.join();
                } catch (InterruptedException ignored) {}
            }
        }

        private void workerLoop() {
            while (!isShutdown || !taskQueue.isEmpty()) {
                try {
                    Runnable task = taskQueue.poll(500, TimeUnit.MILLISECONDS);
                    if (task != null) task.run();
                } catch (InterruptedException e) {
                    // Allow loop to exit if shutdown and queue is empty
                }
            }
        }
    }

    @Test
    void testFutureSubmission() throws Exception {
        ThreadPoolWithFuture pool = new ThreadPoolWithFuture(2);

        Future<Integer> future = pool.submit(() -> 42);
        Future<String> another = pool.submit(() -> "Hello");

        assertEquals(42, future.get());
        assertEquals("Hello", another.get());

        Thread.sleep(1000);
        // Ensure the pool can handle multiple tasks
        for (int i = 0; i < 5; i++) {
            pool.submit(() -> {
                try {
                    System.out.println("Task running in thread: " + Thread.currentThread().getName() +   " at " + System.currentTimeMillis());
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
                return null;
            });
        }

        pool.shutdown();
        pool.awaitTermination();
    }

    @Test
    void testGracefulShutdown() throws InterruptedException {
        ThreadPoolWithFuture pool = new ThreadPoolWithFuture(2);

        for (int i = 0; i < 5; i++) {
            pool.submit(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
                return null;
            });
        }

        pool.shutdown();
        pool.awaitTermination();

        assertTrue(true); // just ensuring no exception was thrown
    }
}
