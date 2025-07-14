package com.ivan.pronin.job.hunt.senior.model;

/**
 * @author Ivan Pronin
 * @since 14.07.2025
 */
public class TrieNode {

    public TrieNode[] children = new TrieNode[26]; // только символы 'a'–'z'

    public boolean isEndOfWord = false;

}
