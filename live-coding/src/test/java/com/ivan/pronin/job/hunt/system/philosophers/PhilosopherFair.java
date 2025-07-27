package com.ivan.pronin.job.hunt.system.philosophers;

// --- Implementation 4: Starvation-Free with Fair Locks ---

import java.util.concurrent.locks.Lock;

public class PhilosopherFair implements Runnable {

    private final int id;

    private final Lock leftFork;

    private final Lock rightFork;

    public PhilosopherFair(int id, Lock leftFork, Lock rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 3; i++) {
                think();
                eat();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking.");
        Thread.sleep((int) (Math.random() * 100));
    }

    private void eat() throws InterruptedException {
        Lock first = id % 2 == 0 ? leftFork : rightFork;
        Lock second = id % 2 == 0 ? rightFork : leftFork;

        first.lock();
        second.lock();

        try {
            System.out.println("Philosopher " + id + " is eating.");
            Thread.sleep((int) (Math.random() * 100));
        } finally {
            second.unlock();
            first.unlock();
        }
    }

}
