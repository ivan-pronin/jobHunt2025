package com.ivan.pronin.job.hunt.middle;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RemoveDuplicates26 {

    /*
     * Given an integer array nums sorted in non-decreasing order, remove the duplicates
     * in-place such that each unique element appears only once.
     * The relative order of the elements should be kept the same.
     * Then return the number of unique elements in nums.
     *
     * Consider the number of unique elements of nums to be k, to get
     * accepted, you need to do the following things:
     *
     * Change the array nums such that the first k elements of nums contain
     * the unique elements in the order they were present in nums initially.
     * The remaining elements of nums are not important as well as the size of nums.
     * Return k.
     */

    //    Так как массив отсортирован, все дубликаты идут подряд. Мы можем использовать два указателя:
//    i — для позиции записи (куда поместить следующий уникальный элемент),
//    j — для прохода по массиву.
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;

        int i = 0; // Указатель на позицию последнего уникального элемента

        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j]; // Сдвигаем уникальный элемент в начало
            }
        }

        return i + 1; // Кол-во уникальных элементов
    }

    @Test
    public void testRemoveDuplicates() {
        RemoveDuplicates26 rd = new RemoveDuplicates26();
        int[] nums = {0, 0, 1, 1, 2, 2, 3, 3, 4};
        int k = rd.removeDuplicates(nums);
        System.out.println(Arrays.toString(nums));
        Assertions.assertEquals(5, k, "Expected 5 unique elements");
        Assertions.assertEquals(0, nums[0]);
        Assertions.assertEquals(1, nums[1]);
        Assertions.assertEquals(2, nums[2]);
        Assertions.assertEquals(3, nums[3]);
        Assertions.assertEquals(4, nums[4]);

    }

}
