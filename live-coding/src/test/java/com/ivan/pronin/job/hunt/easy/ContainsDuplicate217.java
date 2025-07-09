package com.ivan.pronin.job.hunt.easy;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class ContainsDuplicate217 {

    /*
     * Given an integer array nums, return true if any value appears at least twice
     * in the array, and return false if every element is distinct.
     */
    /*
     * 📊 Сложность:
     * Time: O(n) — один проход по массиву.
     * Space: O(n) — в худшем случае все элементы уникальны.
     */
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            if (!seen.add(num)) {
                return true; // если не удалось добавить — дубликат найден
            }
        }
        return false;
    }

    // Альтернативный подход с использованием сортировки:
    /* 📊 Сложность:
     * Time: O(n log n) — из-за сортировки.
     * Space: O(1) (если сортировка in-place, как у Arrays.sort для int[]).
     */
    public boolean containsDuplicateAlt(int[] nums) {
        Arrays.sort(nums); // сортировка
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                return true;
            }
        }
        return false;
    }

    public boolean containsDuplicateBitSet(int[] nums) {
        BitSet bitSet = new BitSet();

        for (int num : nums) {
            if (bitSet.get(num)) {
                return true; // число уже встречалось
            }
            bitSet.set(num); // пометить число как встреченное
        }

        return false;
    }


    @Test
    void testContainsDuplicate_WithDuplicates() {
        ContainsDuplicate217 solution = new ContainsDuplicate217();
        int[] nums = {1, 2, 3, 1};
        Assertions.assertTrue(solution.containsDuplicate(nums));
    }

    @Test
    void testContainsDuplicate_NoDuplicates() {
        ContainsDuplicate217 solution = new ContainsDuplicate217();
        int[] nums = {1, 2, 3, 4};
        Assertions.assertFalse(solution.containsDuplicate(nums));
    }

    @Test
    void testContainsDuplicate_EmptyArray() {
        ContainsDuplicate217 solution = new ContainsDuplicate217();
        int[] nums = {};
        Assertions.assertFalse(solution.containsDuplicate(nums));
    }

    @Test
    void testContainsDuplicate_SingleElement() {
        ContainsDuplicate217 solution = new ContainsDuplicate217();
        int[] nums = {42};
        Assertions.assertFalse(solution.containsDuplicate(nums));
    }

    @Test
    void testContainsDuplicate_AllDuplicates() {
        ContainsDuplicate217 solution = new ContainsDuplicate217();
        int[] nums = {7, 7, 7, 7};
        Assertions.assertTrue(solution.containsDuplicate(nums));
    }

}
