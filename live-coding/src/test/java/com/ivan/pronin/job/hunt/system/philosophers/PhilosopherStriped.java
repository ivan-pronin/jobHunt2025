package com.ivan.pronin.job.hunt.system.philosophers;

// --- Implementation 3: Using Lock Striping ---

import java.util.concurrent.locks.Lock;

public class PhilosopherStriped implements Runnable {

    private final int id;

    private final Lock[] forkStripes;

    public PhilosopherStriped(int id, Lock[] forkStripes) {
        this.id = id;
        this.forkStripes = forkStripes;
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
        int left = id;
        int right = (id + 1) % forkStripes.length;
        int first = Math.min(left, right);
        int second = Math.max(left, right);

        forkStripes[first].lock();
        forkStripes[second].lock();

        try {
            System.out.println("Philosopher " + id + " is eating.");
            Thread.sleep((int) (Math.random() * 100));
        } finally {
            forkStripes[second].unlock();
            forkStripes[first].unlock();
        }
    }

}