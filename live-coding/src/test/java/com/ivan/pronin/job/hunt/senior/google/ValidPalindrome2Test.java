package com.ivan.pronin.job.hunt.senior.google;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 03.09.2025
 */
class ValidPalindrome2Test {

    @Test
    public void testAPalindrome() {
        assertTrue(ValidPalindrome2.canBePalindrome("ac"));
        assertTrue(ValidPalindrome2.canBePalindrome("aca"));
        assertTrue(ValidPalindrome2.canBePalindrome("abca"));
        assertTrue(ValidPalindrome2.canBePalindrome("abcca"));
    }

    @Test
    public void testNotAPalindrome() {
        assertFalse(ValidPalindrome2.canBePalindrome("abc"));
        assertFalse(ValidPalindrome2.canBePalindrome("aabc"));
        assertFalse(ValidPalindrome2.canBePalindrome("acabc"));
        assertFalse(ValidPalindrome2.canBePalindrome("aabbcb"));
    }

}