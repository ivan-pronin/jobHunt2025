package com.ivan.pronin.job.hunt.senior.netflix;

import java.util.Arrays;
import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 22.07.2025
 */
public class KClosestPointstoOrigin973 {

    public static int[][] kClosest(int[][] points, int k) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(distance(b), distance(a)));

        for (int[] p : points) {
            maxHeap.offer(p);
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        return result;
    }

    private static int distance(int[] p) {
        return p[0] * p[0] + p[1] * p[1];
    }

    @Test
    public void testExamples() {
        int[][] points = {{1, 3}, {-2, 2}};
        int[][] result = kClosest(points, 1);
        assertEquals(1, result.length);
        assertArrayEquals(new int[]{-2, 2}, result[0]);

        points = new int[][]{{3, 3}, {5, -1}, {-2, 4}};
        int[][] res = kClosest(points, 2);
        // Два ближайших — {-2,4} и {3,3}, но порядок может отличаться
        assertEquals(2, res.length);
        assertTrue(Arrays.deepToString(res).contains("-2"));
    }

}
