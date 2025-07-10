package com.ivan.pronin.job.hunt.middle;

import java.util.Stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class MinStack155_Array {

    /*
     * 155. Min Stack
     * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
     * * Implement the MinStack class:
     * * MinStack() initializes the stack object.
     * * void push(int val) pushes the element val onto the stack.
     * * void pop() removes the element on the top of the stack.
     * * int top() gets the top element of the stack.
     * * int getMin() retrieves the minimum element in the stack.
     *
     * 📊 Сложность:
     * Time: O(1) — все операции выполняются за константное время.
     * Space: O(n) — используем дополнительный стек для хранения минимумов.
     */

    private Stack<int[]> stack; // каждый элемент: [val, currentMin]

    public MinStack155_Array() {
        stack = new Stack<>();
    }

    public void push(int val) {
        if (stack.isEmpty()) {
            stack.push(new int[]{val, val});
        } else {
            int currentMin = Math.min(val, stack.peek()[1]);
            stack.push(new int[]{val, currentMin});
        }
    }

    public void pop() {
        stack.pop();
    }

    public int top() {
        return stack.peek()[0];
    }

    public int getMin() {
        return stack.peek()[1];
    }

    @BeforeEach
    void setUp() {
        stack = new Stack<>();
    }

    @Test
    void testMinStackOperations() {
        push(-2);
        push(0);
        push(-3);
        assertEquals(-3, getMin());

        pop();
        assertEquals(0, top());
        assertEquals(-2, getMin());
    }

    @Test
    void testSingleElement() {
        push(42);
        assertEquals(42, top());
        assertEquals(42, getMin());

        pop();
        assertThrows(Exception.class, this::top);     // стек пуст
        assertThrows(Exception.class, this::getMin);  // стек пуст
    }

    @Test
    void testIncreasingValues() {
        push(1);
        push(2);
        push(3);
        assertEquals(1, getMin());
    }

    @Test
    void testDecreasingValues() {
        push(3);
        push(2);
        push(1);
        assertEquals(1, getMin());

        pop();
        assertEquals(2, getMin());
    }

}
