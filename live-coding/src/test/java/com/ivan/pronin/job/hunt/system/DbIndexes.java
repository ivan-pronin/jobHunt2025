package com.ivan.pronin.job.hunt.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class DbIndexes {

    // --- Implementation 1: Naive Table (no index) ---
    class NaiveTable {

        private final List<Map<String, Object>> rows = new ArrayList<>();

        public void insert(Map<String, Object> row) {
            rows.add(row);
        }

        public List<Map<String, Object>> selectWhere(String key, Object value) {
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                if (Objects.equals(row.get(key), value)) {
                    result.add(row);
                }
            }
            return result;
        }

        public List<Map<String, Object>> selectWhereCompound(Map<String, Object> criteria) {
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                boolean match = true;
                for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                    if (!Objects.equals(row.get(entry.getKey()), entry.getValue())) {
                        match = false;
                        break;
                    }
                }
                if (match) result.add(row);
            }
            return result;
        }

    }

// --- Implementation 2: Indexed Table (with compound index support) ---

    class IndexedTable {

        private final List<Map<String, Object>> rows = new ArrayList<>();

        private final Map<String, Map<Object, Set<Integer>>> index = new HashMap<>();

        private final Map<List<String>, Map<List<Object>, Set<Integer>>> compoundIndexes = new HashMap<>();

        public void insert(Map<String, Object> row) {
            int idx = rows.size();
            rows.add(row);
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                index.computeIfAbsent(entry.getKey(), k -> new HashMap<>())
                    .computeIfAbsent(entry.getValue(), v -> new HashSet<>())
                    .add(idx);
            }
            // update compound indexes
            for (Map.Entry<List<String>, Map<List<Object>, Set<Integer>>> compoundIndex : compoundIndexes.entrySet()) {
                List<String> keys = compoundIndex.getKey();
                List<Object> values = new ArrayList<>();
                boolean allPresent = true;
                for (String key : keys) {
                    if (!row.containsKey(key)) {
                        allPresent = false;
                        break;
                    }
                    values.add(row.get(key));
                }
                if (allPresent) {
                    compoundIndex.getValue()
                        .computeIfAbsent(values, k -> new HashSet<>())
                        .add(idx);
                }
            }
        }

        public List<Map<String, Object>> selectWhere(String key, Object value) {
            List<Map<String, Object>> result = new ArrayList<>();
            Set<Integer> indices = index.getOrDefault(key, Collections.emptyMap())
                .getOrDefault(value, Collections.emptySet());
            for (int i : indices) {
                result.add(rows.get(i));
            }
            return result;
        }

        public void createCompoundIndex(List<String> keys) {
            compoundIndexes.putIfAbsent(keys, new HashMap<>());
        }

        public List<Map<String, Object>> selectWhereCompound(Map<String, Object> criteria) {
            List<String> keys = new ArrayList<>(criteria.keySet());
            List<Object> values = new ArrayList<>();
            for (String k : keys) values.add(criteria.get(k));
            Map<List<Object>, Set<Integer>> idx = compoundIndexes.get(keys);

            List<Map<String, Object>> result = new ArrayList<>();
            if (idx != null) {
                Set<Integer> indices = idx.getOrDefault(values, Collections.emptySet());
                for (int i : indices) result.add(rows.get(i));
            } else {
                for (Map<String, Object> row : rows) {
                    boolean match = true;
                    for (Map.Entry<String, Object> entry : criteria.entrySet()) {
                        if (!Objects.equals(row.get(entry.getKey()), entry.getValue())) {
                            match = false;
                            break;
                        }
                    }
                    if (match) result.add(row);
                }
            }
            return result;
        }

    }

// --- Unit Tests ---

    @Test
    public void testDbs() {
        NaiveTable naive = new NaiveTable();
        IndexedTable indexed = new IndexedTable();

        for (int i = 0; i < 100; i++) {
            Map<String, Object> row = Map.of(
                "id", i,
                "category", (i % 2 == 0 ? "A" : "B"),
                "type", (i % 5 == 0 ? "X" : "Y")
            );
            naive.insert(row);
            indexed.insert(row);
        }

        indexed.createCompoundIndex(List.of("category", "type"));

        List<Map<String, Object>> naiveResult = naive.selectWhere("category", "A");
        List<Map<String, Object>> indexedResult = indexed.selectWhere("category", "A");

        Map<String, Object> compoundQuery = Map.of("category", "A", "type", "X");
        List<Map<String, Object>> naiveCompound = naive.selectWhereCompound(compoundQuery);
        List<Map<String, Object>> indexedCompound = indexed.selectWhereCompound(compoundQuery);

        System.out.println("Naive count: " + naiveResult.size());
        System.out.println("Indexed count: " + indexedResult.size());
        System.out.println("Naive compound: " + naiveCompound.size());
        System.out.println("Indexed compound: " + indexedCompound.size());

        assertEquals(50, naiveResult.size());
        assertEquals(50, indexedResult.size());
        assertEquals(naiveCompound.size(), indexedCompound.size());
    }

}
