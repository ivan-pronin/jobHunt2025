package com.ivan.pronin.job.hunt.senior.netflix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 23.07.2025
 */
public class NetworkDelayTime743 {

    /*
     * 1. LeetCode 743. Network Delay Time
     * Условие:
     * Дано n узлов и m рёбер, где ребро (u, v) с весом w означает, что сигнал от узла u до узла v достигает за w секунд.
     * Нужно определить, сколько времени потребуется, чтобы сигнал достиг всех узлов из начального узла k.
     */

    /**
     * Edge in a directed weighted graph.
     */
    public static class Edge {

        public final int to;

        public final int w;

        public Edge(int to, int w) {
            this.to = to;
            this.w = w;
        }

    }

    /**
     * Computes the time needed for a signal to reach all N nodes starting from node K.
     *
     * @param times edges given as [u, v, w] triples (1-indexed nodes)
     * @param n     total number of nodes labeled 1..n
     * @param k     starting node (1-indexed)
     * @return minimum time for signal to reach all nodes, or -1 if not all reachable
     */
    public static int networkDelayTime(int[][] times, int n, int k) {
        // Build graph as Map<Integer, List<Edge>>
        Map<Integer, List<Edge>> graph = new HashMap<>();
        for (int[] t : times) {
            int u = t[0];
            int v = t[1];
            int w = t[2];
            graph.computeIfAbsent(u, __ -> new ArrayList<>()).add(new Edge(v, w));
        }

        // Distances (1-based). Use long internally to avoid overflow during relax; cast to int at end.
        long[] dist = new long[n + 1];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[k] = 0L;

        // Min-heap by distance.
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
        pq.offer(new long[]{k, 0L});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[0];
            long d = cur[1];
            if (d > dist[u]) continue; // stale

            List<Edge> edges = graph.get(u);
            if (edges == null) continue; // no outgoing edges
            for (Edge e : edges) {
                int v = e.to;
                long nd = d + e.w;
                if (nd < dist[v]) {
                    dist[v] = nd;
                    pq.offer(new long[]{v, nd});
                }
            }
        }

        long max = 0L;
        for (int node = 1; node <= n; node++) {
            if (dist[node] == Long.MAX_VALUE) return -1; // unreachable
            max = Math.max(max, dist[node]);
        }
        return (int) max; // safe if weights + paths fit in int per problem constraints; else change signature.
    }

    @Test
    void sampleFromLeetCode() {
        int[][] times = {
            {2,1,1},
            {2,3,1},
            {3,4,1}
        };
        int n = 4, k = 2;
        assertEquals(2, networkDelayTime(times, n, k));
    }

    @Test
    void unreachableNodeReturnsMinusOne() {
        int[][] times = {
            {1,2,1}
            // node 3 isolated
        };
        int n = 3, k = 1;
        assertEquals(-1, networkDelayTime(times, n, k));
    }

    @Test
    void singleNodeNoEdges() {
        int[][] times = {};
        int n = 1, k = 1;
        assertEquals(0, networkDelayTime(times, n, k));
    }

    @Test
    void multipleEdgesBetweenSameNodesTakesShortest() {
        int[][] times = {
            {1,2,5},
            {1,2,1}, // better edge
            {2,3,2}
        };
        int n = 3, k = 1;
        // shortest: 1->2 (1) + 2->3 (2) = 3, longest among reachable = 3
        assertEquals(3, networkDelayTime(times, n, k));
    }

    @Test
    void longerChain() {
        int[][] times = {
            {1,2,2},
            {2,3,2},
            {1,3,10},
            {3,4,1},
            {2,4,7}
        };
        int n = 4, k = 1;
        // best paths: 1->2 (2), 1->2->3 (4), 1->2->3->4 (5)
        assertEquals(5, networkDelayTime(times, n, k));
    }

}
