package com.ivan.pronin.job.hunt.system;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public enum Singleton3 {
    INSTANCE;

    public void execute(String task) {
        System.out.println("Executing: " + task);
    }
}
