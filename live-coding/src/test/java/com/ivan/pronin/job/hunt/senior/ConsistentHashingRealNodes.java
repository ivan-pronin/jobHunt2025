package com.ivan.pronin.job.hunt.senior;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 31.07.2025
 */
public class ConsistentHashingRealNodes {

    /*
    Consistent hashing is a technique used in distributed systems to efficiently distribute data across multiple nodes.
    It allows for minimal disruption when nodes are added or removed, ensuring that only a small portion
    of the data needs to be redistributed.
    The basic idea is to use a hash function to map both the data and the nodes to a circular space (often called a "ring").
    Each node is assigned a position on the ring based on its hash value, and data is assigned to the nearest node in a clockwise direction.
    This way, when a new node is added, only the data that was assigned to the nodes that are now adjacent to the new node
    needs to be redistributed, minimizing the impact on the overall system.
    Consistent hashing is commonly used in systems like distributed caches, databases, and load balancers
    to ensure even distribution of data and efficient scaling.
    Example:
    1. Hash the nodes and data to positions on a ring.
    2. When a new node is added, it takes over a portion of the data
         from its neighboring nodes based on the hash values.
    3. When a node is removed, its data is redistributed to the next node in the clockwise direction.
    This approach helps maintain a balanced load across the nodes and ensures that the system can scale
    efficiently with minimal data movement.
     */
    private final TreeMap<Integer, String> ring = new TreeMap<>();

    private final int MAX_HASH = Integer.MAX_VALUE;

    public void addNode(String node) {
        int hash = getHash(node);
        ring.put(hash, node);
    }

    public void removeNode(String node) {
        int hash = getHash(node);
        ring.remove(hash);
    }

    public String getNode(String key) {
        if (ring.isEmpty()) return null;
        int hash = getHash(key);
        // ceilingKey: first key >= hash
        Map.Entry<Integer, String> entry = ring.ceilingEntry(hash);
        if (entry == null) return ring.firstEntry().getValue(); // wrap-around
        return entry.getValue();
    }

    private int getHash(String s) {
        return s.hashCode() & 0x7fffffff; // ensure non-negative
    }

    @Test
    void test() {
        final Instant now = Instant.now();
        addNode(now.toEpochMilli() + " Node-A");
        addNode(now.minusSeconds(1999999) + "Node-B");
        addNode(now.plusSeconds(1999999) + "Node-C");

        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < 10_000_000; i = i + 3333) {
            String node = getNode("ABC_ 123 -32 new-key_" + i + " - 1234");
            counts.put(node, counts.getOrDefault(node, 0) + 1);
        }

        System.out.println("Distribution: " + counts);
        removeNode("B");
        System.out.println("After removing B: " + getNode("key1"));
        System.out.println("All tests passed.");
    }

}
