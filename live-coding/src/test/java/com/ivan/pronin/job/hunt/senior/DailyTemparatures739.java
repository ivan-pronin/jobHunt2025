package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 12.07.2025
 */
public class DailyTemparatures739 {

    /*
     * 739. Daily Temperatures
     * Given an array of integers temperatures represents the daily temperatures, return an array answer such that answer[i] is the number of days you have to wait after the ith day to get a warmer temperature.
     * If there is no future day for which this is possible, keep answer[i] == 0 instead.
     * Example:
     * Input: temperatures = [73,74,75,71,69,72,76,73]
     * Output: [1,1,4,2,1,1,0,0]
     *
     * 📊 Сложность:
     * Time: O(n) — проходим по массиву один раз.
     * Space: O(n) — используем стек для хранения индексов.
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] result = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prevIndex = stack.pop();
                result[prevIndex] = i - prevIndex;
            }
            stack.push(i);
        }

        return result;
    }

    public int[] dailyTemperatures2(int[] temperatures) {
        int n = temperatures.length;
        int[] result = new int[n];

        for (int i = n - 2; i >= 0; i--) {
            int j = i + 1;
            while (j < n && temperatures[i] >= temperatures[j]) {
                if (result[j] == 0) {
                    j = n; // нет потепления
                } else {
                    j += result[j]; // прыжок вперед
                }
            }
            if (j < n) {
                result[i] = j - i;
            }
        }

        return result;
    }

    @Test
    public void testExample1() {
//        assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
        assertArrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, dailyTemperatures2(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
    }

    @Test
    public void testAllSame() {
        assertArrayEquals(new int[]{0, 0, 0, 0}, dailyTemperatures(new int[]{70, 70, 70, 70}));
        assertArrayEquals(new int[]{0, 0, 0, 0}, dailyTemperatures2(new int[]{70, 70, 70, 70}));
    }

    @Test
    public void testDecreasing() {
        assertArrayEquals(new int[]{0, 0, 0, 0}, dailyTemperatures(new int[]{90, 85, 80, 75}));
        assertArrayEquals(new int[]{0, 0, 0, 0}, dailyTemperatures2(new int[]{90, 85, 80, 75}));
    }

}
