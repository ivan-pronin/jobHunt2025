package com.ivan.pronin.job.hunt.system;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class CacheWithTTLandEviction {

    private static class TTLCache<K, V> {

        private final ConcurrentHashMap<K, CacheEntry<V>> map = new ConcurrentHashMap<>();

        private final long ttlMillis;

        private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

        public TTLCache(long ttlMillis, long cleanupIntervalMillis) {
            this.ttlMillis = ttlMillis;
            cleaner.scheduleAtFixedRate(this::cleanup, cleanupIntervalMillis, cleanupIntervalMillis, TimeUnit.MILLISECONDS);
        }

        public void put(K key, V value) {
            long expireAt = System.currentTimeMillis() + ttlMillis;
            map.put(key, new CacheEntry<>(value, expireAt));
        }

        public V get(K key) {
            CacheEntry<V> entry = map.get(key);
            if (entry == null || System.currentTimeMillis() > entry.expireAt) {
                map.remove(key); // очистка по запросу
                return null;
            }
            return entry.value;
        }

        public void remove(K key) {
            map.remove(key);
        }

        private void cleanup() {
            long now = System.currentTimeMillis();
            for (Map.Entry<K, CacheEntry<V>> entry : map.entrySet()) {
                if (now > entry.getValue().expireAt) {
                    map.remove(entry.getKey());
                }
            }
        }

        public void shutdown() {
            cleaner.shutdown();
        }

        private static class CacheEntry<V> {

            V value;

            long expireAt;

            CacheEntry(V value, long expireAt) {
                this.value = value;
                this.expireAt = expireAt;
            }

        }

    }

    @Test
    public void testTTLCache() throws InterruptedException {
        TTLCache<String, String> cache = new TTLCache<>(1000, 500); // 1 сек TTL

        cache.put("k1", "v1");
        assertEquals("v1", cache.get("k1"));

        Thread.sleep(1100); // истекает TTL
        assertNull(cache.get("k1"));

        cache.shutdown();
    }

    private static class TTLCacheWithLRU<K, V> {
        private final long ttlMillis;

        private final Map<K, CacheEntry<V>> map;

        public TTLCacheWithLRU(int maxSize, long ttlMillis) {
            this.ttlMillis = ttlMillis;
            this.map = new LinkedHashMap<K, CacheEntry<V>>(16, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<K, CacheEntry<V>> eldest) {
                    return size() > maxSize;
                }
            };
        }

        public synchronized void put(K key, V value) {
            map.put(key, new CacheEntry<>(value, System.currentTimeMillis() + ttlMillis));
        }

        public synchronized V get(K key) {
            CacheEntry<V> entry = map.get(key);
            if (entry == null || System.currentTimeMillis() > entry.expireAt) {
                map.remove(key);
                return null;
            }
            return entry.value;
        }

        private static class CacheEntry<V> {
            V value;
            long expireAt;

            CacheEntry(V value, long expireAt) {
                this.value = value;
                this.expireAt = expireAt;
            }
        }
    }

    @Test
    public void testTTLAndEviction() throws InterruptedException {
        TTLCacheWithLRU<String, String> cache = new TTLCacheWithLRU<>(2, 1000); // 2 элемента, TTL 1 секунда

        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3"); // a будет удален по LRU

        assertNull(cache.get("a")); // вытеснен
        assertEquals("2", cache.get("b"));
        assertEquals("3", cache.get("c"));

        Thread.sleep(1100);
        assertNull(cache.get("b")); // TTL истек
        assertNull(cache.get("c"));
    }

}
