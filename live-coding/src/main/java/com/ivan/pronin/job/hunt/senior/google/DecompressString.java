package com.ivan.pronin.job.hunt.senior.google;

import java.util.Stack;

/**
 * @author Ivan Pronin
 * @since 02.09.2025
 */
public class DecompressString {

    //  var input = "3[abc]4[ab]c";
    //  var expectedOut = "abcabcabcababababc";
    public static String decompressRecursive(String input) {
        // handle edge cases like empty / null / not compressed strings

        return tokenise(input, new int[]{0});
    }

    public static String decompressIterative(String input) {
        Stack<Integer> count = new Stack<>();
        Stack<StringBuilder> tokens = new Stack<>();
        StringBuilder current = new StringBuilder();
        int k = 0;
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                k = k * 10 + (c - '0');
            } else if (c == '['){
                count.push(k);
                k = 0;
                tokens.push(current);
                current = new StringBuilder();
            } else if (c == ']'){
                int repeat = count.pop();
                StringBuilder parent = tokens.pop();
                parent.append(current.toString().repeat(repeat));
                current = parent;
            } else {
                current.append(c);
            }
        }

        return current.toString();
    }

    private static String tokenise(String input, int[] currIndex) {
        int size = input.length();
        StringBuilder output = new StringBuilder();
        while (currIndex[0] < size) {
            char c = input.charAt(currIndex[0]);
            int k = 0;
            if (Character.isDigit(c)) {
                while (Character.isDigit(input.charAt(currIndex[0]))) {
                    k = k * 10 + (input.charAt(currIndex[0]) - '0');
                    currIndex[0]++;
                }
                // now pointer is at '['
                currIndex[0]++;
                String inner = tokenise(input, currIndex);
                currIndex[0]++;
                output.append(inner.repeat(k));
            } else if (c == ']') {
                break;
            } else {
                output.append(c);
                currIndex[0]++;
            }
        }
        return output.toString();
    }

    private static class Token {

        String value;

        int count;

        Token(String value, int count) {
            this.value = value;
            this.count = count;
        }

    }

}
