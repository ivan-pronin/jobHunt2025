package com.ivan.pronin.job.hunt.senior.google;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 04.09.2025
 */
class BasicCalculator2Test {

    @Test
    public void testCalcII() {
        assertEquals(7, BasicCalculator2.calculate("3+2*2"));
        assertEquals(-4, BasicCalculator2.calculate("-8+2*2"));
        assertEquals(1, BasicCalculator2.calculate(" 3/2 "));
        assertEquals(5, BasicCalculator2.calculate(" 3+5 / 2 "));
        assertEquals(-21, BasicCalculator2.calculate(" 3 - 5 * 10 / 2  + 1 "));
    }

    @Test
    public void testCalcII2() {
        assertEquals(7, BasicCalculator2.calculate2("3+2*2"));
        assertEquals(-4, BasicCalculator2.calculate2("-8+2*2"));
        assertEquals(1, BasicCalculator2.calculate2(" 3/2 "));
        assertEquals(5, BasicCalculator2.calculate2(" 3+5 / 2 "));
        assertEquals(-21, BasicCalculator2.calculate2(" 3 - 5 * 10 / 2  + 1 "));
    }

}