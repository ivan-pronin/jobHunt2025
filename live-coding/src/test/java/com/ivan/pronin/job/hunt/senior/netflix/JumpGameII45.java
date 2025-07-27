package com.ivan.pronin.job.hunt.senior.netflix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 22.07.2025
 */
public class JumpGameII45 {

    public static int jump(int[] nums) {
        if (nums.length <= 1) return 0;

        int jumps = 0;
        int currentEnd = 0;
        int farthest = 0;

        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;
            }
        }
        return jumps;
    }

    @Test
    public void testExamples() {
        assertEquals(2, jump(new int[]{2, 3, 1, 1, 4}));
        assertEquals(2, jump(new int[]{2, 3, 0, 1, 4}));
        assertEquals(0, jump(new int[]{0}));
        assertEquals(1, jump(new int[]{1, 2}));
    }

}
