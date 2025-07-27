package com.ivan.pronin.job.hunt.senior.netflix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 21.07.2025
 */
public class FirstMissingPositive41 {

    /*
     * Given an unsorted integer array, find the smallest missing positive integer.
     * Example:
     * Input: [3, 4, -1, 1]
     * Output: 2
     *
     */
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;

        // Пройдем по всем элементам массива
        for (int i = 0; i < n; i++) {
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                // Меняем местами числа, если они не на своём месте
                int temp = nums[i];
                nums[i] = nums[nums[i] - 1];
                nums[temp - 1] = temp;
            }
        }

        // Найдем первый индекс, на котором нет правильного числа
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        return n + 1; // Если все числа на месте, то отсутствует n+1
    }

    @Test
    public void testFirstMissingPositive() {
//        assertEquals(3, firstMissingPositive(new int[]{1, 2, 0}));
        assertEquals(2, firstMissingPositive(new int[]{3, 4, -1, 1}));
        assertEquals(1, firstMissingPositive(new int[]{7, 8, 9, 11, 12}));
        assertEquals(4, firstMissingPositive(new int[]{1, 2, 3}));
        assertEquals(1, firstMissingPositive(new int[]{-1, -2, -3}));
    }

}
