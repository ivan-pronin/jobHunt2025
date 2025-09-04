package com.ivan.pronin.job.hunt.senior.google;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 04.09.2025
 */
class ShortestPathFromSourceDijkstraTest {

    @Test
    public void testShortestPaths() {
        int n = 5;
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

        // Build graph: same as example
        graph.get(0).add(new int[]{1, 2});
        graph.get(0).add(new int[]{2, 4});
        graph.get(1).add(new int[]{2, 1});
        graph.get(1).add(new int[]{3, 7});
        graph.get(2).add(new int[]{4, 3});
        graph.get(3).add(new int[]{4, 1});
        // node 4 has no outgoing edges

        int[] expected = {0, 2, 3, 9, 6};
        int[] actual = ShortestPathFromSourceDijkstra.dijkstra(n, graph, 0);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDisconnectedGraph() {
        int n = 4;
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

        // Only one edge: 0 -> 1 (weight 2)
        graph.get(0).add(new int[]{1, 2});

        int[] expected = {0, 2, Integer.MAX_VALUE, Integer.MAX_VALUE};
        int[] actual = ShortestPathFromSourceDijkstra.dijkstra(n, graph, 0);

        assertArrayEquals(expected, actual);
    }

}