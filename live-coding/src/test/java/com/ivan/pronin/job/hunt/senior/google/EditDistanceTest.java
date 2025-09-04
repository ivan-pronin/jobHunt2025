package com.ivan.pronin.job.hunt.senior.google;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 04.09.2025
 */
class EditDistanceTest {

    @Test
    public void testExamples() {
        assertEquals(3, EditDistance.minDistance("horse", "ros"));
        assertEquals(5, EditDistance.minDistance("intention", "execution"));
    }

    @Test
    public void testEdgeCases() {
        assertEquals(0, EditDistance.minDistance("", ""));
        assertEquals(5, EditDistance.minDistance("abcde", ""));
        assertEquals(4, EditDistance.minDistance("", "love"));
    }

}