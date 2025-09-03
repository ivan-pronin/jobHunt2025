package com.ivan.pronin.job.hunt.senior.google;

import java.util.Stack;

/**
 * @author Ivan Pronin
 * @since 03.09.2025
 */
public class LongestValidParenthesis {

    public static int getMaxLength(String input) {
        if (null == input || input.length() < 2) return 0;
        int result = 0;
        Stack<Character> stack = new Stack<>();
        for (char c : input.toCharArray()) {
            Character last = null;
            if (!stack.isEmpty()) last = stack.peek();
            stack.push(c);
            if (last != null && (last + stack.peek() == '(' + ')')) {
                result = result + 2;
                stack.pop();
                stack.pop();
            }
        }
        return result;
    }

    public static String getLongestString(String input) {
        if (null == input || input.length() < 2) return "";
        Stack<Integer> indexes = new Stack<>();
        indexes.push(-1);
        int maxLen = 0;
        int start = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                indexes.push(i);
            } else {
                indexes.pop();
                if (indexes.isEmpty()) {
                    indexes.push(i);
                } else {
                    int length = i - indexes.peek();
                    if (length > maxLen) {
                        maxLen = length;
                        start = indexes.peek() + 1;
                    }
                }
            }
        }

        return input.substring(start, start + maxLen);
    }

}
