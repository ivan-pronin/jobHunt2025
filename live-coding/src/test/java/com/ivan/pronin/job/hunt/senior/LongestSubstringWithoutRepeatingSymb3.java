package com.ivan.pronin.job.hunt.senior;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class LongestSubstringWithoutRepeatingSymb3 {

    /*
     * 3. Longest Substring Without Repeating Characters
     * Given a string s, find the length of the longest substring without repeating characters.
     * Example 1:
     * Input: s = "abcabcbb"
     * Output: 3
     * Explanation: The answer is "abc", with the length of 3.
     * Example 2:
     * Input: s = "bbbbb"
     * Output: 1
     * Explanation: The answer is "b", with the length of 1.

     * 📊 Сложность:
     * Time: O(n) — каждый символ строки посещается один раз.
     * Space: O(min(n, m)) — используем хэш-таблицу для хранения уникальных символов, где n — длина строки, m — размер алфавита.
     */
    public static int lengthOfLongestSubstring(String s) {
        Set<Character> seen = new HashSet<>();
        int left = 0, maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            while (seen.contains(s.charAt(right))) {
                seen.remove(s.charAt(left));
                left++;
            }
            seen.add(s.charAt(right));
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }

    public int lengthOfLongestSubstringHashMap(String s) {
        Map<Character, Integer> indexMap = new HashMap<>();
        int left = 0, maxLen = 0;
        //abba {a,0}, {b,1} b->
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (indexMap.containsKey(c) && indexMap.get(c) >= left) {
                left = indexMap.get(c) + 1;
            }
            indexMap.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }

    @Test
    public void testLongestSubstringWithoutRepeatingSymb3utionV1() {
        assertEquals(3, LongestSubstringWithoutRepeatingSymb3.lengthOfLongestSubstring("abcabcbb"));
        assertEquals(1, LongestSubstringWithoutRepeatingSymb3.lengthOfLongestSubstring("bbbbb"));
        assertEquals(3, LongestSubstringWithoutRepeatingSymb3.lengthOfLongestSubstring("pwwkew"));
        assertEquals(0, LongestSubstringWithoutRepeatingSymb3.lengthOfLongestSubstring(""));
        assertEquals(1, LongestSubstringWithoutRepeatingSymb3.lengthOfLongestSubstring(" "));
        assertEquals(2, LongestSubstringWithoutRepeatingSymb3.lengthOfLongestSubstring("abba"));
    }

}
