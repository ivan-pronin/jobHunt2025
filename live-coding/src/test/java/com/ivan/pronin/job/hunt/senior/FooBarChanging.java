package com.ivan.pronin.job.hunt.senior;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 01.08.2025
 */
public class FooBarChanging {

    private static class FooBar {

        private Semaphore fooSem = new Semaphore(1);

        private Semaphore barSem = new Semaphore(0);

        private int counter;

        public FooBar(int n) {
            counter = n;
        }

        public void foo(Runnable printFoo) {
            for (int i = 0; i < counter; i++) {
                try {
                    fooSem.acquire();
                    printFoo.run();
                    barSem.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void bar(Runnable printBar) {
            for (int i = 0; i < counter; i++) {
                try {
                    barSem.acquire();
                    printBar.run();
                    fooSem.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }

    }

    private static class FooBarLocks {

        private Lock lock = new ReentrantLock();

        private Condition switcher = lock.newCondition();

        private boolean fooTurn = true;

        private int counter;

        public FooBarLocks(int n) {
            counter = n;
        }

        public void foo(Runnable printFoo) {
            for (int i = 0; i < counter; i++) {
                try {
                    lock.lock();
                    while (!fooTurn) switcher.await();
                    printFoo.run();
                    fooTurn = false;
                    switcher.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }

        public void bar(Runnable printBar) {
            for (int i = 0; i < counter; i++) {
                try {
                    lock.lock();
                    while (fooTurn) switcher.await();
                    printBar.run();
                    fooTurn = true;
                    switcher.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }

            }
        }

    }

    @Test
    void testFooBar() throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        Runnable foo = () -> sb.append("foo");
        Runnable bar = () -> sb.append("bar");
        FooBar fooBar = new FooBar(4);
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            executorService.submit(() -> fooBar.foo(foo));
            executorService.submit(() -> fooBar.bar(bar));
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        }
        assertEquals("foobarfoobarfoobarfoobar", sb.toString());
    }

    @Test
    void testFooBarLock() throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        Runnable foo = () -> sb.append("foo");
        Runnable bar = () -> sb.append("bar");
        FooBarLocks fooBar = new FooBarLocks(4);
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            executorService.submit(() -> fooBar.foo(foo));
            executorService.submit(() -> fooBar.bar(bar));
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        }
        assertEquals("foobarfoobarfoobarfoobar", sb.toString());
    }

}
