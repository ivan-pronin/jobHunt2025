package com.ivan.pronin.job.hunt.system;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class ConnectionPoolManager {

    // --- Implementation 1: Basic Blocking Queue-based Connection Pool ---
    interface Connection {

        void execute(String query);

    }

    class DummyConnection implements Connection {

        private final int id;

        public DummyConnection(int id) {
            this.id = id;
        }

        public void execute(String query) {
            System.out.println("Conn-" + id + ": " + query);
        }

    }

    class SimpleConnectionPool {

        private final BlockingQueue<Connection> pool;

        public SimpleConnectionPool(int size) {
            pool = new ArrayBlockingQueue<>(size);
            for (int i = 0; i < size; i++) {
                pool.offer(new DummyConnection(i));
            }
        }

        public Connection acquire() throws InterruptedException {
            return pool.take();
        }

        public void release(Connection conn) {
            pool.offer(conn);
        }

    }

// --- Implementation 2: Semaphore + Queue with Timeout Support ---

    class AdvancedConnectionPool {

        private final Queue<Connection> pool = new LinkedList<>();

        private final Semaphore semaphore;

        private final Object lock = new Object();

        public AdvancedConnectionPool(int size) {
            semaphore = new Semaphore(size);
            for (int i = 0; i < size; i++) {
                pool.add(new DummyConnection(i));
            }
        }

        public Connection acquire(long timeout, TimeUnit unit) throws InterruptedException {
            if (!semaphore.tryAcquire(timeout, unit)) return null;
            synchronized (lock) {
                return pool.poll();
            }
        }

        public void release(Connection conn) {
            synchronized (lock) {
                pool.offer(conn);
            }
            semaphore.release();
        }

    }

// --- Unit Test ---

    @Test
    public void test() throws InterruptedException {
        System.out.println("-- SimpleConnectionPool Test --");
        SimpleConnectionPool simplePool = new SimpleConnectionPool(2);
        Connection c1 = simplePool.acquire();
        Connection c2 = simplePool.acquire();
        new Thread(() -> {
            try {
                Connection c3 = simplePool.acquire();
                c3.execute("SELECT * FROM delayed");
                simplePool.release(c3);
            } catch (InterruptedException ignored) {
            }
        }).start();
        Thread.sleep(1000);
        simplePool.release(c1);
        simplePool.release(c2);

        System.out.println("-- AdvancedConnectionPool Test --");
        AdvancedConnectionPool advPool = new AdvancedConnectionPool(2);
        Connection a1 = advPool.acquire(1, TimeUnit.SECONDS);
        Connection a2 = advPool.acquire(1, TimeUnit.SECONDS);
        Connection a3 = advPool.acquire(1, TimeUnit.SECONDS);
        System.out.println("Third acquire: " + (a3 == null ? "timed out" : "success"));
        advPool.release(a1);
        advPool.release(a2);
    }

}