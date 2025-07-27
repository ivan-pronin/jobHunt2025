package com.ivan.pronin.job.hunt.senior.netflix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 21.07.2025
 */
public class SumOfThree15 {

    /*
        * Given an array of integers, find all unique triplets (i, j, k) such that
        * i < j < k and arr[i] + arr[j] + arr[k] == target.
        * * Example:
        *  Input: arr = [1, 2, -2, 0, 3], target = 3
        * Output: [[-2, 1, 3], [0, 2, 1]]
        * 📊 Сложность:
        *
     */
    private static class TwoPointerSolution {
        public List<List<Integer>> threeSum(int[] nums) {
            List<List<Integer>> result = new ArrayList<>();
            if (nums == null || nums.length < 3) return result;

            Arrays.sort(nums);

            for (int i = 0; i < nums.length - 2; i++) {
                // Пропускаем одинаковые элементы, чтобы избежать дубликатов
                if (i > 0 && nums[i] == nums[i - 1]) continue;

                int left = i + 1, right = nums.length - 1;
                while (left < right) {
                    int sum = nums[i] + nums[left] + nums[right];
                    if (sum == 0) {
                        result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                        // Пропускаем одинаковые элементы
                        while (left < right && nums[left] == nums[left + 1]) left++;
                        while (left < right && nums[right] == nums[right - 1]) right--;
                        left++;
                        right--;
                    } else if (sum < 0) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }

            return result;
        }
    }

    @Test
    public void testThreeSum() {
        var solution = new TwoPointerSolution();

        int[] nums1 = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> result1 = solution.threeSum(nums1);
        assertEquals(2, result1.size());
        assertTrue(result1.contains(Arrays.asList(-1, -1, 2)));
        assertTrue(result1.contains(Arrays.asList(-1, 0, 1)));

        int[] nums2 = {0, 0, 0, 0};
        List<List<Integer>> result2 = solution.threeSum(nums2);
        assertEquals(1, result2.size());
        assertTrue(result2.contains(Arrays.asList(0, 0, 0)));

        int[] nums3 = {1, 2, 3};
        List<List<Integer>> result3 = solution.threeSum(nums3);
        assertEquals(0, result3.size());
    }

    private static class ComplementSolution {
        public List<List<Integer>> threeSum(int[] nums) {
            Set<List<Integer>> resultSet = new HashSet<>();
            if (nums == null || nums.length < 3) return new ArrayList<>(resultSet);

            for (int i = 0; i < nums.length - 2; i++) {
                Set<Integer> seen = new HashSet<>();
                for (int j = i + 1; j < nums.length; j++) {
                    int complement = -nums[i] - nums[j];
                    if (seen.contains(complement)) {
                        List<Integer> triplet = Arrays.asList(nums[i], nums[j], complement);
                        resultSet.add(triplet);
                    }
                    seen.add(nums[j]);
                }
            }

            return new ArrayList<>(resultSet);
        }
    }

    @Test
    public void testThreeSum2() {
        var solution = new ComplementSolution();

        int[] nums1 = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> result1 = solution.threeSum(nums1);
        assertEquals(2, result1.size());
        assertTrue(result1.contains(Arrays.asList(-1, -1, 2)));
        assertTrue(result1.contains(Arrays.asList(-1, 0, 1)));

        int[] nums2 = {0, 0, 0, 0};
        List<List<Integer>> result2 = solution.threeSum(nums2);
        assertEquals(1, result2.size());
        assertTrue(result2.contains(Arrays.asList(0, 0, 0)));

        int[] nums3 = {1, 2, 3};
        List<List<Integer>> result3 = solution.threeSum(nums3);
        assertEquals(0, result3.size());
    }

}
