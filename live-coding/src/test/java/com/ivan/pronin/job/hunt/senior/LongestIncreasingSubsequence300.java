package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class LongestIncreasingSubsequence300 {

    /*
     * 300. Longest Increasing Subsequence
     * Given an integer array nums, return the length of the longest strictly increasing subsequence.
     * Example 1:
     * Input: nums = [10,9,2,5,3,7,101,18]
     * Output: 4
     * Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
     *
     * 📊 Сложность:
     * Time: O(n^2) — где n — количество элементов в массиве, так как мы сравниваем каждый элемент с предыдущими.
     * Space: O(n) — используется массив dp для хранения длины наибольшей возрастающей подпоследовательности для каждого элемента.
     *
     */
    public int lengthOfLIS(int[] nums) {
        if (nums.length == 0) return 0;
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);

        int max = 1;
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    /*
     * Решение с использованием двоичного поиска
     * 📊 Сложность:
     * Time: O(n log n) — где n — количество элементов в массиве, так как мы используем двоичный поиск для вставки каждого элемента.
     * Space: O(n) — используется массив tails для хранения концов возможных возрастающих подпоследовательностей.
     * Примечание: это решение эффективнее, чем O(n^2), особенно для больших массивов.
     * Примечание: здесь мы используем tails для хранения концов возможных возрастающих подпоследовательностей.
     * Если мы можем расширить существующую подпоследовательность, то мы обновляем конец этой подпоследовательности.
     * Если текущий элемент больше всех концов, то мы добавляем его как новый конец.
     * Если текущий элемент меньше или равен какому-то концу, то мы заменяем этот конец на текущий элемент.
     * Это позволяет нам поддерживать массив концов возможных возрастающих подпоследовательностей,
     * что позволяет эффективно находить длину наибольшей возрастающей подпоследовательности.
     */
    public int lengthOfLISBinarySearch(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;

        for (int num : nums) {
            int left = 0, right = size;
            while (left < right) {
                int mid = (left + right) / 2;
                if (tails[mid] < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            tails[left] = num;
            if (left == size) size++; // <== вот этот счетчик LIS
        }

        return size;
    }

    public int lengthOfLISCollectionsSort(int[] nums) {
        // tails[i] = the smallest possible tail of an increasing subsequence of length i+1
        List<Integer> tails = new ArrayList<>();

        for (int num : nums) {
            int idx = Collections.binarySearch(tails, num);
            if (idx < 0) idx = -idx - 1; // if not found, binarySearch returns (-(insertion point) - 1)
            if (idx == tails.size()) {
                tails.add(num); // new longer subsequence
            } else {
                tails.set(idx, num); // replace to keep tails optimal
            }
        }

        return tails.size(); // size of tails = length of LIS
    }

    @Test
    public void testLIS_DP() {
        assertEquals(4, lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
        assertEquals(4, lengthOfLISBinarySearch(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
        assertEquals(4, lengthOfLISCollectionsSort(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
        assertEquals(1, lengthOfLIS(new int[]{3, 3, 3, 3}));
        assertEquals(6, lengthOfLIS(new int[]{1, 3, 6, 7, 9, 4, 10, 5, 6}));
        assertEquals(6, lengthOfLISBinarySearch(new int[]{1, 3, 6, 7, 9, 4, 10, 5, 6}));
    }

}
