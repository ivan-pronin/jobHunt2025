package com.ivan.pronin.job.hunt.system;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class ProducerConsumerProblem {

    /// --- Implementation 1: Using BlockingQueue ---
    class ProducerConsumerQueue {

        private final BlockingQueue<Integer> buffer;

        public ProducerConsumerQueue(int capacity) {
            this.buffer = new ArrayBlockingQueue<>(capacity);
        }

        public void produce(int item) throws InterruptedException {
            buffer.put(item);
            System.out.println("Produced: " + item);
        }

        public int consume() throws InterruptedException {
            int item = buffer.take();
            System.out.println("Consumed: " + item);
            return item;
        }

    }

// --- Implementation 2: Using wait() and notify() ---

    class ManualBuffer {

        private final Queue<Integer> queue = new LinkedList<>();

        private final int capacity;

        public ManualBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void produce(int item) throws InterruptedException {
            while (queue.size() == capacity) {
                wait();
            }
            queue.add(item);
            System.out.println("Produced: " + item);
            notifyAll();
        }

        public synchronized int consume() throws InterruptedException {
            while (queue.isEmpty()) {
                wait();
            }
            int item = queue.poll();
            System.out.println("Consumed: " + item);
            notifyAll();
            return item;
        }

    }

    // --- Unit Test ---
    @Test
    public void test() throws InterruptedException {
        System.out.println("--- Test BlockingQueue Version ---");
        ProducerConsumerQueue pcq = new ProducerConsumerQueue(5);
        runTest(pcq::produce, pcq::consume);

        System.out.println("--- Test Manual wait/notify Version ---");
        ManualBuffer mb = new ManualBuffer(5);
        runTest(mb::produce, mb::consume);
    }

    private void runTest(ThrowingConsumer<Integer> producer, ThrowingSupplier<Integer> consumer) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
            final int item = i;
            executor.submit(() -> {
                try {
                    producer.accept(item);
                } catch (InterruptedException ignored) {
                }
            });
        }
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    consumer.get();
                } catch (InterruptedException ignored) {
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    @FunctionalInterface
    interface ThrowingConsumer<T> {

        void accept(T t) throws InterruptedException;

    }

    @FunctionalInterface
    interface ThrowingSupplier<T> {

        T get() throws InterruptedException;

    }

}

