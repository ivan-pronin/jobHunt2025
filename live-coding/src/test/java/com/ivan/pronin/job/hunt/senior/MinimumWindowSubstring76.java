package com.ivan.pronin.job.hunt.senior;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 13.07.2025
 */
public class MinimumWindowSubstring76 {

    /*
     * 76. Minimum Window Substring
     * Given two strings s and t of lengths m and n respectively, return the minimum window substring
     * of s such that every character in t (including duplicates) is included in the window.
     * If there is no such substring, return the empty string "".
     * The test cases will be generated such that the answer is unique.
     *
     * Example:
     * Input: s = "ADOBECODEBANC", t = "ABC"
     * Output: "BANC"
     *
     * 📊 Сложность:
     * Time: O(n + m) — каждый символ строки s и t посещается один раз.
     * Space: O(m) — используем массив для хранения символов из t, где m — длина строки t.
     */
    public String minWindow(String s, String t) {
        int[] need = new int[128];
        for (char c : t.toCharArray()) need[c]++;

        int left = 0, count = 0, start = 0, minLen = Integer.MAX_VALUE;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (--need[c] >= 0) count++;

            while (count == t.length()) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    start = left;
                }

                if (++need[s.charAt(left)] > 0) count--;
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    @Test
    public void testArrayOptimized() {
        assertEquals("BANC", minWindow("ADOBECODEBANC", "ABC"));
        assertEquals("", minWindow("A", "AA"));
        assertEquals("a", minWindow("a", "a"));
    }

    @Test
    public void testEmpty() {
        assertEquals("", minWindow("", ""));
        assertEquals("", minWindow("ABC", ""));
        assertEquals("", minWindow("", "A"));
    }

}
