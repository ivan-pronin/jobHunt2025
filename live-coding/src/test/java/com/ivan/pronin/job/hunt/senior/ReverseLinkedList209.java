package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.List;

import com.ivan.pronin.job.hunt.senior.model.ListNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class ReverseLinkedList209 {

    /*
     * 209. Reverse Linked List
     * Reverse a singly linked list.
     * Example:
     * Input: head = [1,2,3,4,5]
     * Output: [5,4,3,2,1]
     *
     * 📊 Сложность:
     * Time: O(n) — каждый узел посещается один раз.
     * Space: O(1) — используем только несколько указателей, не требуем дополнительной памяти.
     */
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode next = curr.next; // сохранить следующее
            curr.next = prev;          // перевернуть ссылку
            prev = curr;               // двинуть prev
            curr = next;               // двинуть curr
        }

        return prev;
    }

    public static ListNode reverseListRecursive(ListNode head) {
        //  1 -> 5 -> 10 -> null
        // базовый случай: если голова пустая или один узел, возвращаем голову
        if (head == null || head.next == null) {
            return head;
        }
        // рекурсивно переворачиваем оставшуюся часть списка
        ListNode newHead = reverseListRecursive(head.next);
        // устанавливаем текущий узел как следующий для следующего узла
        head.next.next = head;
        // обнуляем ссылку на следующий узел для текущего узла
        head.next = null;
        return newHead; // возвращаем новый "голову" списка
    }

    private ListNode buildList(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        for (int val : vals) {
            curr.next = new ListNode(val);
            curr = curr.next;
        }
        return dummy.next;
    }

    private int[] toArray(ListNode head) {
        List<Integer> res = new ArrayList<>();
        while (head != null) {
            res.add(head.val);
            head = head.next;
        }
        return res.stream().mapToInt(i -> i).toArray();
    }

    @Test
    public void testIterative() {
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, toArray(reverseList(buildList(1, 2, 3, 4, 5))));
        assertArrayEquals(new int[]{}, toArray(reverseList(buildList())));
        assertArrayEquals(new int[]{1}, toArray(reverseList(buildList(1))));
    }

    @Test
    public void testRecursive() {
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, toArray(reverseListRecursive(buildList(1, 2, 3, 4, 5))));
        assertArrayEquals(new int[]{}, toArray(reverseListRecursive(buildList())));
        assertArrayEquals(new int[]{1}, toArray(reverseListRecursive(buildList(1))));
    }

}
