package com.ivan.pronin.job.hunt.middle;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class ClimbingStairs70 {

    /*
     * 70. Climbing Stairs
     * You are climbing a staircase. It takes n steps to reach the top.
     * Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
     * Example 1:
     * Input: n = 2
     * Output: 2
     * Explanation: There are two ways to climb to the top:
     * 1. 1 step + 1 step
     * 2. 2 steps
     * Example 2:
     * Input: n = 3
     * Output: 3
     * Explanation: There are three ways to climb to the top:
     * 1. 1 step + 1 step + 1 step
     * 2. 1 step + 2 steps
     * 3. 2 steps + 1 step
     */
    private static final Map<Integer, Integer> memo = new HashMap<>();

    /*
     * 📊 Сложность:
     * Time: O(n) — каждый уровень рекурсии соответствует одному шагу, и мы сохраняем результаты в мапе.
     * Space: O(n) — используем память для хранения результатов в мапе.
     */
    public static int climbStairs(int n) {
        if (n <= 2) return n;
        if (memo.containsKey(n)) return memo.get(n);

        int result = climbStairs(n - 1) + climbStairs(n - 2);
        memo.put(n, result);
        return result;
    }

    /*
     * Итеративный подход (Bottom-Up)
     * 📊 Сложность:
     * Time: O(n) — проходим по всем ступеням от 1 до n.
     * Space: O(1) — используем только несколько переменных для хранения промежуточных результатов.
     */
    public int climbStairsIterativeBottomUp(int n) {
        if (n <= 2) return n;

        int first = 1;
        int second = 2;

        for (int i = 3; i <= n; i++) {
            int third = first + second;
            first = second;
            second = third;
        }

        return second;
    }

    @Test
    void testBaseCases() {
        assertEquals(1, ClimbingStairs70.climbStairs(1));
        assertEquals(2, ClimbingStairs70.climbStairs(2));
    }

    @Test
    void testSmallInputs() {
        assertEquals(3, ClimbingStairs70.climbStairs(3));
        assertEquals(5, ClimbingStairs70.climbStairs(4));
        assertEquals(8, ClimbingStairs70.climbStairs(5));
    }

    @Test
    void testLargerInput() {
        assertEquals(1836311903, ClimbingStairs70.climbStairs(45)); // влезает в int
    }

    @Test
    void testEdgeInput() {
        assertEquals(1, ClimbingStairs70.climbStairs(0 + 1)); // 1 способ добраться до первой ступени
    }

}
