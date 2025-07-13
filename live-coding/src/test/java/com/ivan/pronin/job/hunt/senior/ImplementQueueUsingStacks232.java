package com.ivan.pronin.job.hunt.senior;

import java.util.Stack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class ImplementQueueUsingStacks232 {
    /*
     * 232. Implement Queue using Stacks
     * Implement a first in first out (FIFO) queue using only two stacks.
     * The implemented queue should support all the functions of a normal queue (push, pop, peek, empty).
     * Example:
     * Input: ["MyQueue", "push", "push", "peek", "pop", "empty"]
     * Output: [null, null, null, 1, 1, false]
     */
    private final Stack<Integer> inStack = new Stack<>();
    private final Stack<Integer> outStack = new Stack<>();

    public void push(int x) {
        inStack.push(x);
    }

    public int pop() {
        shiftStacks();
        return outStack.pop();
    }

    public int peek() {
        shiftStacks();
        return outStack.peek();
    }

    public boolean empty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }

    private void shiftStacks() {
        if (outStack.isEmpty()) {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }

    @Test
    public void testClassicQueue() {
        ImplementQueueUsingStacks232 queue = new ImplementQueueUsingStacks232();
        assertTrue(queue.empty());

        queue.push(1);
        queue.push(2);
        queue.push(3);

        assertEquals(1, queue.peek());
        assertEquals(1, queue.pop());
        assertFalse(queue.empty());

        assertEquals(2, queue.pop());
        assertEquals(3, queue.pop());
        assertTrue(queue.empty());
    }

}
