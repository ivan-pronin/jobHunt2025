package com.ivan.pronin.job.hunt.senior.netflix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 22.07.2025
 */
public class RotateMatrix {

    public void rotate(int[][] matrix) {
        int n = matrix.length;

        // Проходим по слоям матрицы
        for (int layer = 0; layer < n / 2; layer++) {
            int first = layer;
            int last = n - layer - 1;

            for (int i = first; i < last; i++) {
                int offset = i - first;
                int top = matrix[first][i];

                // Перемещаем элементы по часовой стрелке
                matrix[first][i] = matrix[last - offset][first];
                matrix[last - offset][first] = matrix[last][last - offset];
                matrix[last][last - offset] = matrix[i][last];
                matrix[i][last] = top;
            }
        }
    }

    @Test
    public void testRotate() {
        RotateMatrix solution = new RotateMatrix();

        int[][] matrix1 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        solution.rotate(matrix1);
        int[][] expected1 = {
            {7, 4, 1},
            {8, 5, 2},
            {9, 6, 3}
        };
        assertArrayEquals(expected1, matrix1);

        int[][] matrix2 = {
            {1, 2},
            {3, 4}
        };
        solution.rotate(matrix2);
        int[][] expected2 = {
            {3, 1},
            {4, 2}
        };
        assertArrayEquals(expected2, matrix2);

        int[][] matrix3 = {
            {1}
        };
        solution.rotate(matrix3);
        int[][] expected3 = {
            {1}
        };
        assertArrayEquals(expected3, matrix3);
    }

}
