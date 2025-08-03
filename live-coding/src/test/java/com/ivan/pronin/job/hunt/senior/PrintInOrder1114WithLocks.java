package com.ivan.pronin.job.hunt.senior;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class PrintInOrder1114WithLocks {
    private final Lock lock = new ReentrantLock();
    private final Condition condSecond = lock.newCondition();
    private final Condition condThird = lock.newCondition();
    private int state = 0;

    public void first(Runnable printFirst)  {
        lock.lock();
        try {
            printFirst.run();
            state = 1;
            condSecond.signal();
        } finally {
            lock.unlock();
        }
    }

    public void second(Runnable printSecond) {
        lock.lock();
        try {
            while (state < 1) {
                condSecond.await();
            }
            printSecond.run();
            state = 2;
            condThird.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void third(Runnable printThird)  {
        lock.lock();
        try {
            while (state < 2) {
                condThird.await();
            }
            printThird.run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private StringBuilder output = new StringBuilder();

    private final Runnable printFirst = () -> output.append("first");

    private final Runnable printSecond = () -> output.append("second");

    private final Runnable printThird = () -> output.append("third");

    private void runInRandomOrder(PrintInOrder1114WithLocks foo) throws InterruptedException {
        List<Runnable> tasks = Arrays.asList(
            () -> safe(foo, () -> foo.first(printFirst)),
            () -> safe(foo, () -> foo.second(printSecond)),
            () -> safe(foo, () -> foo.third(printThird))
        );
        Collections.shuffle(tasks);
        ExecutorService exec = Executors.newFixedThreadPool(3);
        for (Runnable task : tasks) exec.submit(task);
        exec.shutdown();
        exec.awaitTermination(2, TimeUnit.SECONDS);
    }

    private void safe(Object obj, Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSemaphoreOrder() throws InterruptedException {
        output.setLength(0);
        runInRandomOrder(new PrintInOrder1114WithLocks());
        assertEquals("firstsecondthird", output.toString());
    }

}
