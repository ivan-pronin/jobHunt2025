package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class GroupAnagrams49 {

    /*
     * 49. Group Anagrams
     * Given an array of strings strs, group the anagrams together.
     * Example:
     * Input: strs = ["eat","tea","tan","ate","nat","bat"]
     * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
     *
     * 📊 Сложность:
     * Time: O(n * k log k) — где n — количество строк, k — средняя длина строки. Сортировка каждой строки занимает O(k log k).
     * Space: O(n * k) — используем хэш-таблицу для хранения групп, где n — количество строк, k — средняя длина строки.
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            char[] arr = s.toCharArray();
            Arrays.sort(arr);
            // k strings , so sorting takes O(k log k)
            String key = new String(arr);

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values());
    }

    /*
     * Альтернативный подход с использованием регистра частоты символов
     * 📊 Сложность:
     * Time: O(n * k) — где n — количество строк, k — длина строки. Считаем частоту символов за O(k).
     * Space: O(n * 26) — используем хэш-таблицу для хранения групп, где n — количество строк, 26 — количество букв в английском алфавите.
     */
    public List<List<String>> groupAnagramsWithRegistry(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            int[] counts = new int[26];
            for (char c : s.toCharArray()) {
                counts[c - 'a']++;
            }

            // создаем уникальный ключ: например, "1#0#0#2#..."
            StringBuilder sb = new StringBuilder();
            for (int count : counts) {
                sb.append(count).append('#');
            }
            String key = sb.toString();

            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        return new ArrayList<>(map.values());
    }

    private List<List<String>> normalize(List<List<String>> input) {
        for (List<String> group : input) {
            Collections.sort(group);
        }
        input.sort(Comparator.comparing(a -> a.getFirst()));
        return input;
    }

    @Test
    public void testSortKey() {
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> result = groupAnagrams(input);

        List<List<String>> expected = Arrays.asList(
            Arrays.asList("ate", "eat", "tea"),
            Arrays.asList("nat", "tan"),
            Arrays.asList("bat")
        );

        assertEquals(normalize(expected), normalize(result));
    }

    @Test
    public void testCountKey() {
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> result = groupAnagramsWithRegistry(input);

        List<List<String>> expected = Arrays.asList(
            Arrays.asList("ate", "eat", "tea"),
            Arrays.asList("nat", "tan"),
            Arrays.asList("bat")
        );

        assertEquals(normalize(expected), normalize(result));
    }

}
