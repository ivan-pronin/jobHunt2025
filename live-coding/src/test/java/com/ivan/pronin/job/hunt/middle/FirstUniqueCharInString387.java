package com.ivan.pronin.job.hunt.middle;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class FirstUniqueCharInString387 {

    /*
     * 387. First Unique Character in a String
     * Given a string s, find the first non-repeating character in it and return its index. If it does not exist, return -1.
     * Example 1:
     * Input: s = "leetcode"
     * Output: 0
     * Example 2:
     * Input: s = "loveleetcode"
     * Output: 2
     * Example 3:
     * Input: s = "aabb"
     * Output: -1
     * 📊 Сложность:
     * Time: O(n) — проходим по строке дважды: один раз для подсчета символов, второй раз для поиска первого уникального.
     * Space: O(1) — используем фиксированный массив для хранения количества вхождений символов (26 букв английского алфавита).
     *
     */
    public static int firstUniqChar(String s) {
        int[] charCount = new int[26]; // для букв a-z

        // Считаем количество вхождений каждого символа
        for (char c : s.toCharArray()) {
            charCount[c - 'a']++;
        }

        // Находим первый уникальный символ
        for (int i = 0; i < s.length(); i++) {
            if (charCount[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }

        return -1; // если нет уникальных символов
    }

    public static int firstUniqCharWithHashing(String s) {
        Map<Character, Integer> map = new LinkedHashMap<>();

        for (char c : s.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < s.length(); i++) {
            if (map.get(s.charAt(i)) == 1) {
                return i;
            }
        }

        return -1;
    }

    @Test
    void testExample1() {
        assertEquals(0, FirstUniqueCharInString387.firstUniqChar("leetcode"));
        assertEquals(0, FirstUniqueCharInString387.firstUniqCharWithHashing("leetcode"));
    }

    @Test
    void testExample2() {
        assertEquals(2, FirstUniqueCharInString387.firstUniqChar("loveleetcode"));
        assertEquals(2, FirstUniqueCharInString387.firstUniqCharWithHashing("loveleetcode"));
    }

    @Test
    void testExample3() {
        assertEquals(-1, FirstUniqueCharInString387.firstUniqChar("aabbcc"));
        assertEquals(-1, FirstUniqueCharInString387.firstUniqCharWithHashing("aabbcc"));
    }

    @Test
    void testSingleCharacter() {
        assertEquals(0, FirstUniqueCharInString387.firstUniqChar("z"));
        assertEquals(0, FirstUniqueCharInString387.firstUniqCharWithHashing("z"));
    }

    @Test
    void testEmptyString() {
        assertEquals(-1, FirstUniqueCharInString387.firstUniqChar(""));
        assertEquals(-1, FirstUniqueCharInString387.firstUniqCharWithHashing(""));
    }

    @Test
    void testUnicodeCharacters() {
        assertEquals(2, FirstUniqueCharInString387.firstUniqCharWithHashing("我我爱你你"));
    }

}
