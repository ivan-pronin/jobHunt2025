package com.ivan.pronin.job.hunt.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 14.07.2025
 */
public class ConsistentHashingImplementation {

    /*
     * ❓ Проблема, которую решает Consistent Hashing:
     * Представим, что у нас есть распределённая система (например, кеши, шардированный key-value store или балансировка нагрузки), и нам нужно распределить ключи (например, "user123", "product42", и т.п.) по множеству серверов / узлов.
     * 🔴 Стандартный подход (наивный):
     * serverIndex = hash(key) % N;  // N — количество серверов
     * Работает быстро.
     * Проблема: если добавим/удалим хотя бы один сервер (N изменится) — все ключи перераспределятся!
     * 👉 Это приводит к перегрузке сети, cache miss, недоступности данных.
     * ✅ Что делает Consistent Hashing:
     * Цель:
     * Обеспечить минимальные изменения в распределении ключей при добавлении или удалении узлов.
     * 🔄 Основная идея:
     * Хэшируем и ключи, и узлы (например, hash("serverA"), hash("serverB"), hash("key42")) и располагаем их на логическом круге (ring).
     * Чтобы найти сервер для ключа:
     * Идём по часовой стрелке, ищем первый сервер с хэшем ≥ хэша ключа (wrap-around, если надо).
     * При добавлении/удалении сервера:
     * Затрагиваются только ближайшие ключи (те, что «переходят» на новый/соседний узел).
     */

    /*
     *  📌 Применения:
     * Distributed cache (например, Memcached, Redis Cluster)
     * Load balancing
     * Distributed hash tables (DHT), P2P-сети (BitTorrent, DynamoDB)
     * Sharded databases
     */

    /*
     * 🔹 Зачем нужны виртуальные узлы в Consistent Hashing?
     *
     * 📌 Проблема:
     * Без виртуальных узлов, при небольшом количестве реальных узлов, распределение ключей по серверам может быть очень неравномерным, потому что:
     * hash() может "скучковать" узлы в одной части кольца,
     * из-за этого один сервер может получить слишком много ключей, а другие — почти ничего.
     * ❗ Даже если серверов много, но они расположены неконтролируемо по хэшу, перекос всё равно возможен.
     */

    private static class VirtualNodeSolution {
        private final TreeMap<Integer, String> ring = new TreeMap<>();
        private final Map<String, List<Integer>> virtualMap = new HashMap<>();
        private final int VIRTUAL_NODES = 100;

        public void addNode(String node) {
            List<Integer> hashes = new ArrayList<>();
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                int hash = getHash(node + "#" + i);
                ring.put(hash, node);
                hashes.add(hash);
            }
            virtualMap.put(node, hashes);
        }

        public void removeNode(String node) {
            List<Integer> hashes = virtualMap.getOrDefault(node, List.of());
            for (int hash : hashes) {
                ring.remove(hash);
            }
            virtualMap.remove(node);
        }

        public String getNode(String key) {
            if (ring.isEmpty()) return null;
            int hash = getHash(key);
            Map.Entry<Integer, String> entry = ring.ceilingEntry(hash);
            if (entry == null) return ring.firstEntry().getValue();
            return entry.getValue();
        }

        private int getHash(String s) {
            int hash = s.hashCode();
            return Integer.toUnsignedLong(hash) > Integer.MAX_VALUE
                ? hash & Integer.MAX_VALUE
                : hash;
        }

    }

    @Test
    void test(){
        var ch = new VirtualNodeSolution();
        ch.addNode("AAA");
        ch.addNode("BBB");
        ch.addNode("CCC");

        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            String node = ch.getNode(UUID.randomUUID().toString());
            counts.put(node, counts.getOrDefault(node, 0) + 1);
        }

        System.out.println("With virtual nodes: " + counts);
        assertEquals(3, counts.size());

        ch.removeNode("B");
        System.out.println("After removing B: " + ch.getNode("key1"));

        System.out.println("All tests passed.");
    }

}
