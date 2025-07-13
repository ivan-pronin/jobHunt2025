package com.ivan.pronin.job.hunt.senior;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
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
 * @since 11.07.2025
 */
public class BuildingH2O1117 {

    /*
     * 1117. Building H2O
     * You are given two functions printH and printO that output "H" and "O" respectively.
     * You are given an integer n that represents the number of water molecules to be formed.
     * Your task is to implement a function buildH2O that uses these two functions to print the correct sequence of "H" and "O" to form n water molecules (H2O).
     * * The output should be in the form of "HHOHHOHHO..." for n = 3, meaning that for every water molecule, two "H" should be printed followed by one "O".
     */
    private static class H2OSemaphore {

        private final Semaphore hSemaphore = new Semaphore(2);

        private final Semaphore oSemaphore = new Semaphore(1);

        private final CyclicBarrier barrier = new CyclicBarrier(3);

        public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
            hSemaphore.acquire();
            try {
                barrier.await(); // ждем остальных
                releaseHydrogen.run();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } finally {
                hSemaphore.release();
            }
        }

        public void oxygen(Runnable releaseOxygen) throws InterruptedException {
            oSemaphore.acquire();
            try {
                barrier.await(); // ждем остальных
                releaseOxygen.run();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } finally {
                oSemaphore.release();
            }
        }

    }

    private static class H2OLocks {

        private int hydrogenCount = 0;

        private int oxygenCount = 0;

        private final Lock lock = new ReentrantLock();

        private final Condition condition = lock.newCondition();

        public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
            lock.lock();
            try {
                while (hydrogenCount == 2) {
                    condition.await();
                }
                hydrogenCount++;
                releaseHydrogen.run();
                if (hydrogenCount == 2 && oxygenCount == 1) {
                    hydrogenCount = 0;
                    oxygenCount = 0;
                    condition.signalAll(); // разрешаем следующую молекулу
                }
            } finally {
                lock.unlock();
            }
        }

        public void oxygen(Runnable releaseOxygen) throws InterruptedException {
            lock.lock();
            try {
                while (oxygenCount == 1) {
                    condition.await();
                }
                oxygenCount++;
                releaseOxygen.run();
                if (hydrogenCount == 2 && oxygenCount == 1) {
                    hydrogenCount = 0;
                    oxygenCount = 0;
                    condition.signalAll();
                }
            } finally {
                lock.unlock();
            }
        }

    }

    private StringBuilder output = new StringBuilder();

    private final Runnable releaseH = () -> output.append("H");

    private final Runnable releaseO = () -> output.append("O");

    private void runConcurrentTest(H2OSemaphore h2o, String input) throws InterruptedException {
        output.setLength(0);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (char ch : input.toCharArray()) {
            if (ch == 'H') {
                executor.submit(() -> {
                    try {
                        h2o.hydrogen(releaseH);
                    } catch (InterruptedException ignored) {
                    }
                });
            } else {
                executor.submit(() -> {
                    try {
                        h2o.oxygen(releaseO);
                    } catch (InterruptedException ignored) {
                    }
                });
            }
        }

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
    }

    @Test
    public void testMoleculeFormation() throws InterruptedException {
        H2OSemaphore h2o = new H2OSemaphore(); // или new H2O_Alt();
        runConcurrentTest(h2o, "HOHHOHOOHH");
        assertEquals(0, output.length() % 3); // должна быть кратна 3
        int hCount = 0, oCount = 0;
        for (char c : output.toString().toCharArray()) {
            if (c == 'H') hCount++;
            if (c == 'O') oCount++;
        }
        assertEquals(hCount, oCount * 2); // 2 H на 1 O
    }

}
