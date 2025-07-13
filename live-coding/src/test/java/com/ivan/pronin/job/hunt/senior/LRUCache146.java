package com.ivan.pronin.job.hunt.senior;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class LRUCache146 {

    /*
     * 146. LRU Cache
     * Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.
     *
     * You must implement the LRUCache class:
     * - LRUCache(int capacity) Initializes the LRU cache with positive size capacity.
     *
     * - int get(int key) Returns the value of the key if the key exists, otherwise returns -1.
     * - void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache.
     * If the number of keys exceeds the capacity from this operation, evict the least recently used key.
     * * The functions get and put must each run in O(1) average time complexity.
     */

    private static class LRUCache {

        private final int capacity;

        private final HashMap<Integer, Node> cache = new HashMap<>();

        private final Node head = new Node(0, 0);

        private final Node tail = new Node(0, 0);

        private static class Node {

            int key, value;

            Node prev, next;

            Node(int k, int v) {
                key = k;
                value = v;
            }

        }

        public LRUCache(int capacity) {
            this.capacity = capacity;
            head.next = tail;
            tail.prev = head;
        }

        public int get(int key) {
            if (!cache.containsKey(key)) return -1;
            Node node = cache.get(key);
            remove(node);
            insertToFront(node);
            return node.value;
        }

        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                remove(cache.get(key));
            } else if (cache.size() == capacity) {
                Node lru = tail.prev;
                remove(lru);
                cache.remove(lru.key);
            }
            Node node = new Node(key, value);
            insertToFront(node);
            cache.put(key, node);
        }

        private void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void insertToFront(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

    }

    @Test
    public void testCustomLRUCache() {
        LRUCache lru = new LRUCache146.LRUCache(2);
        lru.put(1, 1);
        lru.put(2, 2);
        assertEquals(1, lru.get(1));
        lru.put(3, 3); // evicts key 2
        assertEquals(-1, lru.get(2));
        lru.put(4, 4); // evicts key 1
        assertEquals(-1, lru.get(1));
        assertEquals(3, lru.get(3));
        assertEquals(4, lru.get(4));
    }

}
