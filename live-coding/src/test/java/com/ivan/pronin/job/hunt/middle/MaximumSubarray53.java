package com.ivan.pronin.job.hunt.middle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class MaximumSubarray53 {

    /*
     * 53. Maximum Subarray
     * Given an integer array nums, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.
     * Example 1:
     * Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
     * Output: 6
     * Explanation: [4,-1,2,1] has the largest sum = 6.
     * Example 2:
     * Input: nums = [1]
     * Output: 1
     * Example 3:
     * Input: nums = [5,4,-1,7,8]
     * Output: 23
     *
     * 📊 Сложность:
     * Time: O(n) — каждый элемент массива посещается один раз.
     * Space: O(1) — используем только несколько переменных для хранения текущей и максимальной суммы.
     */
    public static int maxSubArray(int[] nums) {
        int maxSum = nums[0];
        int currentSum = nums[0];

        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }

        return maxSum;
    }

    public static int maxSubArrayDivideConquer(int[] nums) {
        return divide(nums, 0, nums.length - 1);
    }

    private static int divide(int[] nums, int left, int right) {
        if (left == right) return nums[left];

        int mid = (left + right) / 2;
        int leftSum = divide(nums, left, mid);
        int rightSum = divide(nums, mid + 1, right);
        int crossSum = cross(nums, left, mid, right);

        System.out.println("left" + left + ", mid: " + mid + ", right: " + right + ", leftSum: " + leftSum + ", rightSum: " + rightSum + ", crossSum: " + crossSum);
        return Math.max(Math.max(leftSum, rightSum), crossSum);
    }

    private static int cross(int[] nums, int left, int mid, int right) {
        int leftMax = Integer.MIN_VALUE, sum = 0;
        for (int i = mid; i >= left; i--) {
            sum += nums[i];
            leftMax = Math.max(leftMax, sum);
        }

        int rightMax = Integer.MIN_VALUE;
        sum = 0;
        for (int i = mid + 1; i <= right; i++) {
            sum += nums[i];
            rightMax = Math.max(rightMax, sum);
        }

        return leftMax + rightMax;
    }

    @Test
    void testMixedPositiveAndNegative() {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assertEquals(6, MaximumSubarray53.maxSubArray(nums)); // [4,-1,2,1]
        assertEquals(6, MaximumSubarray53.maxSubArrayDivideConquer(nums)); // [4,-1,2,1]
    }

    @Test
    void testAllPositive() {
        int[] nums = {1, 2, 3, 4};
        assertEquals(10, MaximumSubarray53.maxSubArray(nums));
    }

    @Test
    void testAllNegative() {
        int[] nums = {-1, -2, -3};
        assertEquals(-1, MaximumSubarray53.maxSubArray(nums)); // только -1
    }

    @Test
    void testSingleElement() {
        int[] nums = {5};
        assertEquals(5, MaximumSubarray53.maxSubArray(nums));
    }

    @Test
    void testTwoElements() {
        int[] nums = {-2, 1};
        assertEquals(1, MaximumSubarray53.maxSubArray(nums));
    }

}
