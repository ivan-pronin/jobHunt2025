package com.ivan.pronin.job.hunt.senior.binary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 08.08.2025
 */
class DecimalToBinaryTest {

    private final DecimalToBinary decimalToBinary = new DecimalToBinary();

    @Test
    void toBinaryIterative() {
        for (int i = 0; i <= 16; i++){
            assertEquals(Integer.toBinaryString(i), decimalToBinary.toBinaryIterative(i));
        }
    }

    @Test
    void toBinaryRecursive() {
        for (int i = 1; i <= 16; i++){
            assertEquals(Integer.toBinaryString(i), decimalToBinary.toBinaryRecursive(i, ""));
        }
    }

}