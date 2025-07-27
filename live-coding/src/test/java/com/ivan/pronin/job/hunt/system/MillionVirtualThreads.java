package com.ivan.pronin.job.hunt.system;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MillionVirtualThreads {

    public static void main(String[] args) throws InterruptedException {
        int count = 1_000_000;
        List<Thread> threads = new ArrayList<>(count);
        var now = Instant.now();
        System.out.println("Starting " + count + " virtual threads... at: " + now);
        // Using Thread.startVirtualThread() to create virtual threads
        // This is a simple example to demonstrate the creation of a large number of virtual threads
        // In a real application, you would use a thread pool or other concurrency mechanisms
        // to manage virtual threads more effectively.
        System.out.println("");

        for (int i = 0; i < count; i++) {
            Thread thread = Thread.startVirtualThread(() -> {
                try {
                    Thread.sleep(Duration.ofSeconds(10));
                } catch (InterruptedException ignored) {
                }
            });
            threads.add(thread);
        }

        System.out.println("Started " + count + " virtual threads");
        for (Thread t : threads) {
            t.join();
        }
        var duration = Duration.between(now, Instant.now());
        System.out.println("All threads completed in " + duration.toMillis() + " ms");
        // All threads completed in 16067 ms
    }

}
