package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class SlidingWindowMax239 {
    /*
     * 239. Sliding Window Maximum
     * You are given an array of integers nums, there is a sliding window of size k which is moving
     * from the very left of the array to the very right.
     * You can only see the k numbers in the window. Each time the sliding window moves right by one position.
     * Return the max sliding window.
     * Example 1:
     * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
     * Output: [3,3,5,5,6,7]
     * Example 2:
     * Input: nums = [1], k = 1
     * Output: [1]
     * 📊 Сложность:
     * Time: O(n log k) — каждый элемент добавляется и удаляется из кучи, что занимает O(log k) время.
     * Space: O(k) — храним максимум в куче, которая может содержать до k элементов.
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            maxHeap.offer(new int[]{nums[i], i});
            if (i >= k - 1) {
                // удаляем устаревшие элементы вне окна
                while (maxHeap.peek()[1] <= i - k) {
                    maxHeap.poll();
                }
                result.add(maxHeap.peek()[0]);
            }
        }

        return result.stream().mapToInt(i -> i).toArray();
    }

    public static int[] maxSlidingWindowDeque(int[] nums, int k) {
        Deque<Integer> deque = new ArrayDeque<>();
        int[] result = new int[nums.length - k + 1];
        int ri = 0;

        for (int i = 0; i < nums.length; i++) {
            // Удаляем элементы вне окна
            if (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }

            // Удаляем все элементы меньше текущего
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }

            deque.offerLast(i);

            if (i >= k - 1) {
                result[ri++] = nums[deque.peekFirst()];
            }
        }

        return result;
    }

    @Test
    public void testSlidingWindowMax() {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        maxHeap.offer(new int[]{10, 1});
        maxHeap.offer(new int[]{20, 2});
        maxHeap.offer(new int[]{30, 3});
        System.out.println(maxHeap.poll()[1]);
        System.out.println(maxHeap.poll()[1]);
        System.out.println(maxHeap.poll()[1]);
    }

    @Test
    public void testHeapSolution() {
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, maxSlidingWindow(new int[]{1,3,-1,-3,5,3,6,7}, 3));
        assertArrayEquals(new int[]{1}, maxSlidingWindow(new int[]{1}, 1));
        assertArrayEquals(new int[]{2, 2, 2}, maxSlidingWindow(new int[]{1,2,2,1}, 2));
    }

    @Test
    public void testDequeSolution() {
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, maxSlidingWindowDeque(new int[]{1,3,-1,-3,5,3,6,7}, 3));
        assertArrayEquals(new int[]{1}, maxSlidingWindowDeque(new int[]{1}, 1));
        assertArrayEquals(new int[]{2, 2, 2}, maxSlidingWindowDeque(new int[]{1,2,2,1}, 2));
    }

}
