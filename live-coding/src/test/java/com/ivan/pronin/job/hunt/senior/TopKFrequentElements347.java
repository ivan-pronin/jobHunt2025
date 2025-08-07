package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class TopKFrequentElements347 {

    /*
     * 347. Top K Frequent Elements
     * Given an integer array nums and an integer k, return the k most frequent elements.
     * Example:
     * Input: nums = [1,1,1,2,2,3], k = 2
     * Output: [1,2]
     *
     * 📊 Сложность:
     * Time: O(n log k) — где n — количество элементов в массиве, k — количество наиболее частых элементов.
     * Space: O(n) — используется хэш-таблица для хранения частот элементов.
     */
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) freq.put(num, freq.getOrDefault(num, 0) + 1);

        PriorityQueue<Map.Entry<Integer, Integer>> minHeap =
            new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            minHeap.offer(entry);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        return minHeap.stream()
            .mapToInt(Map.Entry::getKey)
            .toArray();
    }

    /*
     * Решение с использованием bucket sort
     * 📊 Сложность:
     * Time: O(n) — где n — количество элементов в массиве, так как мы проходим по каждому элементу и распределяем их по корзинам.
     * Space: O(n) — используется массив списков для хранения элементов по их частоте, где n — количество уникальных элементов.
     * Примечание: это решение работает хорошо, когда k значительно меньше n, так как мы не сортируем все элементы, а только собираем наиболее частые.
     */
    public int[] topKFrequentBucket(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) freq.put(num, freq.getOrDefault(num, 0) + 1);

        List<Integer>[] buckets = new List[nums.length + 1];
        for (int i = 0; i <= nums.length; i++) buckets[i] = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
            buckets[entry.getValue()].add(entry.getKey());
        }

        List<Integer> result = new ArrayList<>();
        for (int i = buckets.length - 1; i >= 0 && result.size() < k; i--) {
            result.addAll(buckets[i]);
        }

        return result.stream().mapToInt(i -> i).limit(k).toArray();
    }

    private void assertUnorderedEquals(int[] expected, int[] actual) {
        Arrays.sort(expected);
        Arrays.sort(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testHeapSolution() {
        assertUnorderedEquals(new int[]{1, 2}, topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2));
        assertUnorderedEquals(new int[]{1}, topKFrequent(new int[]{1}, 1));
        assertUnorderedEquals(new int[]{1, 2}, topKFrequent(new int[]{1, 2, 2, 3, 1, 1, 2}, 2));
    }

    @Test
    public void testBucketSolution() {
        assertUnorderedEquals(new int[]{1, 2}, topKFrequentBucket(new int[]{1, 1, 1, 2, 2, 3}, 2));
        assertUnorderedEquals(new int[]{1}, topKFrequent(new int[]{1}, 1));
        assertUnorderedEquals(new int[]{1, 2}, topKFrequent(new int[]{1, 2, 2, 3, 1, 1, 2}, 2));
    }

    @Test
    public void testBucketSolution2() {
        assertUnorderedEquals(new int[]{1, 2}, topKFrequentBucket(new int[]{1, 1, 1, 2, 2, 2, 3,3, 3, 4}, 2));
        assertUnorderedEquals(new int[]{1}, topKFrequent(new int[]{1}, 1));
        assertUnorderedEquals(new int[]{1, 2}, topKFrequent(new int[]{1, 2, 2, 3, 1, 1, 2}, 2));
    }

}
