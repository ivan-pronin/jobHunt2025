package com.ivan.pronin.job.hunt.senior.google;

import java.util.Stack;

/**
 * @author Ivan Pronin
 * @since 04.09.2025
 *
 * 📘 Basic Calculator I (LeetCode 224) — Medium
 * Implement a basic calculator to evaluate a simple expression string with:
 *
 * digits
 * +, -
 * parentheses ( and )
 * and no multiplication or division
 *
 * ✅ Spaces allowed
 * ✅ Only integer results
 * ✅ Must handle nested parentheses
 */
public class BasicCalculator1 {

    public static int calculate(String expression) {
        Stack<Integer> result = new Stack<>();
        int num = 0;
        int res = 0;
        int sign = 1;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)){
                num = num * 10 + (c - '0');
            }

            if (!Character.isDigit(c) || c != ' ' ){
                if (c == '+'){
                    res = res + num * sign;
                    sign = 1;
                    num = 0;
                } else if (c == '-'){
                    res = res + num * sign;
                    sign = -1;
                    num = 0;
                } else if (c == '('){
                    result.push(res);
                    result.push(sign);
                    res = 0;
                    sign = 1;
                } else if (c == ')'){
                    res = res + num * sign; // Apply last number
                    num = 0;
                    res *= result.pop(); // Apply sign before '('
                    res += result.pop(); // Add result before '('
                }
            }
        }
        return res + sign * num;
    }

}
