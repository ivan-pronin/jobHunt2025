package com.ivan.pronin.job.hunt.system;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.ivan.pronin.job.hunt.system.api.LoadBalancer;
import com.ivan.pronin.job.hunt.system.model.BalancingStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 15.07.2025
 */
public class LoadBalancerAlgorithm {

    /*
     * 🧠 Цель задачи: Load Balancer
     * Реализовать структуру, которая:
     * Позволяет регистрировать и удалять серверы (addServer(id), removeServer(id))
     * При вызове getServer() возвращает подходящий сервер согласно выбранной стратегии (например, Round Robin, Random, Weighted и т.д.)
     * ✅ Решение 1: Round Robin Load Balancer
     * 📌 Идея
     * Поддерживаем список доступных серверов
     * Используем круговой индекс (counter) для выбора сервера
     */
    public class RoundRobinLoadBalancer {

        private final List<String> servers = new ArrayList<>();

        private int index = 0;

        public void addServer(String serverId) {
            servers.add(serverId);
        }

        public void removeServer(String serverId) {
            servers.remove(serverId);
            if (index >= servers.size()) {
                index = 0;
            }
        }

        public String getServer() {
            if (servers.isEmpty()) throw new IllegalStateException("No servers available");
            String server = servers.get(index);
            index = (index + 1) % servers.size();
            return server;
        }

    }

    @Test
    public void testRoundRobin() {
        RoundRobinLoadBalancer lb = new RoundRobinLoadBalancer();
        lb.addServer("A");
        lb.addServer("B");
        lb.addServer("C");

        assertEquals("A", lb.getServer());
        assertEquals("B", lb.getServer());
        assertEquals("C", lb.getServer());
        assertEquals("A", lb.getServer());
    }

    public class MultiStrategyLoadBalancer implements LoadBalancer {
        private final BalancingStrategy strategy;
        private final List<Server> servers = new ArrayList<>();
        private final Map<String, Server> idToServer = new HashMap<>();
        private int rrIndex = 0;
        private final Random random = new Random();
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        // For consistent hashing
        private final TreeMap<Integer, Server> hashRing = new TreeMap<>();
        private final int VIRTUAL_NODES = 100;

        public MultiStrategyLoadBalancer(BalancingStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public void addServer(String id, int weight) {
            lock.writeLock().lock();
            try {
                Server server = new Server(id, weight);
                servers.add(server);
                idToServer.put(id, server);

                if (strategy == BalancingStrategy.CONSISTENT_HASHING) {
                    for (int i = 0; i < VIRTUAL_NODES; i++) {
                        int hash = getHash(id + "#VN" + i);
                        hashRing.put(hash, server);
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void removeServer(String id) {
            lock.writeLock().lock();
            try {
                Server server = idToServer.remove(id);
                if (server != null) {
                    servers.remove(server);

                    if (strategy == BalancingStrategy.CONSISTENT_HASHING) {
                        for (int i = 0; i < VIRTUAL_NODES; i++) {
                            int hash = getHash(id + "#VN" + i);
                            hashRing.remove(hash);
                        }
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void reportLoad(String id, int currentLoad) {
            lock.writeLock().lock();
            try {
                Server server = idToServer.get(id);
                if (server != null) server.load = currentLoad;
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public String getServer() {
            lock.readLock().lock();
            try {
                if (servers.isEmpty()) throw new IllegalStateException("No servers available");

                return switch (strategy) {
                    case ROUND_ROBIN -> getRoundRobin();
                    case WEIGHTED -> getWeighted();
                    case LEAST_LOAD -> getLeastLoad();
                    case CONSISTENT_HASHING -> throw new IllegalArgumentException("Use getServer(key) instead");
                };
            } finally {
                lock.readLock().unlock();
            }
        }

        public String getServer(String key) {
            if (strategy != BalancingStrategy.CONSISTENT_HASHING)
                throw new UnsupportedOperationException("getServer(key) only for CONSISTENT_HASHING");

            lock.readLock().lock();
            try {
                if (hashRing.isEmpty()) throw new IllegalStateException("No servers available");

                int hash = getHash(key);
                Map.Entry<Integer, Server> entry = hashRing.ceilingEntry(hash);
                if (entry == null) entry = hashRing.firstEntry();
                return entry.getValue().id;
            } finally {
                lock.readLock().unlock();
            }
        }

        private String getRoundRobin() {
            Server server = servers.get(rrIndex);
            rrIndex = (rrIndex + 1) % servers.size();
            return server.id;
        }

        private String getWeighted() {
            int totalWeight = servers.stream().mapToInt(s -> s.weight).sum();
            int r = random.nextInt(totalWeight);
            int sum = 0;
            for (Server s : servers) {
                sum += s.weight;
                if (r < sum) return s.id;
            }
            return servers.get(0).id;
        }

        private String getLeastLoad() {
            return servers.stream().min(Comparator.comparingInt(s -> s.load)).map(s -> s.id).orElseThrow();
        }

        private int getHash(String key) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] hash = md.digest(key.getBytes());
                return ((hash[0] & 0xFF) << 24) | ((hash[1] & 0xFF) << 16) | ((hash[2] & 0xFF) << 8) | (hash[3] & 0xFF);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static class Server {
            String id;
            int weight;
            int load = 0;

            Server(String id, int weight) {
                this.id = id;
                this.weight = weight;
            }
        }
    }

    @Test
    public void testRoundRobin2() {
        MultiStrategyLoadBalancer lb = new MultiStrategyLoadBalancer(BalancingStrategy.ROUND_ROBIN);
        lb.addServer("A", 1);
        lb.addServer("B", 1);
        lb.addServer("C", 1);

        assertEquals("A", lb.getServer());
        assertEquals("B", lb.getServer());
        assertEquals("C", lb.getServer());
        assertEquals("A", lb.getServer());
    }

    @Test
    public void testWeighted() {
        MultiStrategyLoadBalancer lb = new MultiStrategyLoadBalancer(BalancingStrategy.WEIGHTED);
        lb.addServer("A", 1);
        lb.addServer("B", 3);

        Map<String, Integer> count = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            String server = lb.getServer();
            count.put(server, count.getOrDefault(server, 0) + 1);
        }

        assertTrue(count.get("B") > count.get("A")); // B should get more traffic
    }

    @Test
    public void testLeastLoad() {
        MultiStrategyLoadBalancer lb = new MultiStrategyLoadBalancer(BalancingStrategy.LEAST_LOAD);
        lb.addServer("X", 1);
        lb.addServer("Y", 1);
        lb.reportLoad("X", 5);
        lb.reportLoad("Y", 2);

        assertEquals("Y", lb.getServer());
    }

}
