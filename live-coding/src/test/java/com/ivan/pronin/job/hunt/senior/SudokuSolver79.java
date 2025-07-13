package com.ivan.pronin.job.hunt.senior;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class SudokuSolver79 {

    /*
     * 79. Sudoku Solver
     * Write a program to solve a Sudoku puzzle by filling the empty cells.
     * The Sudoku board is a 9x9 grid filled with digits from 1 to 9,
     * where each digit appears exactly once in each row, column, and 3x3 subgrid.

     * A sudoku solution must satisfy all of the following rules:
     * Each of the digits 1-9 must occur exactly once in each row.
     * Each of the digits 1-9 must occur exactly once in each column.
     * Each of the digits 1-9 must occur exactly once in each of the 9 3x3 sub-boxes of the grid.
     * The '.' character indicates empty cells.
     */
    public void solveSudoku(char[][] board) {
        backtrack(board);
    }

    private boolean backtrack(char[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') continue;

                for (char c = '1'; c <= '9'; c++) {
                    if (isValid(board, i, j, c)) {
                        board[i][j] = c;
                        if (backtrack(board)) return true;
                        board[i][j] = '.'; // backtrack
                    }
                }

                return false; // no valid number found
            }
        }
        return true; // solved
    }

    private boolean isValid(char[][] board, int row, int col, char c) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == c) return false;
            if (board[i][col] == c) return false;
            int blockRow = 3 * (row / 3) + i / 3;
            int blockCol = 3 * (col / 3) + i % 3;
            if (board[blockRow][blockCol] == c) return false;
        }
        return true;
    }

    private final char[][] input = {
        {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
        {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
        {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
        {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
        {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
        {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
        {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
        {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
        {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
    };

    @Test
    public void testBasicSolver() {
        solveSudoku(input);
        assertTrue(isBoardValid(input));
    }

    private boolean isBoardValid(char[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] row = new boolean[9];
            boolean[] col = new boolean[9];
            boolean[] box = new boolean[9];
            for (int j = 0; j < 9; j++) {
                int boxIndex = (i / 3) * 3 + j / 3;

                if (board[i][j] != '.') {
                    int n = board[i][j] - '1';
                    if (row[n]) return false;
                    row[n] = true;
                }

                if (board[j][i] != '.') {
                    int n = board[j][i] - '1';
                    if (col[n]) return false;
                    col[n] = true;
                }

                int r = 3 * (i / 3) + j / 3;
                int c = 3 * (i % 3) + j % 3;
                if (board[r][c] != '.') {
                    int n = board[r][c] - '1';
                    if (box[n]) return false;
                    box[n] = true;
                }
            }
        }
        return true;
    }

}
