package com.ivan.pronin.job.hunt.system.fintech;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 17.07.2025
 */
public class FraudDetectionAlgorithm {

    /* --- Implementation: Simple Threshold Check ---
     * Simple fraud detection algorithm that checks if the transaction amount exceeds a threshold
     * and if the frequency of transactions from a user exceeds a threshold within a time window.
     */
    private static class FraudDetectorSimple {

        private final double amountThreshold;

        private final int frequencyThreshold;

        private final long timeWindowMillis;

        private final Map<String, Deque<Long>> userTimestamps = new HashMap<>();

        public FraudDetectorSimple(double amountThreshold, int frequencyThreshold, long timeWindowMillis) {
            this.amountThreshold = amountThreshold;
            this.frequencyThreshold = frequencyThreshold;
            this.timeWindowMillis = timeWindowMillis;
        }

        public boolean isFraudulent(String userId, double amount, long timestampMillis) {
            if (amount > amountThreshold) return true;

            userTimestamps.putIfAbsent(userId, new ArrayDeque<>());
            Deque<Long> timestamps = userTimestamps.get(userId);

            while (!timestamps.isEmpty() && timestampMillis - timestamps.peekFirst() > timeWindowMillis) {
                timestamps.pollFirst();
            }

            timestamps.addLast(timestampMillis);
            return timestamps.size() > frequencyThreshold;
        }

    }

    @Test
    public void testSimpleFraudDetection() {
        FraudDetectorSimple detector = new FraudDetectorSimple(1000.0, 3, 10_000);

        long now = System.currentTimeMillis();
        assertFalse(detector.isFraudulent("user1", 100, now));
        assertFalse(detector.isFraudulent("user1", 150, now + 1000));
        assertFalse(detector.isFraudulent("user1", 200, now + 2000));
        assertTrue(detector.isFraudulent("user1", 50, now + 3000)); // too frequent
        assertTrue(detector.isFraudulent("user2", 2000, now)); // too large
    }

    private static class FraudDetectorZScore {
        private final Map<String, List<Double>> userAmounts = new HashMap<>();

        public boolean isFraudulent(String userId, double amount) {
            userAmounts.putIfAbsent(userId, new ArrayList<>());
            List<Double> history = userAmounts.get(userId);

            if (history.size() < 10) {
                history.add(amount);
                return false; // недостаточно данных
            }

            double mean = history.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            double stdDev = Math.sqrt(history.stream()
                .mapToDouble(v -> (v - mean) * (v - mean)).sum() / history.size());

            history.add(amount);
            if (history.size() > 100) history.remove(0);

            return stdDev > 0 && Math.abs(amount - mean) > 3 * stdDev;
        }
    }

    @Test
    public void testZScoreDetection() {
        FraudDetectorZScore detector = new FraudDetectorZScore();

        for (int i = 0; i < 20; i++) {
            assertFalse(detector.isFraudulent("user1", 100 + Math.random() * 10));
        }

        // Аномально высокая транзакция
        assertTrue(detector.isFraudulent("user1", 1000.0));
    }

}
