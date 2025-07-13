package com.ivan.pronin.job.hunt.senior;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 13.07.2025
 */
public class HouseRobber198 {
    /*
     * 198. House Robber
     * You are a professional robber planning to rob houses along a street.
     * Each house has a certain amount of money stashed, the only constraint stopping you from robbing each of them
     * is that adjacent houses have security systems connected
     * and it will automatically contact the police if two adjacent houses were broken into on the same night.
     * Given an integer array nums representing the amount of money of each house, return the maximum amount of money you can rob tonight without alerting the police.
     * Example:
     * Input: nums = [1,2,3,1]
     * Output: 4
     */
    public int rob(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];

        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        // dp[2] = Math.max(dp[1], dp[0] + nums[2]);
        // dp[3] = Math.max(dp[2], dp[1] + nums[3]);
        // dp[4] = Math.max(dp[3], dp[2] + nums[4]);


        return dp[nums.length - 1];
    }

    public int robOptimized(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];

        int prev1 = nums[0];
        int prev2 = Math.max(nums[0], nums[1]);

        for (int i = 2; i < nums.length; i++) {
            int current = Math.max(prev2, prev1 + nums[i]);
            prev1 = prev2;
            prev2 = current;
        }

        return prev2;
    }

    @Test
    public void testDPArray() {
        assertEquals(12, rob(new int[]{2,7,9,3,1}));
        assertEquals(4, rob(new int[]{1,2,3,1}));
        assertEquals(0, rob(new int[]{}));
        assertEquals(2, rob(new int[]{2}));
    }

    @Test
    public void testDPSpaceOptimized() {
        assertEquals(4, robOptimized(new int[]{1,2,3,1}));
        assertEquals(12, robOptimized(new int[]{2,7,9,3,1}));
        assertEquals(0, robOptimized(new int[]{}));
        assertEquals(2, robOptimized(new int[]{2}));
    }

}
