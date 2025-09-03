package com.ivan.pronin.job.hunt.senior.google;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 03.09.2025
 */
class GenerateParenthesisTest {

    @Test
    public void testGetValidParenthesis1() {
        var expected = List.of("()");
        var actual = GenerateParenthesis.generate(1);
        assertListsEqual(expected, actual);
    }

    @Test
    public void testGetValidParenthesis2() {
        var expected = List.of("(())", "()()");
        var actual = GenerateParenthesis.generate(2);
        assertListsEqual(expected, actual);
    }

    @Test
    public void testGetValidParenthesis3() {
        var expected = List.of("((()))", "(()())", "(())()", "()(())", "()()()");
        var actual = GenerateParenthesis.generate(3);
        assertListsEqual(expected, actual);
    }

    private static void assertListsEqual(List<String> expected, List<String> actual) {
        assertEquals(expected.size(), actual.size());
        expected.forEach(exp -> Assertions.assertTrue(actual.contains(exp)));
    }

}