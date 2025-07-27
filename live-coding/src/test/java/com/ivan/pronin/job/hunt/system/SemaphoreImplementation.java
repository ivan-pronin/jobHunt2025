package com.ivan.pronin.job.hunt.system;

import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 16.07.2025
 */
public class SemaphoreImplementation {

    /*
     * 🔒 Что такое Semaphore?
     * Semaphore (семафор) — это механизм синхронизации, который ограничивает количество потоков, которые могут одновременно получить доступ к ресурсу.
     * * ✅ Решение 1: Простой Semaphore
     * 📌 Идея:
     * Semaphore позволяет ограничить количество потоков, которые могут одновременно выполнять определённый код.
     * * Пример: ограничение доступа к ресурсу, например, к базе данных или файлу.
     *
     */
    private static class SharedResource1 {

        private final Semaphore semaphore;

        public SharedResource1(int permits) {
            this.semaphore = new Semaphore(permits);
        }

        public void accessResource() throws InterruptedException {
            semaphore.acquire();
            try {
                // Simulate resource access
                Thread.sleep(100);
            } finally {
                semaphore.release();
            }
        }

        public int availablePermits() {
            return semaphore.availablePermits();
        }

    }

    @Test
    void testSemaphoreAllowsLimitedAccess() throws InterruptedException {
        SharedResource1 resource = new SharedResource1(2);

        Thread t1 = new Thread(() -> {
            try {
                resource.accessResource();
            } catch (InterruptedException ignored) {
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                resource.accessResource();
            } catch (InterruptedException ignored) {
            }
        });

        t1.start();
        t2.start();
        Thread.sleep(50); // give threads time to acquire

        assertEquals(0, resource.availablePermits());
    }

    private static class SimpleSemaphore {

        private int permits;

        public SimpleSemaphore(int permits) {
            this.permits = permits;
        }

        public synchronized void acquire() throws InterruptedException {
            while (permits == 0) {
                wait();
            }
            permits--;
        }

        public synchronized void release() {
            permits++;
            notify();
        }

        public synchronized int availablePermits() {
            return permits;
        }

    }

    @Test
    void testSimpleSemaphore() throws InterruptedException {
        SimpleSemaphore semaphore = new SimpleSemaphore(1);

        Thread t1 = new Thread(() -> {
            try {
                semaphore.acquire();
                Thread.sleep(100);
                semaphore.release();
            } catch (InterruptedException ignored) {
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                semaphore.acquire();
                semaphore.release();
            } catch (InterruptedException ignored) {
            }
        });

        t1.start();
        Thread.sleep(10);
        t2.start();

        t1.join();
        t2.join();

        assertEquals(1, semaphore.availablePermits());
    }

}