package com.ivan.pronin.job.hunt.system;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 16.07.2025
 */
public class ReadWriteLock {

    /*
     * 🔒 Что такое Read-Write Lock?
     * Read-Write Lock (чтение-запись) — это расширенный механизм блокировки, который позволяет нескольким потокам одновременно читать данные, но ограничивает запись до одного потока.
     * * ✅ Решение 1: Простой Read-Write Lock
     * 📌 Идея:
     * Read-Write Lock разделяет операции чтения и записи:
     * - Чтение: несколько потоков могут одновременно читать данные, если нет активной записи.
     * - Запись: только один поток может записывать данные, блокируя чтение.
     */
    private static class SharedResource1 {

        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        private int value = 0;

        public int read() {
            lock.readLock().lock();
            try {
                return value;
            } finally {
                lock.readLock().unlock();
            }
        }

        public void write(int newValue) {
            lock.writeLock().lock();
            try {
                value = newValue;
            } finally {
                lock.writeLock().unlock();
            }
        }

    }

    @Test
    void testReadWrite() throws InterruptedException {
        SharedResource1 resource = new SharedResource1();

        Thread writer = new Thread(() -> resource.write(42));
        writer.start();
        writer.join();

        assertEquals(42, resource.read());
    }

    /*
     * ✅ Решение 2: Синхронизированный Read-Write Lock
     * 📌 Идея:
     * Этот подход использует синхронизацию для управления доступом к ресурсу:
     * - Чтение: несколько потоков могут одновременно читать данные, если нет активной записи.
     * - Запись: только один поток может записывать данные, блокируя чтение.
     * Этот подход реализует собственный механизм блокировки с использованием методов `wait()` и `notifyAll()`.
     */
    private static class SharedResource2 {

        private int readers = 0;

        private boolean isWriting = false;

        private int value = 0;

        public synchronized void beginRead() throws InterruptedException {
            while (isWriting) {
                wait();
            }
            readers++;
        }

        public synchronized void endRead() {
            readers--;
            if (readers == 0) {
                notifyAll();
            }
        }

        public synchronized void beginWrite() throws InterruptedException {
            while (isWriting || readers > 0) {
                wait();
            }
            isWriting = true;
        }

        public synchronized void endWrite() {
            isWriting = false;
            notifyAll();
        }

        public int read() throws InterruptedException {
            beginRead();
            try {
                return value;
            } finally {
                endRead();
            }
        }

        public void write(int newValue) throws InterruptedException {
            beginWrite();
            try {
                value = newValue;
            } finally {
                endWrite();
            }
        }

    }

    @Test
    void testReadWrite2() throws InterruptedException {
        SharedResource2 resource = new SharedResource2();

        Thread writer = new Thread(() -> {
            try {
                resource.write(42);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        writer.start();
        writer.join();

        assertEquals(42, resource.read());
    }

}
