package com.ivan.pronin.job.hunt.senior.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ivan Pronin
 * @since 04.09.2025
 *
 * ✅ Task Definition: N-Queens Problem (LeetCode 51)
 * Place n queens on an n x n chessboard so that no two queens attack each other. Return all distinct solutions.
 *
 * Rules:
 * A queen can attack horizontally, vertically, and diagonally.
 * You must place exactly one queen per row and column, with no attacks.
 */
public class NQueens {

    private int queens = 0;

    public List<List<String>> solveNQueens(int n) {
        queens = n;
        char[][] board = new char[n][n];
        for (char[] row : board) {
            Arrays.fill(row, '.');
        }
        Set<Integer> columns = new HashSet<>();
        Set<Integer> diag1 = new HashSet<>();
        Set<Integer> diag2 = new HashSet<>();

        List<List<String>> result = new ArrayList<>();

        dfs(0, columns, diag1, diag2, board, result);

        return result;
    }

    private void dfs(int row, Set<Integer> columns, Set<Integer> diag1, Set<Integer> diag2, char[][] board, List<List<String>> result) {
        if (row == queens) {
            result.add(toString(board));
        }

        for (int col = 0; col < queens; col++) {
            if (columns.contains(col) || diag1.contains(row - col) || diag2.contains(row + col)) continue;

            board[row][col] = 'Q';
            columns.add(col);
            diag1.add(row - col);
            diag2.add(row + col);

            dfs(row + 1, columns, diag1, diag2, board, result);

            board[row][col] = '.';
            columns.remove(col);
            diag1.remove(row - col);
            diag2.remove(row + col);
        }

    }

    private List<String> toString(char[][] board) {
        List<String> result = new ArrayList<>();
        for (char[] c : board) {
            result.add(new String(c));
        }
        return result;
    }

}
