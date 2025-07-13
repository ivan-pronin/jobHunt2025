package com.ivan.pronin.job.hunt.senior;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class FindMedianFromDataStream295Heaps {

    /*
     * 295. Find Median from Data Stream
     * Design a data structure that supports the following two operations:
     * 1. void addNum(int num) - Add a integer number from the data stream to the data structure.
     * 2. double findMedian() - Return the median of all elements so far.
     * Example:
     * Input: ["MedianFinder", "addNum", "addNum", "findMedian", "addNum", "findMedian"]
     *       Output: [null, null, null, 2.0, null, 3.0]
     *       Explanation:
     *       MedianFinder medianFinder = new MedianFinder();
     *      medianFinder.addNum(1);
     *      medianFinder.addNum(2);
     *     medianFinder.findMedian(); // return 1.5
     *     medianFinder.addNum(3);
     *    medianFinder.findMedian(); // return 2.0
     */
    private final PriorityQueue<Integer> left = new PriorityQueue<>(Comparator.reverseOrder()); // max-heap

    private final PriorityQueue<Integer> right = new PriorityQueue<>(); // min-heap

    /*
     * Добавляет число в структуру данных.
     * 📊 Сложность:
     * Time: O(log n) — где n — количество элементов в структуре, так как мы добавляем элемент в кучу.
     * Space: O(n) — храним все элементы в кучах.
     */
    public void addNum(int num) {
        left.offer(num);
        right.offer(left.poll());

        if (left.size() < right.size()) {
            left.offer(right.poll());
        }
    }

    /*
     * Находит медиану текущих элементов.
     * 📊 Сложность:
     * Time: O(1) — медиана находится за константное время, так как мы просто смотрим на вершины куч.
     * Space: O(1) — не используем дополнительную память для хранения медианы.
     */
    public double findMedian() {
        if (left.size() == right.size()) {
            return (left.peek() + right.peek()) / 2.0;
        } else {
            return left.peek();
        }
    }

    @Test
    public void testTwoHeaps() {
        addNum(1);
        addNum(2);
        assertEquals(1.5, findMedian());
        addNum(3);
        assertEquals(2.0, findMedian());
        addNum(4);
        addNum(5);
        assertEquals(3.0, findMedian());
    }

    @Test
    public void testTwoHeaps2() {
        addNum(2);
        addNum(20);
        addNum(15);
        addNum(11);
        addNum(4);
        addNum(10);
        addNum(5);
        addNum(1);
        addNum(12);
        addNum(8);
        addNum(7);
        addNum(9);
    }

    @Test
    public void testTwoHeaps3() {
        left.offer(2);
        left.offer(10);
        left.offer(20);
        left.offer(18);
        left.offer(9);
        left.offer(3);
        left.offer(15);
        left.offer(14);
        left.offer(13);
        left.offer(8);
        left.offer(7);
    }

}
