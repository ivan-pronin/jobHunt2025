package com.ivan.pronin.job.hunt.senior;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class TwoSum1 {

    /*
     * 1. Two Sum
     * Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
     * Example:
     * Input: nums = [2,7,11,15], target = 9
     * Output: [0,1]
     * You may assume that each input would have exactly one solution, and you may not use the same element twice.
     *
     * 📊 Сложность:
     * Time: O(n) — один проход по массиву.
     * Space: O(n) — используем хэш-таблицу для хранения индексов.
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};
            }
            seen.put(nums[i], i);
        }

        return new int[0]; // не найдено
    }

    @Test
    public void testHashMap() {
        assertArrayEquals(new int[]{0, 1}, twoSum(new int[]{2, 7, 11, 15}, 9));
        assertArrayEquals(new int[]{1, 2}, twoSum(new int[]{3, 2, 4}, 6));
        assertArrayEquals(new int[]{0, 1}, twoSum(new int[]{3, 3}, 6));
    }

}
