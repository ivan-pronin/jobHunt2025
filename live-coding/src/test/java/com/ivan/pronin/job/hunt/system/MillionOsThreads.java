package com.ivan.pronin.job.hunt.system;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class MillionOsThreads {

    public static void main(String[] args) throws InterruptedException {
        int count = 1_000;
        List<Runnable> threads = new ArrayList<>(count);
        List<Future<?>> results = new ArrayList<>(count);
        var now = Instant.now();
        System.out.println("Starting " + count + " real threads... at: " + now);
        // Using Thread.startVirtualThread() to create virtual threads
        // This is a simple example to demonstrate the creation of a large number of virtual threads
        // In a real application, you would use a thread pool or other concurrency mechanisms
        // to manage virtual threads more effectively.

        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(Duration.ofSeconds(10));
                } catch (InterruptedException ignored) {
                }
            });
            threads.add(thread);
        }

        try (var executor = java.util.concurrent.Executors.newFixedThreadPool(10)) {
            for (Runnable t : threads) {
                results.add(executor.submit(t));
            }
            System.out.println("Started " + count + " real threads");
            executor.shutdown();
            if (!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                final long count1 = results.stream()
                    .filter(r -> r.state() != Future.State.SUCCESS)
                    .count();
                System.err.println("Total " + count1 + " threads did not complete successfully at :" + Instant.now());
            }
            var duration = Duration.between(now, Instant.now());
            System.out.println("All threads completed in " + duration.toMillis() + " ms");
            // All threads completed in 16067 ms

        }


    }

}
