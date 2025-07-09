package com.ivan.pronin.job.hunt.easy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class MoveZeroesSolution283 {

    // (two-pointer, optimal O(n))
    public void moveZeroes(int[] nums) {
        int insertPos = 0; // позиция, куда ставим следующий ненулевой элемент

        // Первый проход: копируем все ненулевые элементы вперед
        for (int num : nums) {
            if (num != 0) {
                nums[insertPos++] = num;
            }
        }

        // Второй проход: заполняем оставшиеся позиции нулями
        while (insertPos < nums.length) {
            nums[insertPos++] = 0;
        }
    }

//    Альтернативный (swap-based) подход:
    public void moveZeroesAlt(int[] nums) {
        int left = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] != 0) {
                // Поменять местами ноль и ненулевое значение
                int temp = nums[left];
                nums[left] = nums[right];
                nums[right] = temp;
                left++;
            }
        }
    }

    @Test
    void testMoveZeroes_Mixed() {
        MoveZeroesSolution283 solution = new MoveZeroesSolution283();
        int[] nums = {0, 1, 0, 3, 12};
        solution.moveZeroes(nums);
        Assertions.assertArrayEquals(new int[]{1, 3, 12, 0, 0}, nums);
    }

    @Test
    void testMoveZeroes_AllZeros() {
        MoveZeroesSolution283 solution = new MoveZeroesSolution283();
        int[] nums = {0, 0, 0};
        solution.moveZeroes(nums);
        Assertions.assertArrayEquals(new int[]{0, 0, 0}, nums);
    }

    @Test
    void testMoveZeroes_NoZeros() {
        MoveZeroesSolution283 solution = new MoveZeroesSolution283();
        int[] nums = {1, 2, 3};
        solution.moveZeroes(nums);
        Assertions.assertArrayEquals(new int[]{1, 2, 3}, nums);
    }

    @Test
    void testMoveZeroes_Empty() {
        MoveZeroesSolution283 solution = new MoveZeroesSolution283();
        int[] nums = {};
        solution.moveZeroes(nums);
        Assertions.assertArrayEquals(new int[]{}, nums);
    }

    @Test
    void testMoveZeroes_SingleZero() {
        MoveZeroesSolution283 solution = new MoveZeroesSolution283();
        int[] nums = {0};
        solution.moveZeroes(nums);
        Assertions.assertArrayEquals(new int[]{0}, nums);
    }

    @Test
    void testMoveZeroes_SingleNonZero() {
        MoveZeroesSolution283 solution = new MoveZeroesSolution283();
        int[] nums = {5};
        solution.moveZeroes(nums);
        Assertions.assertArrayEquals(new int[]{5}, nums);
    }

}
