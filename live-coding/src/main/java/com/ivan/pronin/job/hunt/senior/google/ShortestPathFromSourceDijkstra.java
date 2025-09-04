package com.ivan.pronin.job.hunt.senior.google;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author Ivan Pronin
 * @since 04.09.2025
 */
public class ShortestPathFromSourceDijkstra {

    public static int[] dijkstra(int n, List<List<int[]>> graph, int source) {
        int[] distance = new int[n];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[source] = 0;
        PriorityQueue<int[]> nodes = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        nodes.offer(new int[]{source, 0});

        while (!nodes.isEmpty()) {
            int[] node = nodes.poll();
            for (int[] nei : graph.get(node[0])) {
                int newDistance = distance[node[0]] + nei[1];
                if (newDistance < distance[nei[0]]) {
                    distance[nei[0]] = newDistance;
                    nodes.offer(nei);
                }
            }
        }
        return distance;
    }

}
