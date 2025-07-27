package com.ivan.pronin.job.hunt.system.philosophers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophersTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Test 1: Ordered Locking ---");
        testOrderedLocking();

        System.out.println("--- Test 2: Semaphore ---");
        testSemaphore();

        System.out.println("--- Test 3: Lock Striping ---");
        testStriping();

        System.out.println("--- Test 4: Starvation-Free (Fair Locks) ---");
        testFairLocks();
    }

    private static void testOrderedLocking() throws InterruptedException {
        int n = 5;
        ExecutorService executor = Executors.newFixedThreadPool(n);
        Lock[] forks = new ReentrantLock[n];
        for (int i = 0; i < n; i++) forks[i] = new ReentrantLock();

        for (int i = 0; i < n; i++) {
            executor.submit(new Philosopher(i, forks[i], forks[(i + 1) % n]));
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void testSemaphore() throws InterruptedException {
        int n = 5;
        ExecutorService executor = Executors.newFixedThreadPool(n);
        Lock[] forks = new ReentrantLock[n];
        for (int i = 0; i < n; i++) forks[i] = new ReentrantLock();
        Semaphore limit = new Semaphore(n - 1);

        for (int i = 0; i < n; i++) {
            executor.submit(new PhilosopherSemaphore(i, forks[i], forks[(i + 1) % n], limit));
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void testStriping() throws InterruptedException {
        int n = 5;
        ExecutorService executor = Executors.newFixedThreadPool(n);
        Lock[] forks = new ReentrantLock[n];
        for (int i = 0; i < n; i++) forks[i] = new ReentrantLock();

        for (int i = 0; i < n; i++) {
            executor.submit(new PhilosopherStriped(i, forks));
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void testFairLocks() throws InterruptedException {
        int n = 5;
        ExecutorService executor = Executors.newFixedThreadPool(n);
        Lock[] forks = new ReentrantLock[n];
        for (int i = 0; i < n; i++) forks[i] = new ReentrantLock(true); // fair lock

        for (int i = 0; i < n; i++) {
            executor.submit(new PhilosopherFair(i, forks[i], forks[(i + 1) % n]));
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

}

// --- Problem Explanation ---
// The Dining Philosophers Problem is a classic concurrency challenge:
// - N philosophers sit around a table with N forks.
// - To eat, each philosopher needs both the left and right fork.
// - The challenge is to prevent deadlocks (circular wait), starvation (some can't eat),
//   and ensure fairness and efficiency.
// - Lock striping is one way to enforce an acquisition order (e.g., always lock lower index first)
//   to avoid deadlock while sharing fork resources.
// - Using fair locks ensures that waiting threads are granted access in order of request, reducing starvation.