package com.ivan.pronin.job.hunt.senior;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class LFUCache460 {

    /*
     * 460. LFU Cache
     * Design and implement a data structure for a Least Frequently Used (LFU) cache.
     * It should support the following operations: get and put.
     * * get(key) - Get the value of the key if the key exists in the cache. Otherwise, return -1.
     * * put(key, value) - Set or insert the value if the key is not already present.
     * If the cache reaches its capacity, it should invalidate the least frequently used key before inserting a new key-value pair.
     * * The functions get and put must each run in O(1) average time complexity.
     * * Note: The LFU cache should evict the least frequently used key when the cache reaches its capacity.
     *
     * LFUCache implementation using a combination of a HashMap and a LinkedHashSet.
     * The HashMap stores the key to value mapping, while the LinkedHashSet keeps track of keys by their frequency.
     * The cache supports O(1) time complexity for both get and put operations.
     * The cache evicts the least frequently used key when it reaches its capacity.
     * The cache maintains a minimum frequency to efficiently find the least frequently used key.
     * 📊 Сложность:
     * Time: O(1) for both get and put operations.
     * Space: O(n) where n is the number of keys in the cache, as we store both keys and their frequencies.
     */
    private static class LFUCache {

        private static class Entry {

            int key, value, freq;

            Entry(int key, int value) {
                this.key = key;
                this.value = value;
                this.freq = 1;
            }

        }

        private final int capacity;

        private int size = 0;

        private int minFreq = 0;

        private final Map<Integer, Entry> keyToEntry = new HashMap<>();

        private final Map<Integer, LinkedHashSet<Integer>> freqToKeys = new HashMap<>();

        public LFUCache(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            if (!keyToEntry.containsKey(key)) return -1;
            increaseFreq(key);
            return keyToEntry.get(key).value;
        }

        public void put(int key, int value) {
            if (capacity == 0) return;

            if (keyToEntry.containsKey(key)) {
                Entry entry = keyToEntry.get(key);
                entry.value = value;
                increaseFreq(key);
            } else {
                if (size == capacity) evictLFU();
                Entry entry = new Entry(key, value);
                keyToEntry.put(key, entry);
                freqToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
                minFreq = 1;
                size++;
            }
        }

        private void increaseFreq(int key) {
            Entry entry = keyToEntry.get(key);
            int freq = entry.freq;
            freqToKeys.get(freq).remove(key);
            if (freqToKeys.get(freq).isEmpty()) {
                freqToKeys.remove(freq);
                if (freq == minFreq) minFreq++;
            }

            entry.freq++;
            freqToKeys.computeIfAbsent(entry.freq, k -> new LinkedHashSet<>()).add(key);
        }

        private void evictLFU() {
            LinkedHashSet<Integer> keys = freqToKeys.get(minFreq);
            int keyToRemove = keys.iterator().next();
            keys.remove(keyToRemove);
            if (keys.isEmpty()) freqToKeys.remove(minFreq);

            keyToEntry.remove(keyToRemove);
            size--;
        }

    }

    @Test
    public void testLFUSimple() {
        LFUCache lfu = new LFUCache(2);
        lfu.put(1, 1);
        lfu.put(2, 2);
        assertEquals(1, lfu.get(1)); // частота 1: 2, частота 2: 1
        lfu.put(3, 3); // удаляет 2 (меньшая частота)
        assertEquals(-1, lfu.get(2));
        assertEquals(1, lfu.get(1));
        assertEquals(3, lfu.get(3));
        lfu.put(4, 4); // удаляет 3 (меньше freq)
        assertEquals(-1, lfu.get(3));
        assertEquals(1, lfu.get(1));
        assertEquals(4, lfu.get(4));
    }

}
