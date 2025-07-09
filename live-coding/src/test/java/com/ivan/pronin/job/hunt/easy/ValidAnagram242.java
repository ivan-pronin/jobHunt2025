package com.ivan.pronin.job.hunt.easy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class ValidAnagram242 {
    /*
     * 242. Valid Anagram
     * Given two strings s and t, return true if t is an anagram of s, and false otherwise.
     *
     * An anagram is a word or phrase formed by rearranging the letters of a
     * different word or phrase, using all the original letters exactly once.
     */

    /*
     * 📊 Сложность:
     * Time: O(n) — один проход по строкам для подсчета символов.
     * Space: O(1) — фиксированный размер массива для английского алфавита (26 букв).
     *
     */
    public boolean isAnagramFixedDictionary(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] counts = new int[26]; // английский алфавит

        for (int i = 0; i < s.length(); i++) {
            counts[s.charAt(i) - 'a']++;
            counts[t.charAt(i) - 'a']--;
        }

        for (int count : counts) {
            if (count != 0) return false;
        }

        return true;
    }

    /*
     * Альтернативный подход с использованием сортировки:
     * 📊 Сложность:
     * Time: O(n log n) — из-за сортировки.
     * Space: O(n) — для хранения отсортированных массивов символов.
     */
    public boolean isAnagramSorted(String s, String t) {
        if (s.length() != t.length()) return false;

        char[] sArr = s.toCharArray();
        char[] tArr = t.toCharArray();

        Arrays.sort(sArr);
        Arrays.sort(tArr);

        return Arrays.equals(sArr, tArr);
    }

    /*
     * Альтернативный подход с использованием HashMap:
     * 📊 Сложность:
     * Time: O(n) — один проход по строкам для подсчета символов.
     * Space: O(n) — для хранения символов и их частот.
     */
    public boolean isAnagramHashMap(String s, String t) {
        if (s.length() != t.length()) return false;

        Map<Character, Integer> countMap = new HashMap<>();

        for (char c : s.toCharArray()) {
            countMap.put(c, countMap.getOrDefault(c, 0) + 1);
        }

        for (char c : t.toCharArray()) {
            if (!countMap.containsKey(c)) return false;

            countMap.put(c, countMap.get(c) - 1);
            if (countMap.get(c) < 0) return false;
        }

        return true;
    }

    @Test
    void testAnagrams() {
        ValidAnagram242 va = new ValidAnagram242();
        assertTrue(va.isAnagramFixedDictionary("listen", "silent"));
        assertTrue(va.isAnagramFixedDictionary("anagram", "nagaram"));
        assertTrue(va.isAnagramFixedDictionary("a", "a"));
        assertTrue(va.isAnagramFixedDictionary("", ""));
    }

    @Test
    void testNotAnagrams() {
        ValidAnagram242 va = new ValidAnagram242();
        assertFalse(va.isAnagramFixedDictionary("rat", "car"));
        assertFalse(va.isAnagramFixedDictionary("hello", "helloo"));
        assertFalse(va.isAnagramFixedDictionary("abc", "abd"));
        assertFalse(va.isAnagramFixedDictionary("a", "b"));
    }

    @Test
    void testCaseSensitivity() {
        ValidAnagram242 va = new ValidAnagram242();
        assertFalse(va.isAnagramFixedDictionary("aA", "Aa"));
    }

    @Test
    void testUnicodeCharacters() {
        ValidAnagram242 va = new ValidAnagram242();
        // Cyrillic letters, should return false (out of 'a'-'z' range)
        assertFalse(va.isAnagramFixedDictionary("кот", "ток"));
        // Emoji, should return false
        assertFalse(va.isAnagramFixedDictionary("😀😁", "😁😀"));
        // Mixed Latin and Unicode, should return false
        assertFalse(va.isAnagramFixedDictionary("a😊", "😊a"));
    }

}
