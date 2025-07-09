package com.ivan.pronin.job.hunt.easy;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RemoveDuplicates16 {

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
        RemoveDuplicates16 rd = new RemoveDuplicates16();
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
