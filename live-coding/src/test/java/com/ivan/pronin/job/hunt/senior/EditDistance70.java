package com.ivan.pronin.job.hunt.senior;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class EditDistance70 {

    /*
     * 70. Edit Distance
     * Given two strings word1 and word2, return the minimum number of operations required to convert word1 to word2.
     * Example:
     * Input: word1 = "horse", word2 = "ros"
     * Output: 3
     * Explanation:
     * horse -> rorse (replace 'h' with 'r')
     * rorse -> rose (remove 'r')
     * rose -> ros (remove 'e')
     *
     * 📊 Сложность:
     *  Time: O(m * n) — где m и n — длины строк word1 и word2 соответственно, так как мы заполняем двумерный массив dp.
     *  Space: O(m * n) — используем двумерный массив для хранения промежуточных результатов.
     * Примечание: можно оптимизировать до O(n) по памяти, используя только одну строку dp.
     */
    public int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация: одна из строк пустая
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // no operation needed
                } else {
                    dp[i][j] = 1 + Math.min(
                        dp[i - 1][j - 1], // replace
                        Math.min(dp[i - 1][j], // delete
                            dp[i][j - 1]) // insert
                    );
                }
            }
        }
        return dp[m][n];
    }

    public int minDistanceSpaceOptimized(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int j = 0; j <= n; j++) prev[j] = j;

        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(
                        prev[j - 1], // replace
                        Math.min(prev[j], // delete
                            curr[j - 1]) // insert
                    );
                }
            }
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return prev[n];
    }

    @Test
    public void testMinDistance_DP() {
        assertEquals(3, minDistance("horse", "ros"));
        assertEquals(5, minDistance("intention", "execution"));
        assertEquals(0, minDistance("same", "same"));
    }

    @Test
    public void testMinDistance_Optimized() {
        assertEquals(3, minDistanceSpaceOptimized("horse", "ros"));
        assertEquals(5, minDistanceSpaceOptimized("intention", "execution"));
        assertEquals(0, minDistanceSpaceOptimized("same", "same"));
    }

}
