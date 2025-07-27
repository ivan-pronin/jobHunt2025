package com.ivan.pronin.job.hunt.system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class InMemoryDbWithTransactions {

    public class InMemoryDB {

        private final Map<String, String> db = new HashMap<>();

        private Map<String, String> transactionBuffer = null;

        public void set(String key, String value) {
            if (transactionBuffer != null) {
                transactionBuffer.put(key, value);
            } else {
                db.put(key, value);
            }
        }

        public String get(String key) {
            if (transactionBuffer != null && transactionBuffer.containsKey(key)) {
                return transactionBuffer.get(key);
            }
            return db.get(key);
        }

        public void delete(String key) {
            if (transactionBuffer != null) {
                transactionBuffer.put(key, null);
            } else {
                db.remove(key);
            }
        }

        public void begin() {
            if (transactionBuffer == null) {
                transactionBuffer = new HashMap<>();
            }
        }

        public void commit() {
            if (transactionBuffer != null) {
                for (Map.Entry<String, String> entry : transactionBuffer.entrySet()) {
                    if (entry.getValue() == null) {
                        db.remove(entry.getKey());
                    } else {
                        db.put(entry.getKey(), entry.getValue());
                    }
                }
                transactionBuffer = null;
            }
        }

        public void rollback() {
            transactionBuffer = null;
        }

    }

    @Test
    public void testBasicTransaction() {
        InMemoryDB db = new InMemoryDB();
        db.set("a", "1");
        assertEquals("1", db.get("a"));

        db.begin();
        db.set("a", "2");
        assertEquals("2", db.get("a"));
        db.rollback();
        assertEquals("1", db.get("a"));

        db.begin();
        db.set("a", "3");
        db.commit();
        assertEquals("3", db.get("a"));
    }

    public class NestedTransactionDB {

        private final Map<String, String> db = new HashMap<>();

        private final Deque<Map<String, String>> txStack = new ArrayDeque<>();

        public void set(String key, String value) {
            if (!txStack.isEmpty()) {
                txStack.peek().put(key, value);
            } else {
                db.put(key, value);
            }
        }

        public String get(String key) {
            for (Map<String, String> layer : txStack) {
                if (layer.containsKey(key)) return layer.get(key);
            }
            return db.get(key);
        }

        public void delete(String key) {
            set(key, null);
        }

        public void begin() {
            txStack.push(new HashMap<>());
        }

        public void commit() {
            if (txStack.isEmpty()) return;
            Map<String, String> top = txStack.pop();
            if (!txStack.isEmpty()) {
                txStack.peek().putAll(top);
            } else {
                for (Map.Entry<String, String> e : top.entrySet()) {
                    if (e.getValue() == null) {
                        db.remove(e.getKey());
                    } else {
                        db.put(e.getKey(), e.getValue());
                    }
                }
            }
        }

        public void rollback() {
            if (!txStack.isEmpty()) {
                txStack.pop();
            }
        }

    }

    @Test
    public void testNestedTransactions() {
        NestedTransactionDB db = new NestedTransactionDB();

        db.set("x", "1");
        db.begin(); // tx1
        db.set("x", "2");
        assertEquals("2", db.get("x"));

        db.begin(); // tx2
        db.set("x", "3");
        assertEquals("3", db.get("x"));
        db.rollback(); // rollback tx2
        assertEquals("2", db.get("x"));

        db.commit(); // commit tx1
        assertEquals("2", db.get("x"));
    }

    public class NestedTransactionDBExtended implements Serializable {

        private static class ValueEntry implements Serializable {

            String value;

            Long expiresAt;

            ValueEntry(String value, Long expiresAt) {
                this.value = value;
                this.expiresAt = expiresAt;
            }

            boolean isAlive() {
                return expiresAt == null || System.currentTimeMillis() < expiresAt;
            }

        }

        private Map<String, ValueEntry> db = new HashMap<>();

        private Deque<Map<String, ValueEntry>> txStack = new ArrayDeque<>();

        private List<Map<String, ValueEntry>> snapshots = new ArrayList<>();

        private Map<String, Set<String>> invertedIndex = new HashMap<>();

        // SET with optional TTL
        public void set(String key, String value, Integer ttlSeconds) {
            Long expiresAt = ttlSeconds != null ? System.currentTimeMillis() + ttlSeconds * 1000L : null;
            ValueEntry entry = new ValueEntry(value, expiresAt);

            Map<String, ValueEntry> target = txStack.isEmpty() ? db : txStack.peek();
            target.put(key, entry);

            if (value != null) {
                invertedIndex.computeIfAbsent(value, k -> new HashSet<>()).add(key);
            }
        }

        public void set(String key, String value) {
            set(key, value, null);
        }

        public String get(String key) {
            for (Map<String, ValueEntry> layer : txStack) {
                if (layer.containsKey(key)) {
                    ValueEntry entry = layer.get(key);
                    return (entry != null && entry.isAlive()) ? entry.value : null;
                }
            }
            ValueEntry entry = db.get(key);
            return (entry != null && entry.isAlive()) ? entry.value : null;
        }

        public void delete(String key) {
            set(key, null);
        }

        public int count(String value) {
            Set<String> keys = invertedIndex.getOrDefault(value, Collections.emptySet());
            int count = 0;
            for (String key : keys) {
                String v = get(key);
                if (value.equals(v)) count++;
            }
            return count;
        }

        public void begin() {
            txStack.push(new HashMap<>());
        }

        public void commit() {
            if (txStack.isEmpty()) return;
            Map<String, ValueEntry> top = txStack.pop();
            Map<String, ValueEntry> target = txStack.isEmpty() ? db : txStack.peek();
            for (Map.Entry<String, ValueEntry> e : top.entrySet()) {
                target.put(e.getKey(), e.getValue());
            }
        }

        public void rollback() {
            if (!txStack.isEmpty()) {
                txStack.pop();
            }
        }

        public void saveSnapshot() {
            Map<String, ValueEntry> copy = new HashMap<>();
            for (Map.Entry<String, ValueEntry> entry : db.entrySet()) {
                if (entry.getValue().isAlive()) {
                    copy.put(entry.getKey(), entry.getValue());
                }
            }
            snapshots.add(copy);
        }

        public void restoreSnapshot(int index) {
            if (index >= 0 && index < snapshots.size()) {
                db = new HashMap<>(snapshots.get(index));
                txStack.clear();
            }
        }

        public void saveToFile(String filePath) throws IOException {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
                oos.writeObject(this);
            }
        }

        public static NestedTransactionDB loadFromFile(String filePath) throws IOException, ClassNotFoundException {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                return (NestedTransactionDB) ois.readObject();
            }
        }

    }

}
