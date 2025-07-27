package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class CourseSchedule207 {

    /*
     * 207. Course Schedule
     * There are n courses you have to take, labeled from 0 to n - 1.
     * You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that
     * you must take course bi first if you want to take course ai.
     * Return true if you can finish all courses. Otherwise, return false.
     * Example:
     * Input: numCourses = 2, prerequisites = [[1,0]]
     * Output: true
     * Explanation: There are a total of 2 courses to take. To take course 1, you should have finished course 0. So it is possible.
     * 📊 Сложность:
     * Time: O(V + E) — где V — количество вершин (курсов), E — количество рёбер (предварительных условий).
     * Space: O(V + E) — используем память для хранения графа и стека/очереди для обхода.
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[numCourses];

        for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());

        for (int[] pre : prerequisites) {
            graph.get(pre[1]).add(pre[0]);
            inDegree[pre[0]]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        int visited = 0;

        while (!queue.isEmpty()) {
            int course = queue.poll();
            visited++;

            for (int neighbor : graph.get(course)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) queue.offer(neighbor);
            }
        }

        return visited == numCourses;
    }

    @Test
    public void testBFS() {
        assertTrue(canFinish(4, new int[][]{{1, 0}, {2, 0}, {3, 1}, {3, 2}}));
        assertTrue(canFinish(2, new int[][]{{1, 0}}));
        assertFalse(canFinish(2, new int[][]{{1, 0}, {0, 1}}));
    }

}
