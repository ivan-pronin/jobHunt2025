package com.ivan.pronin.job.hunt.senior.netflix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 22.07.2025
 */
public class MergeIntervals56 {

    public static int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length == 0) return new int[0][0];

        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> result = new ArrayList<>();

        int[] current = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= current[1]) {
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                result.add(current);
                current = intervals[i];
            }
        }
        result.add(current);

        return result.toArray(new int[result.size()][]);
    }

    private boolean arraysEqual(int[][] a, int[][] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i][0] != b[i][0] || a[i][1] != b[i][1]) return false;
        }
        return true;
    }

    @Test
    public void testExamples() {
        assertTrue(arraysEqual(
            merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}}),
            new int[][]{{1, 6}, {8, 10}, {15, 18}}
        ));
        assertTrue(arraysEqual(
            merge(new int[][]{{1, 4}, {4, 5}}),
            new int[][]{{1, 5}}
        ));
        assertTrue(arraysEqual(
            merge(new int[][]{{1, 4}}),
            new int[][]{{1, 4}}
        ));
    }

    @Test
    public void testExamples2() {
        int[][] arr = new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        System.out.println(Arrays.toString(arr[0]));
    }
}
