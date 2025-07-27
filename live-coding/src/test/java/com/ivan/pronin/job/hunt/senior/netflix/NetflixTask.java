package com.ivan.pronin.job.hunt.senior.netflix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Ivan Pronin
 * @since 25.07.2025
 */
public class NetflixTask {

    private final int[] ts = new int[]{2, 7, 4, 3, 1, 1, 20, 50};

    private final int k = 3;

    private int getMostDropOffsTs(int[] ts, int k) {
        Map<Integer, Integer> frequences = new HashMap<>();
        for (int t : ts) {
            frequences.put(t, frequences.getOrDefault(t, 0) + 1);
        }

        final Set<Integer> integers = frequences.keySet();
        Collections.sort(new ArrayList<>(integers));

        int maxFreq = 0;
        int maxT = 0;
        for (int key : integers) {
            int start = key;
            int end = key + k;
            int currSum = 0;
            int temp = maxFreq;
            for (int i = start; i < end; i++) {
                currSum += frequences.getOrDefault(i, 0);
            }
            maxFreq = Math.max(maxFreq, currSum);
            if (temp != maxFreq) {
                maxT = key;
            }
        }
        return maxT;
    }

    @Test
    public void test() {
        int[] ts = new int[]{2, 7, 4, 3, 1, 1, 20, 50};
        int k = 3;

        int result = getMostDropOffsTs(ts, k);
        Assertions.assertEquals(1, result);
    }

}
