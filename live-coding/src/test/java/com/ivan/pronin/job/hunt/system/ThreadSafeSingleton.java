package com.ivan.pronin.job.hunt.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class ThreadSafeSingleton {

// --- Unit Test ---

    @Test
    public void test() throws InterruptedException {
        Set<Singleton1> singleton1Set = ConcurrentHashMap.newKeySet();
        Set<Singleton2> singleton2Set = ConcurrentHashMap.newKeySet();
        Set<Singleton3> singleton3Set = ConcurrentHashMap.newKeySet();

        Runnable task1 = () -> singleton1Set.add(Singleton1.getInstance());
        Runnable task2 = () -> singleton2Set.add(Singleton2.getInstance());
        Runnable task3 = () -> singleton3Set.add(Singleton3.INSTANCE);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Thread(task1));
            threads.add(new Thread(task2));
            threads.add(new Thread(task3));
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        System.out.println("Singleton1 unique instances: " + singleton1Set.size());
        System.out.println("Singleton2 unique instances: " + singleton2Set.size());
        System.out.println("Singleton3 unique instances: " + singleton3Set.size());

        assertEquals(1, singleton1Set.size());
        assertEquals(1, singleton2Set.size());
        assertEquals(1, singleton3Set.size());
    }

}
