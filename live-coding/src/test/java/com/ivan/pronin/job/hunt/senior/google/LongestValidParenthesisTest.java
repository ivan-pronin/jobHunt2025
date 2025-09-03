package com.ivan.pronin.job.hunt.senior.google;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 03.09.2025
 */
class LongestValidParenthesisTest {

    @Test
    void testGetLongestString() {
        assertEquals("", LongestValidParenthesis.getLongestString(")"));
        assertEquals("", LongestValidParenthesis.getLongestString("("));
        assertEquals("()", LongestValidParenthesis.getLongestString("(()"));
        assertEquals("(())", LongestValidParenthesis.getLongestString("(()))"));
        assertEquals("()()", LongestValidParenthesis.getLongestString("((()()("));
        assertEquals("()(())", LongestValidParenthesis.getLongestString("(()(())"));
    }

    @Test
    void testGetMaxLength() {
        assertEquals(0, LongestValidParenthesis.getMaxLength(")"));
        assertEquals(0, LongestValidParenthesis.getMaxLength("("));
        assertEquals(2, LongestValidParenthesis.getMaxLength("(()"));
        assertEquals(4, LongestValidParenthesis.getMaxLength("(()))"));
        assertEquals(4, LongestValidParenthesis.getMaxLength("((()()("));
        assertEquals(6, LongestValidParenthesis.getMaxLength("(()(())"));
        assertEquals(6, LongestValidParenthesis.getMaxLength("()(())"));
        assertEquals(8, LongestValidParenthesis.getMaxLength("(()(()))"));

    }

}