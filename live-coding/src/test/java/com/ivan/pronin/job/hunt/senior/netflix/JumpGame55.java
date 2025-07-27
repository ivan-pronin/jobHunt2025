package com.ivan.pronin.job.hunt.senior.netflix;

/**
 * @author Ivan Pronin
 * @since 22.07.2025
 */
public class JumpGame55 {

    /*
     * 1. LeetCode 55. Jump Game
     * Условие:
     * Дан массив nums, где nums[i] — максимальная длина прыжка из позиции i.
     * Начинаем с позиции 0. Нужно определить, можно ли добраться до последнего индекса.
     */
    public static boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false;
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        return true;
    }

}
