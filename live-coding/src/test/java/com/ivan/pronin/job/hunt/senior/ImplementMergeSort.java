package com.ivan.pronin.job.hunt.senior;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 13.07.2025
 */
public class ImplementMergeSort {
    /**
     * 912. Sort an Array
     * Implement merge sort algorithm to sort an array of integers.
     * Example:
     * Input: [5, 2, 3, 1]
     * Output: [1, 2, 3, 5]
     *
     * 📊 Сложность:
     * Time: O(n log n) — каждый уровень рекурсии обрабатывает n элементов, а высота дерева рекурсии составляет log n.
     * Space: O(n) — требуется дополнительная память для временных массивов.
     */
    public static int[] mergeSort(int[] nums) {
        if (nums.length < 2) {
            return nums;
        }
        int mid = nums.length / 2;
        int[] left = mergeSort(java.util.Arrays.copyOfRange(nums, 0, mid));
        int[] right = mergeSort(java.util.Arrays.copyOfRange(nums, mid, nums.length));
        return merge(left, right);
    }

    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }
        while (i < left.length) {
            result[k++] = left[i++];
        }
        while (j < right.length) {
            result[k++] = right[j++];
        }
        return result;
    }

    @Test
    public void testMergeSort() {
        int[] arr = {7, 3, 9, 1, 6, 2, 8};
        int[] expected = {1, 2, 3, 6, 7, 8, 9};
        int[] result = mergeSort(arr);
        Assertions.assertArrayEquals(expected, result);
    }

}
