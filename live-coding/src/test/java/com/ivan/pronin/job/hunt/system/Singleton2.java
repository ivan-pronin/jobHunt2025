package com.ivan.pronin.job.hunt.system;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class Singleton2 {

    // --- Implementation 2: Double-Checked Locking ---// --- Explanation ---
    // In Singleton2 (Double-Checked Locking), instance == null is checked twice:
    // 1. First check avoids locking if instance is already created (fast path)
    // 2. Second check is necessary to ensure no other thread created the instance
    //    between the first check and acquiring the lock.
    //    Without the second check, multiple instances could be created in a race condition.
    private static volatile Singleton2 instance;

    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        if (instance == null) {
            synchronized (Singleton2.class) {
                if (instance == null) {
                    instance = new Singleton2();
                }
            }
        }
        return instance;
    }

}
