package com.ivan.pronin.job.hunt.senior.netflix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 22.07.2025
 */
public class LongestPalindromicSubstring5 {

    /**
        * Самое простое решение: проверяем все возможные подстроки.
        * Легко понять, но очень медленное O(n³)
     */
    public static class BruteForce {

        public String longestPalindrome(String s) {
            if (s == null || s.length() < 2) return s;

            String longest = "";

            // Перебираем все возможные подстроки
            for (int i = 0; i < s.length(); i++) {
                for (int j = i; j < s.length(); j++) {
                    String substring = s.substring(i, j + 1);

                    // Если подстрока - палиндром и длиннее текущего максимума
                    if (isPalindrome(substring) && substring.length() > longest.length()) {
                        longest = substring;
                    }
                }
            }

            return longest;
        }

        private boolean isPalindrome(String str) {
            int left = 0, right = str.length() - 1;

            while (left < right) {
                if (str.charAt(left) != str.charAt(right)) {
                    return false;
                }
                left++;
                right--;
            }

            return true;
        }
    }

    // ========================================
    // РЕШЕНИЕ 2: Expand Around Centers
    // ========================================

    /**
     * Классическое эффективное решение: расширяем от центров палиндромов.
     * O(n²) время, O(1) дополнительная память. Самое популярное решение.
     */
    public static class ExpandAroundCenter {

        public String longestPalindrome(String s) {
            if (s == null || s.length() < 2) return s;

            int start = 0, maxLength = 1;

            for (int i = 0; i < s.length(); i++) {
                // Проверяем палиндромы с нечетной длиной (центр в i)
                int len1 = expandAroundCenter(s, i, i);

                // Проверяем палиндромы с четной длиной (центр между i и i+1)
                int len2 = expandAroundCenter(s, i, i + 1);

                int currentMax = Math.max(len1, len2);

                if (currentMax > maxLength) {
                    maxLength = currentMax;
                    // Вычисляем начальный индекс палиндрома
                    start = i - (currentMax - 1) / 2;
                }
            }

            return s.substring(start, start + maxLength);
        }

        /**
         * Расширяем палиндром от центра и возвращаем его длину
         */
        private int expandAroundCenter(String s, int left, int right) {
            while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
                left--;
                right++;
            }

            // Возвращаем длину палиндрома
            return right - left - 1;
        }
    }

    @Test
    public void testAllSolutions() {
        // Создаем экземпляры всех решений
        BruteForce brute = new BruteForce();
        ExpandAroundCenter expand = new ExpandAroundCenter();

        // Тест 1: Классический пример
        String s1 = "babad";
        String result1 = brute.longestPalindrome(s1);
        assertTrue(result1.equals("bab") || result1.equals("aba"));
        assertEquals(result1, expand.longestPalindrome(s1));

        // Тест 2: Четный палиндром
        String s2 = "cbbd";
        String expected2 = "bb";
        assertEquals(expected2, brute.longestPalindrome(s2));
        assertEquals(expected2, expand.longestPalindrome(s2));

        // Тест 3: Один символ
        String s3 = "a";
        assertEquals("a", brute.longestPalindrome(s3));
        assertEquals("a", expand.longestPalindrome(s3));

        // Тест 4: Вся строка - палиндром
        String s4 = "racecar";
        assertEquals("racecar", brute.longestPalindrome(s4));
        assertEquals("racecar", expand.longestPalindrome(s4));

        // Тест 5: Нет палиндромов длины > 1
        String s5 = "abcdef";
        assertEquals(1, brute.longestPalindrome(s5).length());
        assertEquals(1, expand.longestPalindrome(s5).length());

        // Тест 6: Пустая строка и null
        assertEquals("", expand.longestPalindrome(""));
        assertNull(expand.longestPalindrome(null));

        System.out.println("Все тесты прошли успешно!");
    }

    // Дополнительный тест с более сложными случаями
    @Test
    public void testComplexCases() {
        ExpandAroundCenter expand = new ExpandAroundCenter();

        // Длинный палиндром в середине
        String s1 = "abcdefghijklmnopqrstuvwxyzabcdcbafedcba";
        String result1 = expand.longestPalindrome(s1);
        assertTrue(result1.length() >= 7); // abcdcba или другой палиндром

        // Несколько палиндромов одинаковой длины
        String s2 = "abacabad";
        String result2 = expand.longestPalindrome(s2);
        assertTrue(result2.equals("abacaba"));

        System.out.println("Сложные тесты прошли успешно!");
    }

}
