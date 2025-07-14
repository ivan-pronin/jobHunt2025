package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 14.07.2025
 */
public class DesignStackWithIncrementOperation1381 {
    /*
     * 1381. Design a Stack With Increment Operation
     * Design a stack that supports increment operations on its elements.
     * Implement the CustomStack class:
     * CustomStack(int maxSize) Initializes the object with maxSize which is the maximum number of elements in the stack or do nothing if the stack reached the maxSize.
     * void push(int x) Adds x to the top of the stack if the stack hasn't reached the maxSize.
     * int pop() Removes and returns the top element of the stack or -1 if the stack is empty.
     * void increment(int k, int val) Increments the bottom k elements of the stack by val. If there are less than k elements in the stack, just increment all elements in the stack.
     *
     * 📊 Сложность:
     * Time: O(1) — операции push, pop и increment выполняются за константное время.
     * Space: O(n) — используем массив для хранения элементов стека.
     */

    private static class ListSolution {

        private final int maxSize;

        private final List<Integer> stack;

        public ListSolution(int maxSize) {
            this.maxSize = maxSize;
            this.stack = new ArrayList<>();
        }

        public void push(int x) {
            if (stack.size() < maxSize) {
                stack.add(x);
            }
        }

        public int pop() {
            if (stack.isEmpty()) return -1;
            return stack.remove(stack.size() - 1);
        }

        public void increment(int k, int val) {
            int limit = Math.min(k, stack.size());
            for (int i = 0; i < limit; i++) {
                stack.set(i, stack.get(i) + val);
            }
        }

    }

    private static class OptimizedSolution {

        private final int[] stack;

        private final int[] inc;

        private int top = -1;

        public OptimizedSolution(int maxSize) {
            stack = new int[maxSize];
            inc = new int[maxSize];
        }

        public void push(int x) {
            if (top + 1 < stack.length) {
                top++;
                stack[top] = x;
            }
        }

        public int pop() {
            if (top == -1) return -1;
            int res = stack[top] + inc[top];
            if (top > 0) inc[top - 1] += inc[top];
            inc[top] = 0;
            top--;
            return res;
        }

        public void increment(int k, int val) {
            int idx = Math.min(k, top + 1) - 1;
            if (idx >= 0) inc[idx] += val;
        }

    }

    @Test
    public void test() {
        testStack(new ListSolution(3));
        testStack(new OptimizedSolution(3));
        System.out.println("All tests passed.");
    }

    private static void testStack(Object stackObj) {
        if (stackObj instanceof ListSolution cs) {
            cs.push(1);
            cs.push(2);
            assertEquals(2, cs.pop());
            cs.push(2);
            cs.push(3);
            cs.push(4); // should not be added
            cs.increment(5, 100);
            cs.increment(2, 100);
            assertEquals(103, cs.pop());
            assertEquals(202, cs.pop());
            assertEquals(201, cs.pop());
            assertEquals(-1, cs.pop());
        } else if (stackObj instanceof OptimizedSolution cs) {
            cs.push(1);
            cs.push(2);
            assertEquals(2, cs.pop());
            cs.push(2);
            cs.push(3);
            cs.push(4); // should not be added
            cs.increment(5, 100);
            cs.increment(2, 100);
            assertEquals(103, cs.pop());
            assertEquals(202, cs.pop());
            assertEquals(201, cs.pop());
            assertEquals(-1, cs.pop());
        }
    }

}
