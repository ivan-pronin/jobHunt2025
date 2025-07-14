package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ivan.pronin.job.hunt.senior.model.TrieNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 14.07.2025
 */
public class WordDictionary211 {

    /*
     * 211. Design Add and Search Words Data Structure
     * Design a data structure that supports adding new words and finding if a string matches any previously added string.
     * Implement the WordDictionary class:
     * - WordDictionary() initializes the object.
     * - void addWord(String word) adds word to the data structure, it can be matched later.
     * - boolean search(String word) returns true if there is any string in the data structure that matches word or false otherwise.
     * word may contain dots '.' where dots can be matched with any letter.
     */
    // 📊 Сложность:
    // Time: O(m) — добавление слова и поиск слова выполняются за линейное время относительно длины слова m.
    // Space: O(n) — используем память для хранения n слов, где n — количество добавленных слов.
    private static class TrieNodeSolution {

        private final TrieNode root;

        public TrieNodeSolution() {
            root = new TrieNode();
        }

        public void addWord(String word) {
            TrieNode node = root;
            for (char ch : word.toCharArray()) {
                int idx = ch - 'a';
                if (node.children[idx] == null)
                    node.children[idx] = new TrieNode();
                node = node.children[idx];
            }
            node.isEndOfWord = true;
        }

        public boolean search(String word) {
            return dfs(word.toCharArray(), 0, root);
        }

        private boolean dfs(char[] word, int i, TrieNode node) {
            if (node == null) return false;
            if (i == word.length) return node.isEndOfWord;

            char ch = word[i];
            if (ch == '.') {
                for (TrieNode child : node.children) {
                    if (child != null && dfs(word, i + 1, child))
                        return true;
                }
                return false;
            } else {
                return dfs(word, i + 1, node.children[ch - 'a']);
            }
        }

    }

    @Test
    public void testWordDictionary() {
        TrieNodeSolution wordDictionary = new TrieNodeSolution();
        wordDictionary.addWord("bad");
        wordDictionary.addWord("dad");
        wordDictionary.addWord("mad");

        assertFalse(wordDictionary.search("pad"));
        assertFalse(wordDictionary.search("mom"));
        assertTrue(wordDictionary.search("bad"));
        assertTrue(wordDictionary.search(".ad"));
        assertTrue(wordDictionary.search("b.."));
        assertTrue(wordDictionary.search("b.d"));
    }

    /*
     * Сложность
     * Time: O(n * m) — добавление слова выполняется за O(1), где m — длина слова, и поиск слова выполняется за O(n * m), где n — количество слов.
     * Space: O(n) — используем память для хранения n слов, где n — количество добавленных слов.
     */
    private static class HashMapSolution {

        // This solution uses a HashMap to store words and supports search with wildcards.
        // It is less efficient than the TrieNodeSolution but demonstrates an alternative approach.
        private final Map<Integer, List<String>> wordsByLength = new HashMap<>();

        public void addWord(String word) {
            wordsByLength.computeIfAbsent(word.length(), k -> new ArrayList<>()).add(word);
        }

        public boolean search(String word) {
            if (!wordsByLength.containsKey(word.length()))
                return false;

            for (String candidate : wordsByLength.get(word.length())) {
                if (isMatch(candidate, word))
                    return true;
            }
            return false;
        }

        private boolean isMatch(String word, String pattern) {
            for (int i = 0; i < word.length(); i++) {
                if (pattern.charAt(i) != '.' && word.charAt(i) != pattern.charAt(i))
                    return false;
            }
            return true;
        }

    }

    @Test
    public void testWordDictionaryMap() {
        var wordDictionary = new HashMapSolution();
        wordDictionary.addWord("bad");
        wordDictionary.addWord("dad");
        wordDictionary.addWord("mad");

        assertFalse(wordDictionary.search("pad"));
        assertFalse(wordDictionary.search("mom"));
        assertTrue(wordDictionary.search("bad"));
        assertTrue(wordDictionary.search(".ad"));
        assertTrue(wordDictionary.search("b.."));
        assertTrue(wordDictionary.search("b.d"));
    }

}
