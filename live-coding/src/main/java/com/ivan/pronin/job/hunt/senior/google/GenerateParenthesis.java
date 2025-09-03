package com.ivan.pronin.job.hunt.senior.google;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Pronin
 * @since 03.09.2025
 * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
 *
 * Example 1:
 * Input: n = 3
 * Output: ["((()))","(()())","(())()","()(())","()()()"]
 *
 * Example 2:
 * Input: n = 1
 * Output: ["()"]
 *
 * 1 <= n <=8
 */
public class GenerateParenthesis {

    public static List<String> generate(int n) {
        List<String> result = new ArrayList<>();
        dfs(0, 0, "", n, result);

        return result;
    }

    private static void dfs(int left, int right, String current, int n, List<String> result) {
        if (left > n || right > n || right > left) {
            return;
        }
        if (left == n && right == n) {
            result.add(String.valueOf(current));
        }
        dfs(left + 1, right, current + "(", n, result);
        dfs(left, right + 1, current + ")", n, result);
    }

}
