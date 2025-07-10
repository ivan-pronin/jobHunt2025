package com.ivan.pronin.job.hunt.middle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class ValidParentheses20 {
    /*
     * 20. Valid Parentheses
     *
     * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
     * A string is valid if:
     * 1. Open brackets must be closed by the same type of brackets.
     * 2. Open brackets must be closed in the correct order.
     * 3. Every close bracket has a corresponding open bracket of the same type.
     * * Example 1:
     * Input: s = "()"
     * Output: true
     * Example 2:
     *
     * Input: s = "()[]{}"
     * Output: true
     * Example 3:
     * Input: s = "(]"
     *
     */

    /*
     * 📊 Сложность:
     * Time: O(n) — один проход по строке.
     * Space: O(n) — стек для хранения открывающих скобок.
     */
    public boolean isValidStack(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> matching = new HashMap<>();
        matching.put(')', '(');
        matching.put(']', '[');
        matching.put('}', '{');

        for (char c : s.toCharArray()) {
            if (matching.containsKey(c)) {
                char top = stack.isEmpty() ? '#' : stack.pop();
                if (top != matching.get(c)) {
                    return false;
                }
            } else {
                stack.push(c);
            }
        }

        return stack.isEmpty();
    }

    /*
     * Альтернативный подход с использованием счетчика:
     * 📊 Сложность:
     * Time: O(n) — один проход по строке.
     * Space: O(1) — фиксированное количество переменных для счетчиков.
     */
    public boolean isValidCounter(String s) {
        int round = 0, square = 0, curly = 0;

        for (char c : s.toCharArray()) {
            switch (c) {
                case '(' -> round++;
                case ')' -> round--;
                case '[' -> square++;
                case ']' -> square--;
                case '{' -> curly++;
                case '}' -> curly--;
            }

            if (round < 0 || square < 0 || curly < 0) {
                return false;
            }
        }

        return round == 0 && square == 0 && curly == 0;

    }

    /*
     * Альтернативный подход с использованием Deque:
     * 📊 Сложность:
     * Time: O(n) — один проход по строке.
     * Space: O(n) — Deque для хранения открывающих скобок.
     */
    public boolean isValidDeque(String s) {
//        Deque<Character> stack = new ArrayDeque<>();
        Stack<Character> stack = new Stack<>();
        int[] intArray = new int[]{1, 2, 3}; // Example array to demonstrate usage of Arrays.stream
        Arrays.stream(intArray).max();
        for (char c : s.toCharArray()) {
            if (c == '(') stack.push(')');
            else if (c == '{') stack.push('}');
            else if (c == '[') stack.push(']');
            else if (stack.isEmpty() || stack.pop() != c) return false;
        }

        return stack.isEmpty();
    }

    @Test
    void testSingleLongValidCase() {
        ValidParentheses20 vp = new ValidParentheses20();
        assertTrue(vp.isValidStack("{{[((()))]}}"));
        assertTrue(vp.isValidDeque("{{[((()))]}}"));
    }

    @Test
    void testValidCases() {
        ValidParentheses20 vp = new ValidParentheses20();
        assertTrue(vp.isValidStack("()"));
        assertTrue(vp.isValidDeque("()"));

        assertTrue(vp.isValidStack("()[]{}"));
        assertTrue(vp.isValidDeque("()[]{}"));

        assertTrue(vp.isValidStack("{[()]}"));
        assertTrue(vp.isValidDeque("{[()]}"));

        assertTrue(vp.isValidStack(""));
        assertTrue(vp.isValidDeque(""));
    }

    @Test
    void testInvalidCases() {
        ValidParentheses20 vp = new ValidParentheses20();
        assertFalse(vp.isValidStack("(]"));
        assertFalse(vp.isValidDeque("(]"));

        assertFalse(vp.isValidStack("([)]"));
        assertFalse(vp.isValidDeque("([)]"));

        assertFalse(vp.isValidStack("("));
        assertFalse(vp.isValidDeque("("));

        assertFalse(vp.isValidStack("]"));
        assertFalse(vp.isValidDeque("]"));

        assertFalse(vp.isValidStack("["));
        assertFalse(vp.isValidDeque("["));

        assertFalse(vp.isValidStack("({[})]"));
        assertFalse(vp.isValidDeque("({[})]"));
    }

    @Test
    void testSingleCharacter() {
        ValidParentheses20 vp = new ValidParentheses20();
        assertFalse(vp.isValidStack("("));
        assertFalse(vp.isValidDeque("("));
        assertFalse(vp.isValidStack(")"));
        assertFalse(vp.isValidDeque(")"));
        assertFalse(vp.isValidStack("["));
        assertFalse(vp.isValidDeque("["));
        assertFalse(vp.isValidStack("]"));
        assertFalse(vp.isValidDeque("]"));
        assertFalse(vp.isValidStack("{"));
        assertFalse(vp.isValidDeque("{"));
        assertFalse(vp.isValidStack("}"));
    }

}
