package com.ivan.pronin.job.hunt.senior.google;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 04.09.2025
 */
class BasicCalculator1Test {

    @Test
    public void testCalcI() {
        assertEquals(3, BasicCalculator1.calculate("1 + 2"));
        assertEquals(0, BasicCalculator1.calculate("1 - 1"));
        assertEquals(84, BasicCalculator1.calculate("99 - 15"));
        assertEquals(700, BasicCalculator1.calculate("556 + 144"));
        assertEquals(-8, BasicCalculator1.calculate("1 - (4 + 5)"));
        assertEquals(23, BasicCalculator1.calculate("(1 + (4 + 5 + 2) - 3) + (6 + 8)"));
    }

}