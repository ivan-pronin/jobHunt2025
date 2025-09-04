package com.ivan.pronin.job.hunt.senior.google;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 04.09.2025
 */
class NQueensTest {

    @Test
    public void testNQueens4() {
        NQueens solver = new NQueens();
        List<List<String>> results = solver.solveNQueens(4);
        results.forEach(System.out::println);
        assertEquals(2, results.size());

        for (List<String> sol : results) {
            assertEquals(4, sol.size());
            for (String row : sol) {
                assertEquals(4, row.length());
                assertEquals(1, row.chars().filter(c -> c == 'Q').count());
            }
        }
    }

    @Test
    public void testNQueens1() {
        NQueens solver = new NQueens();
        List<List<String>> results = solver.solveNQueens(1);
        assertEquals(1, results.size());
        assertEquals("Q", results.get(0).get(0));
    }

    @Test
    public void testNQueens2() {
        NQueens solver = new NQueens();
        List<List<String>> results = solver.solveNQueens(2);
        assertEquals(0, results.size());
    }

    @Test
    public void testNQueens3() {
        NQueens solver = new NQueens();
        List<List<String>> results = solver.solveNQueens(3);
        assertEquals(0, results.size());
    }

}