package com.ivan.pronin.job.hunt.senior.netflix;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 22.07.2025
 */
public class NonOverlappingIntervals435 {

    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0) return 0;

        Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));
        int count = 0;
        int prevEnd = intervals[0][1];

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < prevEnd) {
                // Пересечение – удаляем один
                count++;
            } else {
                // Обновляем end
                prevEnd = intervals[i][1];
            }
        }
        return count;
    }

    @Test
    public void testExamples() {
        assertEquals(1, eraseOverlapIntervals(
            new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 3}}));
        assertEquals(2, eraseOverlapIntervals(
            new int[][]{{1, 2}, {1, 2}, {1, 2}}));
        assertEquals(0, eraseOverlapIntervals(
            new int[][]{{1, 2}, {2, 3}}));
        assertEquals(0, eraseOverlapIntervals(new int[][]{}));
    }

}
