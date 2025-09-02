package com.ivan.pronin.job.hunt.senior.google;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 02.09.2025
 */
class DecompressStringTest {

    @Test
    public void testSeveralSingleTokensRecursive() {
        var input = "3[abc]4[ab]c";
        var expectedOut = "abcabcabcababababc";
        assertEquals(expectedOut, DecompressString.decompressRecursive(input));
    }

    @Test
    public void testSingleTokenRecursive() {
        assertEquals("aaaaaaaaaa", DecompressString.decompressRecursive("10[a]"));
    }

    @Test
    public void testNestedTokenRecursive() {
        assertEquals("aaabaaab", DecompressString.decompressRecursive("2[3[a]b]"));
    }

    @Test
    public void testSeveralSingleTokensIterative() {
        var input = "3[abc]4[ab]c";
        var expectedOut = "abcabcabcababababc";
        assertEquals(expectedOut, DecompressString.decompressIterative(input));
    }

    @Test
    public void testSingleTokenIterative() {
        assertEquals("aaaaaaaaaa", DecompressString.decompressIterative("10[a]"));
    }

    @Test
    public void testNestedTokenIterative() {
        assertEquals("aaabaaab", DecompressString.decompressIterative("2[3[a]b]"));
    }

}