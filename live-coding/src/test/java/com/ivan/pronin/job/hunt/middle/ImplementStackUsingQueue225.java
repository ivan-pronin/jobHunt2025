package com.ivan.pronin.job.hunt.middle;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class ImplementStackUsingQueue225 {

    /*
     * 225. Implement Stack using Queues
     * Implement a last in first out (LIFO) stack using only two queues. The implemented stack should support all the functions of a normal stack (push, top, pop, and retrieving the size).
     * You must use only standard operations of a queue, which means that only push to back, peek/pop from front, size, and is empty operations are valid.
     * Implement the MyStack class:
     * MyStack() Initializes the stack using the two queues.
     * void push(int x) Pushes element x to the top of the stack.
     * int pop() Removes the element on the top of the stack and returns it.
     * int top() Returns the element on the top of the stack.
     * boolean empty() Returns true if the stack is empty, false otherwise.
     * Note that the stack follows the LIFO order, meaning that the last element pushed onto the stack is the first one to be popped off.
     * 📊 Сложность:
     * Time: O(n) для операции push, так как нужно переместить все элементы из одной очереди в другую.
     * Space: O(n) для хранения элементов в двух очередях.
     * * Решение с использованием двух очередей:
     *
     */
    public static class StackWithQueues {

        private Queue<Integer> q1 = new LinkedList<>();

        private Queue<Integer> q2 = new LinkedList<>();

        // Push element onto stack
        public void push(Integer x) {
            q2.offer(x);
            while (!q1.isEmpty()) {
                q2.offer(q1.poll());
            }
            // Swap queues
            Queue<Integer> temp = q1;
            q1 = q2;
            q2 = temp;
        }

        // Removes the element on top of the stack
        public Integer pop() {
            return q1.poll();
        }

        // Get the top element
        public Integer top() {
            return q1.peek();
        }

        // Return whether the stack is empty
        public boolean isEmpty() {
            return q1.isEmpty();
        }

    }

    @Test
    void testPushAndTop() {
        ImplementStackUsingQueue225.StackWithQueues stack = new ImplementStackUsingQueue225.StackWithQueues();
        stack.push(1);
        assertEquals(1, stack.top());
        stack.push(2);
        assertEquals(2, stack.top());
        stack.push(3);
        assertEquals(3, stack.top());
    }

    @Test
    void testPop() {
        ImplementStackUsingQueue225.StackWithQueues stack = new ImplementStackUsingQueue225.StackWithQueues();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        assertEquals(30, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void testIsEmpty() {
        ImplementStackUsingQueue225.StackWithQueues stack = new ImplementStackUsingQueue225.StackWithQueues();
        assertTrue(stack.isEmpty());
        stack.push(5);
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testMixedOperations() {
        ImplementStackUsingQueue225.StackWithQueues stack = new ImplementStackUsingQueue225.StackWithQueues();
        stack.push(100);
        stack.push(200);
        assertEquals(200, stack.top());
        assertEquals(200, stack.pop());
        assertEquals(100, stack.top());
        stack.push(300);
        assertEquals(300, stack.top());
        assertFalse(stack.isEmpty());
    }

}
