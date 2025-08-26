package com.ivan.pronin.job.hunt.senior.recursion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 08.08.2025
 */
class SumOfNaturalNumbersTest {

    private final SumOfNaturalNumbers sumOfNaturalNumbers = new SumOfNaturalNumbers();

    @Test
    void getSum() {
        assertEquals(15, sumOfNaturalNumbers.getSum(5));
        assertEquals(55, sumOfNaturalNumbers.getSum(10));
    }

}