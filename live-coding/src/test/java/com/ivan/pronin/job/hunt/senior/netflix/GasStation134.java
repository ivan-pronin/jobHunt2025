package com.ivan.pronin.job.hunt.senior.netflix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 22.07.2025
 */
public class GasStation134 {

    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int totalTank = 0; // общая разница gas - cost
        int currTank = 0;  // запас топлива с текущей станции
        int startIndex = 0;

        for (int i = 0; i < gas.length; i++) {
            totalTank += gas[i] - cost[i];
            currTank += gas[i] - cost[i];

            // Если текущий запас отрицательный — переносим старт
            if (currTank < 0) {
                startIndex = i + 1;
                currTank = 0;
            }
        }
        return totalTank >= 0 ? startIndex : -1;
    }

    @Test
    public void testExamples() {
        assertEquals(3, canCompleteCircuit(
            new int[]{1, 2, 3, 4, 5},
            new int[]{3, 4, 5, 1, 2}));
        assertEquals(-1, canCompleteCircuit(
            new int[]{2, 3, 4},
            new int[]{3, 4, 3}));
        assertEquals(0, canCompleteCircuit(
            new int[]{5},
            new int[]{4}));
    }

}
