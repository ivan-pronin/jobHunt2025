package com.ivan.pronin.job.hunt.senior;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class MinAddToMakeValidStack921 {

    /**
     * 921. Minimum Add to Make Parentheses Valid
     * Given a string s of '(' and ')', return the minimum number of parentheses we must add to make the resulting parentheses string valid.
     * Example:
     * Input: s = "())"
     * Output: 1
     *
     * 📊 Сложность:
     * Time: O(n) — проходим по строке один раз.
     * Space: O(1) — используем только несколько переменных для подсчета.
     */
    public int minAddToMakeValid(String s) {
        int open = 0, close = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') {
                open++;
            } else if (c == ')') {
                if (open > 0) {
                    open--;
                } else {
                    close++;
                }
            }
        }

        return open + close;
    }

   @Test
    public void testValidParentheses() {
        assertEquals(1, minAddToMakeValid("())"));
        assertEquals(3, minAddToMakeValid("((("));
        assertEquals(0, minAddToMakeValid("()"));
        assertEquals(4, minAddToMakeValid("()))(("));
    }

}
