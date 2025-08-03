package com.ivan.pronin.job.hunt.neetcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 30.07.2025
 */
public class FindMinimumInSortedRotatedArr {

    class Solution {

        public int findMin(int[] nums) {
            int l = 0, r = nums.length - 1;
            while (l < r) {
                int m = (l + r) / 2;
                if (l - r == -1) {
                    return Math.min(nums[l], nums[r]);
                }
                System.out.println("Checking at l: " + l + " mid: " + m + " r: " + r);
                if (nums[l] < nums[m] && nums[m] < nums[r]) {
                    return nums[l]; // The array is not rotated, return the first element
                }
                if (nums[l] > nums[m])  r = m; // search in the left subarray
                if (nums[m] > nums[r])  l = m; // search in the right subarray
            }
            return -9999;
        }

    }

    @Test
    void testFindMin() {
        Solution solution = new Solution();
        int[] nums = {3, 4, 5, 6, 1, 2};
        int min = solution.findMin(nums);
        System.out.println("Minimum in rotated sorted array: " + min);
        assertEquals(1, min);
    }

}
