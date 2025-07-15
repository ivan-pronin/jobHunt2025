package com.ivan.pronin.job.hunt.system;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class CircuitBreakerPattern {

    /* 🔌 Что такое Circuit Breaker?
     * Circuit Breaker (предохранитель) — это шаблон отказоустойчивости, который защищает систему от постоянных попыток выполнить сбойные операции. Он работает как автоматический выключатель.
     *  3 состояния:
     * CLOSED — все работает, запросы проходят
     * OPEN — внешняя система нестабильна, запросы блокируются
     * HALF_OPEN — период проверки: часть запросов разрешается
     * ✅ Решение 1: Простой Circuit Breaker по количеству ошибок
     * 📌 Идея:
     * Если подряд произошло failureThreshold ошибок → переходим в OPEN
     * Через resetTimeoutMillis мс → пробуем снова (в состоянии HALF_OPEN)
     * Если пробный вызов прошел — возвращаемся в CLOSED, иначе снова OPENя
     */

    public class SimpleCircuitBreaker {

        enum State {CLOSED, OPEN, HALF_OPEN}

        private final int failureThreshold;

        private final long resetTimeoutMillis;

        private State state = State.CLOSED;

        private int failureCount = 0;

        private long lastFailureTime = 0;

        public SimpleCircuitBreaker(int failureThreshold, long resetTimeoutMillis) {
            this.failureThreshold = failureThreshold;
            this.resetTimeoutMillis = resetTimeoutMillis;
        }

        public synchronized <T> T call(Supplier<T> operation, Supplier<T> fallback) {
            long now = System.currentTimeMillis();

            switch (state) {
                case OPEN:
                    if (now - lastFailureTime >= resetTimeoutMillis) {
                        state = State.HALF_OPEN;
                    } else {
                        return fallback.get();
                    }
                    break;
                case HALF_OPEN:
                    // allow just 1 request to test system
                    break;
                case CLOSED:
                    break;
            }

            try {
                T result = operation.get();
                reset();
                return result;
            } catch (Exception e) {
                recordFailure();
                return fallback.get();
            }
        }

        private void recordFailure() {
            failureCount++;
            lastFailureTime = System.currentTimeMillis();
            if (failureCount >= failureThreshold) {
                state = State.OPEN;
            }
        }

        private void reset() {
            state = State.CLOSED;
            failureCount = 0;
        }

        public State getState() {
            return state;
        }

    }

    @Test
    public void testCircuitBreaker() throws InterruptedException {
        SimpleCircuitBreaker cb = new SimpleCircuitBreaker(2, 1000);

        Supplier<String> fail = () -> {
            throw new RuntimeException("fail");
        };
        Supplier<String> fallback = () -> "fallback";

        assertEquals("fallback", cb.call(fail, fallback)); // 1 fail
        assertEquals("fallback", cb.call(fail, fallback)); // 2 fail → opens

        assertEquals(SimpleCircuitBreaker.State.OPEN, cb.getState());
        assertEquals("fallback", cb.call(fail, fallback)); // short-circuited

        Thread.sleep(1100); // wait for timeout
        assertEquals("fallback", cb.call(fail, fallback)); // HALF_OPEN fail → OPEN again

        Supplier<String> success = () -> "ok";
        Thread.sleep(1100);
        assertEquals("ok", cb.call(success, fallback)); // HALF_OPEN success → CLOSED
        assertEquals(SimpleCircuitBreaker.State.CLOSED, cb.getState());
    }

    public class SlidingWindowCircuitBreaker {

        enum State {CLOSED, OPEN, HALF_OPEN}

        private final Deque<Boolean> history = new ArrayDeque<>();

        private final int windowSize;

        private final double failureRateThreshold;

        private final long resetTimeoutMillis;

        private State state = State.CLOSED;

        private long lastFailureTime = 0;

        public SlidingWindowCircuitBreaker(int windowSize, double failureRateThreshold, long resetTimeoutMillis) {
            this.windowSize = windowSize;
            this.failureRateThreshold = failureRateThreshold;
            this.resetTimeoutMillis = resetTimeoutMillis;
        }

        public synchronized <T> T call(Supplier<T> op, Supplier<T> fallback) {
            long now = System.currentTimeMillis();

            if (state == State.OPEN) {
                if (now - lastFailureTime >= resetTimeoutMillis) {
                    state = State.HALF_OPEN;
                } else {
                    return fallback.get();
                }
            }

            try {
                T result = op.get();
                recordSuccess();
                return result;
            } catch (Exception e) {
                recordFailure();
                return fallback.get();
            }
        }

        private void recordSuccess() {
            if (state == State.HALF_OPEN) {
                state = State.CLOSED;
                history.clear();
            }
            history.addLast(true);
            if (history.size() > windowSize) history.pollFirst();
        }

        private void recordFailure() {
            history.addLast(false);
            if (history.size() > windowSize) history.pollFirst();

            long failures = history.stream().filter(s -> !s).count();
            if ((double) failures / history.size() > failureRateThreshold) {
                state = State.OPEN;
                lastFailureTime = System.currentTimeMillis();
            }
        }

        public State getState() {
            return state;
        }

    }

    @Test
    public void testSlidingWindow() throws InterruptedException {
        SlidingWindowCircuitBreaker cb = new SlidingWindowCircuitBreaker(4, 0.5, 1000);

        Supplier<String> fail = () -> { throw new RuntimeException("fail"); };
        Supplier<String> ok = () -> "ok";
        Supplier<String> fallback = () -> "fallback";

        cb.call(fail, fallback);
        cb.call(ok, fallback);
        cb.call(fail, fallback);
        cb.call(fail, fallback); // 3/4 failures → > 0.5

        assertEquals(SlidingWindowCircuitBreaker.State.OPEN, cb.getState());

        Thread.sleep(1100);
        cb.call(fail, fallback); // still failing in HALF_OPEN → OPEN again

        Thread.sleep(1100);
        cb.call(ok, fallback); // HALF_OPEN succeed → CLOSED
        assertEquals(SlidingWindowCircuitBreaker.State.CLOSED, cb.getState());
    }

}
