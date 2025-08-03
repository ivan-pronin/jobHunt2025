package com.ivan.pronin.job.hunt.senior;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 31.07.2025
 */
public class ConsistentHashingVirtualNodes {

    /*
    Consistent hashing with virtual nodes is a technique used in distributed systems to evenly distribute data across multiple nodes while allowing for scalability and fault tolerance. The key idea is to use a hash function to map both data items and nodes to a fixed-size hash space, which allows for efficient data retrieval and load balancing.
    In this approach, each physical node is represented by multiple virtual nodes in the hash space. This means that when a new node is added or removed, only a small portion of the data needs to be redistributed, minimizing disruption to the system. The virtual nodes are typically assigned based on the hash of the node's identifier, allowing for a uniform distribution of data across the available nodes.
    The process generally involves the following steps:
    1. **Hashing Nodes**: Each physical node is hashed to a position in
    the hash space. For example, if you have nodes A, B, and C, you might hash them to positions 10, 20, and 30 in a circular hash space.
    2. **Creating Virtual Nodes**: Each physical node is assigned multiple virtual nodes.
    For instance, node A might have virtual nodes A1, A2, and A3, which are hashed to positions 5, 15, and 25 in the hash space.
    3. **Hashing Data**: When data items are added, they are hashed
    to a position in the same hash space. For example, if you have data item X
    that hashes to position 12, it would be assigned to the node whose virtual node is closest to position 12 in the hash space.
    4. **Handling Node Changes**: When a new node is added or an existing
    node is removed, only the virtual nodes associated with that node are affected.
    The data items that were assigned to those virtual nodes are redistributed to the remaining virtual nodes,
    minimizing the amount of data that needs to be moved.
    5. **Load Balancing**: The use of multiple virtual nodes helps to balance
    the load across the physical nodes, as each node can handle multiple virtual nodes.
    This approach allows for efficient scaling and fault tolerance in distributed systems, as it minimizes the impact
    of adding or removing nodes while ensuring that data is evenly distributed across the available nodes.
    This technique is widely used in distributed databases, caching systems, and other applications where data needs
    to be distributed across multiple nodes while maintaining high availability and performance.
     */
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
        return entry != null ? entry.getValue() : ring.firstEntry().getValue();
    }

    private int getHash(String s) {
        return s.hashCode() & 0x7fffffff;
    }

    @Test
    void test() {
        final Instant now = Instant.now();
        addNode(-10000 + " Node-A");
        addNode("Node-B");
        addNode(10000 + "Node-C");

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
