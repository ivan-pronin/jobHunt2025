package com.ivan.pronin.job.hunt.system;

import java.util.Deque;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class RateLimiterOnTokenBucket {

    /*
     * 🧠 Суть задачи
     * Token Bucket (ведро с токенами) — это алгоритм ограничения пропускной способности, где:
     * Имеется "ведро", которое наполняется токенами со скоростью rate (токенов в секунду), до максимума capacity.
     * Каждый запрос "тратит" 1 токен.
     * Если токенов недостаточно — запрос отклоняется.
     */
    private static class NoSpaceSolution {

        private final int capacity;

        private final double refillRate; // tokens per second

        private double tokens;

        private long lastRefillTimestamp;

        public NoSpaceSolution(int capacity, double refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = capacity;
            this.lastRefillTimestamp = System.nanoTime();
        }

        public synchronized boolean allowRequest() {
            refill();
            if (tokens >= 1) {
                tokens -= 1;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.nanoTime();
            double secondsPassed = (now - lastRefillTimestamp) / 1_000_000_000.0;
            double tokensToAdd = secondsPassed * refillRate;
            if (tokensToAdd > 0) {
                tokens = Math.min(capacity, tokens + tokensToAdd);
                lastRefillTimestamp = now;
            }
        }

    }

    @Test
    public void testAllowRequest() throws InterruptedException {
        var limiter = new NoSpaceSolution(2, 1); // 2 tokens max, 1 per second

        assertTrue(limiter.allowRequest());
        assertTrue(limiter.allowRequest());
        assertFalse(limiter.allowRequest());

        Thread.sleep(1100); // wait for 1 token
        assertTrue(limiter.allowRequest());
        assertFalse(limiter.allowRequest());
    }

    private static class DequeSolution {

        private final int capacity;

        private final long refillIntervalNanos;

        private long lastRefillTsNanos;

        private final Deque<Long> timestamps;

        public DequeSolution(int capacity, int refillRatePerSec) {
            this.capacity = capacity;
            this.refillIntervalNanos = 1_000_000_000L / refillRatePerSec;
            this.lastRefillTsNanos = System.nanoTime();
            this.timestamps = new LinkedList<>();
            System.out.println("refillIntervalNanos=" + refillIntervalNanos);
            System.out.println("refillIntervalNanos * capacity=" + refillIntervalNanos * capacity);

        }

        public synchronized boolean allowRequest() {
            long now = System.nanoTime();
            System.out.println("Checking request at now: " + now + ". Deque is: " + timestamps + ". now - timestamps.peekFirst(): " + (now - (timestamps.peekFirst() == null ? 0 : timestamps.peekFirst())));

            var refills = 0;
            // Удаляем устаревшие токены
            var refillsCount = 0L;
            if (!timestamps.isEmpty()) refillsCount = Math.min(capacity, (now - lastRefillTsNanos) / refillIntervalNanos);
            while (!timestamps.isEmpty() && now- timestamps.peekFirst() >= refillIntervalNanos & refills < refillsCount) {
                System.out.println("Removing timestamp: " + timestamps.peekFirst() + " with diff: " + (now - timestamps.peekFirst()));
                if (refills < refillsCount) {
                    timestamps.pollFirst();
                    refills++;
                    lastRefillTsNanos = now;
                }
                refillsCount = Math.min(capacity, (timestamps.peekFirst() - lastRefillTsNanos) / refillIntervalNanos);
                System.out.println("Current deque: " + timestamps);
            }

            if (timestamps.size() < capacity) {
                timestamps.addLast(now);
                return true;
            }

            return false;
        }

    }

    @Test
    public void testAllowRequest2() throws InterruptedException {
        var limiter = new DequeSolution(2, 1); // 2 tokens max, 1 per second

        assertTrue(limiter.allowRequest());
        assertTrue(limiter.allowRequest());
        assertFalse(limiter.allowRequest());

        Thread.sleep(1100); // wait for 1 token
        assertTrue(limiter.allowRequest());
        assertFalse(limiter.allowRequest());
    }

}
