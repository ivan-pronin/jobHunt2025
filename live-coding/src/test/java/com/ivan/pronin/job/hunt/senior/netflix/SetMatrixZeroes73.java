package com.ivan.pronin.job.hunt.senior.netflix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 22.07.2025
 */
public class SetMatrixZeroes73 {

    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        boolean[] rows = new boolean[m];
        boolean[] cols = new boolean[n];

        // Проходим по матрице и отмечаем строки и столбцы, которые должны быть обнулены
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    rows[i] = true;
                    cols[j] = true;
                }
            }
        }

        // Обнуляем строки
        for (int i = 0; i < m; i++) {
            if (rows[i]) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = 0;
                }
            }
        }

        // Обнуляем столбцы
        for (int j = 0; j < n; j++) {
            if (cols[j]) {
                for (int i = 0; i < m; i++) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public void setZeroesInPlace(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        boolean firstRowZero = false;
        boolean firstColZero = false;

        // Проверяем первый столбец на наличие нулей
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) {
                firstColZero = true;
                break;
            }
        }

        // Проверяем первый ряд на наличие нулей
        for (int j = 0; j < n; j++) {
            if (matrix[0][j] == 0) {
                firstRowZero = true;
                break;
            }
        }

        // Отмечаем строки и столбцы, которые нужно обнулить
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;  // Отметить первый элемент строки
                    matrix[0][j] = 0;  // Отметить первый элемент столбца
                }
            }
        }

        // Обнуляем ячейки, основываясь на первом ряду и первом столбце
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        // Обрабатываем первый ряд
        if (firstRowZero) {
            for (int j = 0; j < n; j++) {
                matrix[0][j] = 0;
            }
        }

        // Обрабатываем первый столбец
        if (firstColZero) {
            for (int i = 0; i < m; i++) {
                matrix[i][0] = 0;
            }
        }
    }

    @Test
    public void testSetZeroes() {
        int[][] matrix1 = {
            {1, 2, 3},
            {4, 0, 6},
            {7, 8, 9}
        };
        setZeroesInPlace(matrix1);
        int[][] expected1 = {
            {1, 0, 3},
            {0, 0, 0},
            {7, 0, 9}
        };
        assertArrayEquals(expected1, matrix1);

        int[][] matrix2 = {
            {0, 1},
            {1, 1}
        };
        setZeroesInPlace(matrix2);
        int[][] expected2 = {
            {0, 0},
            {0, 1}
        };
        assertArrayEquals(expected2, matrix2);

        int[][] matrix3 = {
            {1, 2},
            {3, 4}
        };
        setZeroesInPlace(matrix3);
        int[][] expected3 = {
            {1, 2},
            {3, 4}
        };
        assertArrayEquals(expected3, matrix3);
    }

}
