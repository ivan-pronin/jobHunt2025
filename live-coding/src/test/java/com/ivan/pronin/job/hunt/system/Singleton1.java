package com.ivan.pronin.job.hunt.system;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class Singleton1 {

    // --- Implementation 1: Lazy Initialization with Synchronized Method ---
    private static Singleton1 instance;

    private Singleton1() {
    }

    public static synchronized Singleton1 getInstance() {
        if (instance == null) {
            instance = new Singleton1();
        }
        return instance;
    }

}
