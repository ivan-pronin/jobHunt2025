package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 14.07.2025
 */
public class InsertDeleteGetRandom380 {

    /**
     * 380. Insert Delete GetRandom O(1)
     * Design a data structure that supports all following operations in average O(1) time.
     * Note: Duplicate elements are allowed.
     * 1. insert(val): Inserts an item val to the set if not already present.
     * 2. remove(val): Removes an item val from the set if present.
     * 3. getRandom: Returns a random element from current set of elements (it's guaranteed that at least one element exists when this method is called).
     *
     * 📊 Сложность:
     * Time: O(1) — все операции выполняются за константное время.
     * Space: O(n) — храним элементы в списке и их индексы в мапе.
     */
    private final List<Integer> list = new ArrayList<>();

    private final Map<Integer, Integer> valToIndex = new HashMap<>();

    private final Random rand = new Random();

    public boolean insert(int val) {
        if (valToIndex.containsKey(val)) return false;
        valToIndex.put(val, list.size());
        list.add(val);
        return true;
    }

    public boolean remove(int val) {
        if (!valToIndex.containsKey(val)) return false;

        int idx = valToIndex.get(val);
        int lastElement = list.get(list.size() - 1);

        // Переставляем последний элемент на место удаляемого
        list.set(idx, lastElement);
        valToIndex.put(lastElement, idx);

        // Удаляем последний
        list.remove(list.size() - 1);
        valToIndex.remove(val);
        return true;
    }

    public int getRandom() {
        return list.get(rand.nextInt(list.size()));
    }

    @Test
    public void testInsertDeleteGetRandom() {
        InsertDeleteGetRandom380 solution = new InsertDeleteGetRandom380();
        assertTrue(solution.insert(1));
        assertTrue(solution.insert(2));
        assertFalse(solution.insert(1)); // Дубликат
        assertTrue(solution.remove(1));
        assertTrue(solution.insert(3));
        assertEquals(2, solution.getRandom()); // Вернет 2 или 3
    }
}
