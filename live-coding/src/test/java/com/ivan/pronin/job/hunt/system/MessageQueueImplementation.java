package com.ivan.pronin.job.hunt.system;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class MessageQueueImplementation {

    /*
     * 🧠 Цель задачи
     * Реализовать очередь сообщений с возможностью:
     * enqueue(message) — добавить сообщение
     * dequeue() — получить следующее сообщение (в порядке FIFO)
     * опционально: peek(), size(), isEmpty()
     * ✅ Решение 1: Простая потокобезопасная очередь
     * 📌 Идея
     * Используем BlockingQueue из java.util.concurrent, например LinkedBlockingQueue.
     * Гарантирует:
     * FIFO порядок
     * Потокобезопасность
     * Блокирующий или неблокирующий доступ
     */

    private static class BlockingQueueSolution {

        private final BlockingQueue<String> queue;

        public BlockingQueueSolution(int capacity) {
            this.queue = new LinkedBlockingQueue<>(capacity);
        }

        public void enqueue(String message) throws InterruptedException {
            queue.put(message); // блокирует, если очередь полна
        }

        public String dequeue() throws InterruptedException {
            return queue.take(); // блокирует, если очередь пуста
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }

        public int size() {
            return queue.size();
        }

    }

    private static class LockSolution {

        private final Deque<String> queue = new ArrayDeque<>();

        private final int capacity;

        private final Lock lock = new ReentrantLock();

        private final Condition notEmpty = lock.newCondition();

        private final Condition notFull = lock.newCondition();

        public LockSolution(int capacity) {
            this.capacity = capacity;
        }

        public void enqueue(String message) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " ENQ: " + message);
            lock.lock();
            try {
                while (queue.size() == capacity) {
                    notFull.await();
                    System.out.println("Thread: " + Thread.currentThread().getName() + " waiting for space");
                }
                queue.addLast(message);
                notEmpty.signal();
                System.out.println("Thread: " + Thread.currentThread().getName() + " new MSG signal");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }

        public String dequeue() {
            System.out.println("Thread: " + Thread.currentThread().getName() + " DEQ");
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    notEmpty.await();
                    System.out.println("Thread: " + Thread.currentThread().getName() + " waiting for MSG");
                }
                String msg = queue.removeFirst();
                notFull.signal();
                System.out.println("Thread: " + Thread.currentThread().getName() + " MSG removed signal");
                return msg;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }

        public boolean isEmpty() {
            lock.lock();
            try {
                return queue.isEmpty();
            } finally {
                lock.unlock();
            }
        }

        public int size() {
            lock.lock();
            try {
                return queue.size();
            } finally {
                lock.unlock();
            }
        }

    }

    @Test
    public void testCustomQueue() throws InterruptedException {
        LockSolution mq = new LockSolution(2);

        Runnable task1 = () -> mq.enqueue("A");
        Runnable task2 = () -> mq.enqueue("B");
        Runnable task3 = () -> mq.enqueue("C");
        Runnable task4 = () -> mq.enqueue("D");
        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        Thread t3 = new Thread(task3);
        Thread t4 = new Thread(task4);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        assertNotNull(mq.dequeue());
        assertNotNull(mq.dequeue());
        assertNotNull(mq.dequeue());
        assertNotNull(mq.dequeue());
        assertTrue(mq.isEmpty());
    }

    @Test
    public void testCustomQueueExecutors() {
        var mq = new LockSolution(2);

        Runnable task1 = () -> mq.enqueue("A");
        Runnable task2 = () -> mq.enqueue("B");
        Runnable task3 = () -> mq.enqueue("C");
        Runnable task4 = () -> mq.enqueue("D");
        try (var executor = Executors.newFixedThreadPool(2)) {
            executor.submit(task1);
            executor.submit(task2);
            executor.submit(task3);
            executor.submit(task4);
            executor.submit(mq::dequeue);
            executor.submit(mq::dequeue);
            executor.submit(mq::dequeue);
            executor.submit(mq::dequeue);
        }
        assertTrue(mq.isEmpty());
    }

    @Test
    public void testCustomQueueForkJoin() {
        final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        var mq = new LockSolution(2);

        Runnable task1 = () -> mq.enqueue("A");
        Runnable task2 = () -> mq.enqueue("B");
        Runnable task3 = () -> mq.enqueue("C");
        Runnable task4 = () -> mq.enqueue("D");
        forkJoinPool.submit(task1);
        forkJoinPool.submit(task2);
        forkJoinPool.submit(task3);
        forkJoinPool.submit(task4);
        forkJoinPool.submit(mq::dequeue);
        forkJoinPool.submit(mq::dequeue);
        forkJoinPool.submit(mq::dequeue);
        forkJoinPool.submit(mq::dequeue);
        assertTrue(mq.isEmpty());
    }

}
