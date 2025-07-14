package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 13.07.2025
 */
public class ImplementPriorityQueue {

    /*
     * Implement Priority Queue
     * Design a priority queue that supports the following operations:
     * - insert(int x): Inserts an integer x into the priority queue.
     * - remove(): Removes and returns the highest priority element (the smallest element).
     * - peek(): Returns the highest priority element without removing it.
     * - isEmpty(): Returns true if the priority queue is empty, false otherwise.
     * Example:
     * Input: ["PriorityQueue", "insert", "insert", "peek", "remove", "isEmpty"]
     * Output: [null, null, null, 1, 1, false]
     */
    private static class MinPriorityQueue {

        private final List<Integer> heap = new ArrayList<>();

        public void offer(int val) {
            heap.add(val);
            bubbleUp(heap.size() - 1);
        }

        public int poll() {
            if (heap.isEmpty()) throw new NoSuchElementException();
            int min = heap.get(0);
            int last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                bubbleDown(0);
            }
            return min;
        }

        public int peek() {
            if (heap.isEmpty()) throw new NoSuchElementException();
            return heap.get(0);
        }

        public boolean isEmpty() {
            return heap.isEmpty();
        }

        private void bubbleUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i) >= heap.get(parent)) break;
                Collections.swap(heap, i, parent);
                i = parent;
            }
        }

        private void bubbleDown(int i) {
            int n = heap.size();
            while (i < n) {
                int left = 2 * i + 1, right = 2 * i + 2, smallest = i;
                if (left < n && heap.get(left) < heap.get(smallest)) smallest = left;
                if (right < n && heap.get(right) < heap.get(smallest)) smallest = right;
                if (smallest == i) break;
                Collections.swap(heap, i, smallest);
                i = smallest;
            }
        }

    }

    private static class TreeMapSolution {
        private final TreeMap<Integer, Integer> map = new TreeMap<>();

        public void offer(int val) {
            map.put(val, map.getOrDefault(val, 0) + 1);
        }

        public int poll() {
            if (map.isEmpty()) throw new NoSuchElementException();
            int min = map.firstKey();
            if (map.get(min) == 1) map.remove(min);
            else map.put(min, map.get(min) - 1);
            return min;
        }

        public int peek() {
            if (map.isEmpty()) throw new NoSuchElementException();
            return map.firstKey();
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }
    }

    @Test
    public void testPriorityQueue() {
        MinPriorityQueue pq = new MinPriorityQueue();
        pq.offer(3);
        pq.offer(1);
        pq.offer(2);

        assert !pq.isEmpty();
        assert pq.peek() == 1;
        assert pq.poll() == 1;
        assert pq.peek() == 2;
        assert pq.poll() == 2;
        assert pq.peek() == 3;
        assert pq.poll() == 3;
        assert pq.isEmpty();
    }

}
