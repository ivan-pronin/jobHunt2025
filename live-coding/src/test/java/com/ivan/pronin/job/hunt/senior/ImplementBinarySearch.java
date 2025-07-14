package com.ivan.pronin.job.hunt.senior;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 13.07.2025
 */
public class ImplementBinarySearch {

    /*
     * 704. Binary Search
     * Implement binary search algorithm.
     * Given a sorted array of integers nums and an integer target, write a function to search for target in nums.
     * If target exists, then return its index. Otherwise, return -1.
     * You must write an algorithm with O(log n) runtime complexity.
     * Example:
     * Input: nums = [-1,0,3,5,9,12], target = 9
     * Output: 4
     */
    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // Избегаем переполнения

            if (nums[mid] == target) {
                return mid; // Найден элемент
            } else if (nums[mid] < target) {
                left = mid + 1; // Ищем в правой половине
            } else {
                right = mid - 1; // Ищем в левой половине
            }
        }
        return -1; // Элемент не найден
    }

    @Test
    public void testBinarySearch() {
        ImplementBinarySearch binarySearch = new ImplementBinarySearch();
        int[] nums = {-1, 0, 3, 5, 9, 12};
        int target = 9;
        Assertions.assertEquals(4, binarySearch.search(nums, target));

        target = 0;
        Assertions.assertEquals(1, binarySearch.search(nums, target));
    }

}
