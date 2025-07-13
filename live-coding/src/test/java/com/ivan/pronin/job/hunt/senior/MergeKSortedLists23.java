package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.ivan.pronin.job.hunt.senior.model.ListNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class MergeKSortedLists23 {

    /*
     * 23. Merge k Sorted Lists
     * You are given an array of k linked-lists lists, each linked-list is sorted in ascending order.
     * Merge all the linked-lists into one sorted linked-list and return it.
     * Example:
     * Input: lists = [[1,4,5],[1,3,4],[2,6]]
     * Output: [1,1,2,3,4,4,5,6]
     * 📊 Сложность:
     * Time: O(N log k) — где N — общее количество узлов во всех списках, k — количество списков.
     * Space: O(k) — используем кучу для хранения узлов, где k — количество списков.
     */
    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));
        for (ListNode list : lists) {
            if (list != null) {
                minHeap.offer(list);
            }
        }

        // Создаем фиктивный узел для упрощения логики
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        while (!minHeap.isEmpty()) {
            // Извлекаем узел с наименьшим значением
            ListNode minNode = minHeap.poll();
            current.next = minNode; // Добавляем его к результату

            current = current.next; // Переходим к следующему узлу
            if (minNode.next != null) {
                minHeap.offer(minNode.next); // Если есть следующий узел, добавляем его в кучу
            }
        }
        return dummy.next; // Возвращаем следующий узел после фиктивного
    }

    /*
     * Разделяй и властвуй (Divide and Conquer)
     * 📊 Сложность:
     * Time: O(N log k) — где N — общее количество узлов во всех списках, k — количество списков.
     * Space: O(log k) — используется стек вызовов для рекурсии, где k — количество списков.
     */
    public ListNode mergeKListsDaC(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return mergeRange(lists, 0, lists.length - 1);
    }

    private ListNode mergeRange(ListNode[] lists, int left, int right) {
        if (left == right) return lists[left];

        int mid = left + (right - left) / 2;
        ListNode l1 = mergeRange(lists, left, mid);
        ListNode l2 = mergeRange(lists, mid + 1, right);

        return mergeTwoLists(l1, l2);
    }

    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0), tail = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                tail.next = l1;
                l1 = l1.next;
            } else {
                tail.next = l2;
                l2 = l2.next;
            }
            tail = tail.next;
        }

        tail.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    private ListNode buildList(int... vals) {
        ListNode dummy = new ListNode(0), curr = dummy;
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
    public void testHeapSolution() {
        ListNode[] lists = new ListNode[]{
            buildList(1, 4, 5),
            buildList(1, 3, 4),
            buildList(2, 6)
        };
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4, 5, 6}, toArray(mergeKLists(lists)));
    }

    @Test
    public void testDivideAndConquerSolution() {
        ListNode[] lists = new ListNode[]{
            buildList(1, 4, 5),
            buildList(1, 3, 4),
            buildList(2, 6)
        };
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4, 5, 6}, toArray(mergeKListsDaC(lists)));
    }

}
