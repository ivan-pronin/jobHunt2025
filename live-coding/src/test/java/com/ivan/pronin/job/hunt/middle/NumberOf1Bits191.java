package com.ivan.pronin.job.hunt.middle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class NumberOf1Bits191 {

    /*
     * 191. Number of 1 Bits
     * Write a function that takes an unsigned integer and returns the number of '1' bits it has (also known as the Hamming weight).
     * Example 1:
     * Input: n = 00000000000000000000000000001011
     * Output: 3
     * Example 2:
     * Input: n = 00000000000000000000000010000000
     * Output: 1
     *
     *
     * 📊 Сложность:
     * Time: O(1) — количество итераций фиксировано (32 бита).
     * Space: O(1) — используем только несколько переменных для подсчета.
     */
    public static int hammingWeight(int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            count += (n & 1);  // если последний бит = 1, прибавим
            n >>>= 1;          // логический сдвиг вправо (беззнаковый)
        }
        return count;
    }

    @Test
    void testZero() {
        assertEquals(0, NumberOf1Bits191.hammingWeight(0));
    }

    @Test
    void testAllOnes() {
        assertEquals(32, NumberOf1Bits191.hammingWeight(-1)); // в 32-битах -1 = 32 единицы
    }

    @Test
    void testSingleBit() {
        assertEquals(1, NumberOf1Bits191.hammingWeight(1));
        assertEquals(1, NumberOf1Bits191.hammingWeight(0b10000000));
    }

    @Test
    void testMultipleBits() {
        assertEquals(3, NumberOf1Bits191.hammingWeight(0b1011)); // 3 единицы
    }

    @Test
    void testMaxInt() {
        assertEquals(31, NumberOf1Bits191.hammingWeight(Integer.MAX_VALUE)); // 2^31 - 1
    }

    @Test
    void testNegativeNumber() {
        assertEquals(1, NumberOf1Bits191.hammingWeight(Integer.MIN_VALUE)); // только старший бит
    }

}
