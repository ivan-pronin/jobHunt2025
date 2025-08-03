package com.ivan.pronin.job.hunt.system.philosophers;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ivan Pronin
 * @since 01.08.2025
 */
public class DininigPhilosophers1226 {
// Решение 1: Ordered Locking (вилки берутся в определенном порядке — deadlock impossible)

    private static class DiningPhilosophersOrdered {

        private final Lock[] forks = new ReentrantLock[5];

        public DiningPhilosophersOrdered() {
            for (int i = 0; i < 5; i++) {
                forks[i] = new ReentrantLock();
            }
        }

        public void wantsToEat(int philosopher,
                               Runnable pickLeftFork,
                               Runnable pickRightFork,
                               Runnable eat,
                               Runnable putLeftFork,
                               Runnable putRightFork) throws InterruptedException {

            int left = philosopher;
            int right = (philosopher + 1) % 5;

            // взять сначала меньшую по индексу вилку (гарантирует порядок)
            Lock first = forks[Math.min(left, right)];
            Lock second = forks[Math.max(left, right)];

            first.lock();
            second.lock();

            pickLeftFork.run();
            pickRightFork.run();
            eat.run();
            putLeftFork.run();
            putRightFork.run();

            second.unlock();
            first.unlock();
        }

    }

// Решение 2: Semaphore (ограничиваем количество философов одновременно за столом)

    private static class DiningPhilosophersSemaphore {

        private final Semaphore[] forks = new Semaphore[5];

        private final Semaphore room = new Semaphore(4); // максимум 4 философа одновременно

        public DiningPhilosophersSemaphore() {
            for (int i = 0; i < 5; i++) {
                forks[i] = new Semaphore(1);
            }
        }

        public void wantsToEat(int philosopher,
                               Runnable pickLeftFork,
                               Runnable pickRightFork,
                               Runnable eat,
                               Runnable putLeftFork,
                               Runnable putRightFork) throws InterruptedException {

            int left = philosopher;
            int right = (philosopher + 1) % 5;

            room.acquire(); // не больше 4 философов одновременно
            forks[left].acquire();
            forks[right].acquire();

            pickLeftFork.run();
            pickRightFork.run();
            eat.run();
            putLeftFork.run();
            putRightFork.run();

            forks[right].release();
            forks[left].release();
            room.release();
        }

    }

}
