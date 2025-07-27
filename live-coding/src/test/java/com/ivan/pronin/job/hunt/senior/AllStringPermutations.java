package com.ivan.pronin.job.hunt.senior;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 21.07.2025
 */
public class AllStringPermutations {

    /*
     * Generate all permutations of a string using backtracking.
     * @param str the input string
     * @return a list of all permutations
     */

    public static int countPermutations(String str) {
        Set<String> result = new HashSet<>();
        permute("", str, result);
        return result.size();
    }

    private static void permute(String prefix, String remaining, Set<String> result) {
        if (remaining.isEmpty()) {
            result.add(prefix);
            return;
        }

        for (int i = 0; i < remaining.length(); i++) {
            permute(prefix + remaining.charAt(i),
                remaining.substring(0, i) + remaining.substring(i + 1),
                result);
        }
    }

    @Test
    public void testBasicCases() {
        assertEquals(6, countPermutations("ABC"));
        assertEquals(3, countPermutations("AAB"));
        assertEquals(1, countPermutations("AAA"));
    }

}
