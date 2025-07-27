package com.ivan.pronin.job.hunt.system.philosophers;

// --- Implementation 2: Using Semaphore to Limit Concurrency ---

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class PhilosopherSemaphore implements Runnable {

    private final int id;

    private final Lock leftFork;

    private final Lock rightFork;

    private final Semaphore limit;

    public PhilosopherSemaphore(int id, Lock leftFork, Lock rightFork, Semaphore limit) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.limit = limit;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 3; i++) {
                think();
                limit.acquire();
                leftFork.lock();
                rightFork.lock();

                try {
                    System.out.println("Philosopher " + id + " is eating.");
                    Thread.sleep((int) (Math.random() * 100));
                } finally {
                    rightFork.unlock();
                    leftFork.unlock();
                    limit.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking.");
        Thread.sleep((int) (Math.random() * 100));
    }

}
