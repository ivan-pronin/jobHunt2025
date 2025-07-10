package com.ivan.pronin.job.hunt.middle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class MaximumAvgSubarray643 {

    /*
     * 643. Maximum Average Subarray I
     * Given an integer array nums and an integer k, return the maximum average of any subarray of length k.
     * Example 1:
     * Input: nums = [1,12,-5,-6,50,3], k = 4
     *
     * Output: 12.75
     * Example 2:
     * Input: nums = [5], k = 1
     * Output: 5.0
     * 📊 Сложность:
     * Time	    O(n * k)
     * Space	O(1)
     */
    public static double findMaxAverageSimple(int[] nums, int k) {
        double maxAvg = Double.NEGATIVE_INFINITY;

        for (int i = 0; i <= nums.length - k; i++) {
            int sum = 0;
            for (int j = i; j < i + k; j++) {
                sum += nums[j];
            }
            maxAvg = Math.max(maxAvg, (double) sum / k);
        }

        return maxAvg;
    }

    /*
     * Оптимизированный подход с использованием скользящего окна:
     * 📊 Сложность:
     * Time: O(n) — один проход по массиву.
     * Space: O(1) — используем только несколько переменных для хранения суммы и максимума.
     *
     */
    public static double findMaxAverageSlidingWindow(int[] nums, int k) {
        int windowSum = 0;

        for (int i = 0; i < k; i++) {
            windowSum += nums[i];
        }

        int maxSum = windowSum;

        for (int i = k; i < nums.length; i++) {
            windowSum += nums[i] - nums[i - k];
            maxSum = Math.max(maxSum, windowSum);
        }

        return (double) maxSum / k;
    }

    @Test
    void testExample1() {
        int[] nums = {1, 12, -5, -6, 50, 3};
        int k = 4;
        assertEquals(12.75, MaximumAvgSubarray643.findMaxAverageSimple(nums, k), 0.0001);
        assertEquals(12.75, MaximumAvgSubarray643.findMaxAverageSlidingWindow(nums, k), 0.0001);
    }

    @Test
    void testAllNegatives() {
        int[] nums = {-1, -12, -5, -6, -50, -3};
        int k = 2;
        assertEquals(-5.5, MaximumAvgSubarray643.findMaxAverageSimple(nums, k), 0.0001);
        assertEquals(-5.5, MaximumAvgSubarray643.findMaxAverageSlidingWindow(nums, k), 0.0001);
    }

    @Test
    void testSingleElementWindow() {
        int[] nums = {1, 2, 3, 4};
        int k = 1;
        assertEquals(4.0, MaximumAvgSubarray643.findMaxAverageSimple(nums, k), 0.0001);
        assertEquals(4.0, MaximumAvgSubarray643.findMaxAverageSlidingWindow(nums, k), 0.0001);
    }

    @Test
    void testFullArrayWindow() {
        int[] nums = {2, 2, 2, 2};
        int k = 4;
        assertEquals(2.0, MaximumAvgSubarray643.findMaxAverageSimple(nums, k), 0.0001);
        assertEquals(2.0, MaximumAvgSubarray643.findMaxAverageSlidingWindow(nums, k), 0.0001);
    }

    @Test
    void testEdgeCaseLengthEqualsK() {
        int[] nums = {5};
        int k = 1;
        assertEquals(5.0, MaximumAvgSubarray643.findMaxAverageSimple(nums, k), 0.0001);
        assertEquals(5.0, MaximumAvgSubarray643.findMaxAverageSlidingWindow(nums, k), 0.0001);
    }

}
