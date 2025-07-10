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
public class MinStack155 {

    /*
     * 155. Min Stack
     * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
     * * Implement the MinStack class:
     * * MinStack() initializes the stack object.
     * * void push(int val) pushes the element val onto the stack.
     * * void pop() removes the element on the top of the stack.
     * * int top() gets the top element of the stack.
     * * int getMin() retrieves the minimum element in the stack.
     */

    private Stack<Integer> stack;

    private Stack<Integer> minStack;

    /*
     * 📊 Сложность:
     * Time: O(1) — все операции выполняются за константное время.
     * Space: O(n) — используем дополнительный стек для хранения минимумов.
     * * Примечание: в этом решении мы сохраняем минимум на каждом уровне стека.
     * * Это позволяет нам быстро получать минимум, не перебирая весь стек.
     * * * Альтернативный подход: можно хранить только один минимум, обновляя его при каждом push/pop.
     * * * Но это усложнит реализацию, так как придется отслеживать изменения минимума.
     *
     */
    public MinStack155() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }

    public void push(int val) {
        stack.push(val);
        // сохраняем минимум на текущем уровне
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        } else {
            minStack.push(minStack.peek());
        }
    }

    public void pop() {
        stack.pop();
        minStack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }

    @BeforeEach
    void setUp() {
        stack = new Stack<>();
        minStack = new Stack<>();
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
