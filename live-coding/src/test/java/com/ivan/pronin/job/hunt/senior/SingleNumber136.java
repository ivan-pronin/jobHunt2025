package com.ivan.pronin.job.hunt.senior;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 13.07.2025
 */
public class SingleNumber136 {

    /*
     * 136. Single Number
     * Given a non-empty array of integers nums, every element appears twice except for one.
     * Find that single one.
     * You must implement a solution with a linear runtime complexity and use only constant extra space.
     * Example:
     * Input: nums = [2,2,1]
     * Output: 1
     */
    public int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }

    @Test
    public void testXOR() {
        assertEquals(1, singleNumber(new int[]{2, 2, 1}));
        assertEquals(4, singleNumber(new int[]{4, 1, 2, 1, 2}));
        assertEquals(99, singleNumber(new int[]{99}));

        System.out.println(0 ^ 4 ^ 6 ^ 4 ^ 6); // 0
    }

}
