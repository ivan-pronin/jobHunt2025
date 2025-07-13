package com.ivan.pronin.job.hunt.senior;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 13.07.2025
 */
public class CoinChange322 {

    /*
     * 322. Coin Change
     * You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.
     * Return the fewest number of coins that you need to make up that amount.
     * If that amount of money cannot be made up by any combination of the coins, return -1.
     * You may assume that you have an infinite number of each kind of coin.
     * Example:
     * Input: coins = [1, 2, 5], amount = 11
     * Output: 3
     * Explanation: 11 = 5 + 5 + 1
     */
    public int coinChangeBottomUp(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;

        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }

        return dp[amount] > amount ? -1 : dp[amount];
    }

    Map<Integer, Integer> memo = new HashMap<>();

    public int coinChangeTopDown(int[] coins, int amount) {
        if (amount == 0) return 0;
        if (amount < 0) return -1;
        if (memo.containsKey(amount)) return memo.get(amount);

        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            int res = coinChangeTopDown(coins, amount - coin);
            if (res >= 0 && res < min) {
                min = res + 1;
            }
        }

        memo.put(amount, min == Integer.MAX_VALUE ? -1 : min);
        return memo.get(amount);
    }

    @Test
    public void testBottomUp() {
        assertEquals(3, coinChangeBottomUp(new int[]{1, 2, 5}, 11));
        assertEquals(-1, coinChangeBottomUp(new int[]{2}, 3));
        assertEquals(0, coinChangeBottomUp(new int[]{1}, 0));
    }

    @Test
    public void testTopDownMemo() {
        assertEquals(-1, coinChangeTopDown(new int[]{2}, 3));
        assertEquals(3, coinChangeTopDown(new int[]{1, 2, 5}, 11));
        assertEquals(0, coinChangeTopDown(new int[]{1}, 0));
    }

}
