package com.ivan.pronin.job.hunt.senior;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class WordSearch79 {

    /*
     * 79. Word Search
     * Given an m x n grid of characters board and a string word, return true if word exists in the grid.
     * The word can be constructed from letters of sequentially adjacent cells,
     * where adjacent cells are horizontally or vertically neighboring.
     * The same letter cell may not be used more than once.
     * Example:
     * Input: board = [
     * ["A","B","C","E"],
     * ["S","F","C","S"],
     *  ["A","D","E","E"]
     * ], word = "ABCCED"
     * Output: true
     *
     * 📊 Сложность:
     * Time: O(m * n * 4^k) — где m и n — размеры сетки, k — длина слова.
     * Space: O(m * n) — используется стек вызовов для DFS, в худшем случае может хранить все элементы сетки.
     * Примечание: 4^k учитывает все возможные пути для каждого символа слова,
     * где k — длина слова, так как для каждого символа мы можем двигаться в 4 направлениях (вверх, вниз, влево, вправо).
     */
    private int m, n;

    private char[][] board;

    private String word;

    public boolean exist(char[][] board, String word) {
        this.board = board;
        this.word = word;
        m = board.length;
        n = board[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dfs(i, j, 0)) return true;
            }
        }
        return false;
    }

    private boolean dfs(int i, int j, int index) {
        if (index == word.length()) return true;
        if (i < 0 || j < 0 || i >= m || j >= n || board[i][j] != word.charAt(index)) return false;

        char temp = board[i][j];
        board[i][j] = '#'; // mark visited

        boolean found = dfs(i + 1, j, index + 1) ||
            dfs(i - 1, j, index + 1) ||
            dfs(i, j + 1, index + 1) ||
            dfs(i, j - 1, index + 1);

        board[i][j] = temp; // backtrack
        return found;
    }

    private char[][] testBoard = {
        {'A', 'B', 'C', 'E'},
        {'S', 'F', 'C', 'S'},
        {'A', 'D', 'E', 'E'}
    };

    @Test
    public void testWordSearchDFS() {
        assertTrue(exist(testBoard, "ABCCED"));
        assertTrue(exist(testBoard, "SEE"));
        assertFalse(exist(testBoard, "ABCB"));
    }

}
