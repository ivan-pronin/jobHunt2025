package com.ivan.pronin.job.hunt.senior.netflix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 22.07.2025
 */
public class MergeSortedArray88 {

    /*
     * Given two sorted integer arrays nums1 and nums2, merge nums2 into nums1 as one sorted array.
     * Example:
     * Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
     * Output: [1,2,2,3,5,6]
     *
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1;  // Указатель на последний элемент nums1
        int j = n - 1;  // Указатель на последний элемент nums2
        int k = m + n - 1;  // Указатель на последний индекс в nums1

        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--];
            } else {
                nums1[k--] = nums2[j--];
            }
        }

        // Если элементы в nums2 остались
        while (j >= 0) {
            nums1[k--] = nums2[j--];
        }
    }

    @Test
    public void testMerge() {
        int[] nums1_1 = {1, 2, 3, 0, 0, 0};
        int[] nums2_1 = {2, 5, 6};
        merge(nums1_1, 3, nums2_1, 3);
        assertArrayEquals(new int[]{1, 2, 2, 3, 5, 6}, nums1_1);

        int[] nums1_2 = {1};
        int[] nums2_2 = {};
        merge(nums1_2, 1, nums2_2, 0);
        assertArrayEquals(new int[]{1}, nums1_2);

        int[] nums1_3 = {0};
        int[] nums2_3 = {1};
        merge(nums1_3, 0, nums2_3, 1);
        assertArrayEquals(new int[]{1}, nums1_3);
    }

    public void merge2(int[] nums1, int m, int[] nums2, int n) {
        int[] result = new int[m + n];
        int i = 0, j = 0, k = 0;

        // Слияние двух массивов в новый результат
        while (i < m && j < n) {
            if (nums1[i] < nums2[j]) {
                result[k++] = nums1[i++];
            } else {
                result[k++] = nums2[j++];
            }
        }

        // Если в nums1 остались элементы
        while (i < m) {
            result[k++] = nums1[i++];
        }

        // Если в nums2 остались элементы
        while (j < n) {
            result[k++] = nums2[j++];
        }

        // Копируем результат обратно в nums1
        System.arraycopy(result, 0, nums1, 0, m + n);
    }

}
