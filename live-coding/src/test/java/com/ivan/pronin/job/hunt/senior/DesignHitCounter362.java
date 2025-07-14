package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 13.07.2025
 */
public class DesignHitCounter362 {

    /*
     * 362. Design Hit Counter
     * Design a hit counter which counts the number of hits received in the past 5 minutes.
     * Each function accepts a timestamp parameter (in seconds granularity) and you may assume that calls are being made to the system in chronological
     *  order (i.e., timestamp is monotonically increasing).
     * You may assume that the earliest timestamp starts at 1.
     * Implement the HitCounter class:
     * HitCounter() Initializes the hit counter object.
     * void hit(int timestamp) Records a hit that happened at timestamp (in seconds).
     * int getHits(int timestamp) Returns the number of hits in the past 5 minutes from timestamp.
     */
    // 📊 Сложность:
    // Time: O(1) for hit, O(n) for getHits, where n is the number of hits in the last 5 minutes.
    // Space: O(n) for storing hits, where n is the number of hits in the last 5 minutes.
    private static class HitCounterTreeMap {

        private static final int WINDOW_OFFSET_SECONDS = 300;

        private final Deque<Hit> hits = new ArrayDeque<>();

        private final SortedMap<Integer, Integer> hitsSorted = new TreeMap<>();

        void hit(int timestamp) {
            hitsSorted.put(timestamp, hitsSorted.getOrDefault(timestamp, 0) + 1);
        }

        int getHits(int timestamp) {
            var keyToSearch = timestamp - WINDOW_OFFSET_SECONDS;
            var mapWithinWindow = hitsSorted.tailMap(keyToSearch + 1);
            // Clean up old hits from hitsSorted
            final SortedMap<Integer, Integer> oldHits = hitsSorted.headMap(keyToSearch + 1);
            oldHits.clear();

            return mapWithinWindow.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        }

        private static class Hit {

            int timestamp;

            int count;

            Hit(int timestamp) {
                this.timestamp = timestamp;
                this.count = 1;
            }

            void increment() {
                count++;
            }

        }

    }

    @Test
    public void testQueueTreeMap() {
        var counter = new HitCounterTreeMap();
        counter.hit(1);
        counter.hit(2);
        counter.hit(3);
        assertEquals(3, counter.getHits(4));
        counter.hit(300);
        assertEquals(4, counter.getHits(300));
        assertEquals(3, counter.getHits(301));
    }

    private static class HitCounterDeque {

        private final Deque<int[]> hits = new ArrayDeque<>();

        void hit(int timestamp) {
            if (hits.isEmpty() || hits.peekLast()[0] != timestamp) {
                hits.offerLast(new int[]{timestamp, 1});
            } else {
                hits.peekLast()[1]++;
            }
        }

        int getHits(int timestamp) {
            int windowStart = timestamp - 300;
            while (!hits.isEmpty() && hits.peekFirst()[0] <= windowStart) {
                hits.pollFirst();
            }
            return hits.stream()
                .mapToInt(hit -> hit[1])
                .sum();
        }

    }

    @Test
    public void testQueue() {
        var counter = new HitCounterDeque();
        counter.hit(1);
        counter.hit(2);
        counter.hit(3);
        assertEquals(3, counter.getHits(4));
        counter.hit(300);
        assertEquals(4, counter.getHits(300));
        assertEquals(3, counter.getHits(301));
    }


    private static class HitCounterArray {
        private final int[] times = new int[300];
        private final int[] hits = new int[300];

        public void hit(int timestamp) {
            int index = timestamp % 300;
            if (times[index] != timestamp) {
                times[index] = timestamp;
                hits[index] = 1;
            } else {
                hits[index]++;
            }
        }

        public int getHits(int timestamp) {
            int total = 0;
            for (int i = 0; i < 300; i++) {
                if (timestamp - times[i] < 300) {
                    total += hits[i];
                }
            }
            return total;
        }
    }

    @Test
    public void testArray() {
        var counter = new HitCounterArray();
        counter.hit(1);
        counter.hit(2);
        counter.hit(3);
        assertEquals(3, counter.getHits(4));
        counter.hit(300);
        assertEquals(4, counter.getHits(300));
        assertEquals(3, counter.getHits(301));
    }


}
