package com.ivan.pronin.job.hunt.senior.google;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * @author Ivan Pronin
 * @since 04.09.2025
 */
public class BasicCalculator2 {

    public static int calculate(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        int num = 0;
        char op = '+';

        for (int i = 0; i <= s.length(); i++) {
            char c = i < s.length() ? s.charAt(i) : '#';

            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            }

            if ((!Character.isDigit(c) && c != ' ') || i == s.length()) {
                if (op == '+') stack.push(num);
                else if (op == '-') stack.push(-num);
                else if (op == '*') stack.push(stack.pop() * num);
                else if (op == '/') stack.push(stack.pop() / num);

                op = c;
                num = 0;
            }
        }

        int sum = 0;
        while (!stack.isEmpty()) sum += stack.pop();
        return sum;
    }

    public static int calculate2(String s) {
        Stack<Integer> stack = new Stack<>();
        int number = 0;
        char operand = '+';
        for (int i = 0; i <= s.length(); i++) {
            char c = i < s.length() ? s.charAt(i) : '#';
            if (Character.isDigit(c)) {
                number = number * 10 + (c - '0');
            }

            if ((!Character.isDigit(c) && c != ' ') || c == '#') {
                if (operand == '+') stack.push(number);
                if (operand == '-') stack.push(-number);
                if (operand == '*') stack.push(stack.pop() * number);
                if (operand == '/') stack.push(stack.pop() / number);

                operand = c;
                number = 0;
            }
        }
        int result = 0;
        while (!stack.isEmpty()) result = result + stack.pop();
        return result;
    }

}
