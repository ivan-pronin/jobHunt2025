package com.ivan.pronin.job.hunt.senior;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ivan.pronin.job.hunt.senior.model.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 12.07.2025
 */
public class CloneGraph133 {
    /*
     * 133. Clone Graph
     * Given a reference of a node in a connected undirected graph, return a deep copy (clone) of the graph.
     * Each node in the graph contains a value and a list of its neighbors.
     * Example:
     * Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
     * Output: [[2,4],[1,3],[2,4],[1,3]]
     *
     * 📊 Сложность:
     * Time: O(V + E) — где V — количество вершин, E — количество рёбер в графе.
     * Space: O(V) — используем хэш-таблицу для хранения клонированных узлов.
     */

    private static class DfsSolution {

        private Map<Node, Node> visited = new HashMap<>();

        public Node cloneGraph(Node node) {
            if (node == null) return null;

            if (visited.containsKey(node)) {
                return visited.get(node);
            }

            Node clone = new Node(node.val);
            visited.put(node, clone);

            for (Node neighbor : node.neighbors) {
                clone.neighbors.add(cloneGraph(neighbor));
            }

            return clone;
        }

    }

    private static class BfsSolution {
        public Node cloneGraph(Node node) {
            if (node == null) return null;

            Map<Node, Node> visited = new HashMap<>();
            Node clone = new Node(node.val);
            visited.put(node, clone);

            java.util.Queue<Node> queue = new java.util.LinkedList<>();
            queue.offer(node);

            while (!queue.isEmpty()) {
                Node current = queue.poll();

                for (Node neighbor : current.neighbors) {
                    if (!visited.containsKey(neighbor)) {
                        visited.put(neighbor, new Node(neighbor.val));
                        queue.offer(neighbor);
                    }
                    visited.get(current).neighbors.add(visited.get(neighbor));
                }
            }

            return clone;
        }
    }

    private Node buildSimpleGraph() {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);

        node1.neighbors = Arrays.asList(node2, node4);
        node2.neighbors = Arrays.asList(node1, node3);
        node3.neighbors = Arrays.asList(node2, node4);
        node4.neighbors = Arrays.asList(node1, node3);

        return node1;
    }

    private boolean areGraphsEqual(Node n1, Node n2, Set<Integer> visited) {
        if (n1 == null || n2 == null || n1.val != n2.val) return false;
        if (visited.contains(n1.val)) return true;

        visited.add(n1.val);
        if (n1.neighbors.size() != n2.neighbors.size()) return false;

        for (int i = 0; i < n1.neighbors.size(); i++) {
            if (!areGraphsEqual(n1.neighbors.get(i), n2.neighbors.get(i), visited)) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testDFSClone() {
        Node original = buildSimpleGraph();
        var solution = new DfsSolution();
        Node clone = solution.cloneGraph(original);
        assertNotSame(original, clone);
        assertTrue(areGraphsEqual(original, clone, new HashSet<>()));
    }

    @Test
    public void testBFSClone() {
        Node original = buildSimpleGraph();
        var solution = new BfsSolution();
        Node clone = solution.cloneGraph(original);
        assertNotSame(original, clone);
        assertTrue(areGraphsEqual(original, clone, new HashSet<>()));
    }

    @Test
    public void testNull() {
        var solution = new DfsSolution();
        assertNull(solution.cloneGraph(null));
    }

}
