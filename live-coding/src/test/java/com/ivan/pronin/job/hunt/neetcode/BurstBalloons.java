package com.ivan.pronin.job.hunt.neetcode;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 29.07.2025
 */
public class BurstBalloons {

    private static class SolutionBruteForce {

        public int maxCoins(int[] nums) {
            int[] newNums = new int[nums.length + 2];
            newNums[0] = newNums[nums.length + 1] = 1;
            for (int i = 0; i < nums.length; i++) {
                newNums[i + 1] = nums[i];
            }

            return dfs(newNums);
        }

        public int dfs(int[] nums) {
            if (nums.length == 2) {
                return 0;
            }

            int maxCoins = 0;
            for (int i = 1; i < nums.length - 1; i++) {
                int coins = nums[i - 1] * nums[i] * nums[i + 1];
                int[] newNums = new int[nums.length - 1];
                for (int j = 0, k = 0; j < nums.length; j++) {
                    if (j != i) {
                        newNums[k++] = nums[j];
                    }
                }
                coins += dfs(newNums);
                System.out.println("Coins for bursting balloon at index " + i + ": " + coins + " with newNums: " + java.util.Arrays.toString(newNums));
                maxCoins = Math.max(maxCoins, coins);
            }
            return maxCoins;
        }

    }

    private static class BruteForce2 {

        public int maxCoins(int[] nums) {
            if (null == nums || nums.length == 0) return 0;

            List<Integer> numsList = new ArrayList<>();
            for (int i : nums) {
                numsList.add(i);
            }

            return findMaxSum(numsList);
        }

        private int findMaxSum(List<Integer> nums) {
            if (nums.size() == 1) return nums.get(0);

            List<Integer> tmp = new ArrayList<>(nums);
            int maxSum = 0;
            for (int j = 0; j < tmp.size(); j++) {
                List<Integer> tmp2 = new ArrayList<>(tmp);
                int currProduct = calcProduct(j, tmp2);
                tmp2.remove(j);
                currProduct = currProduct + findMaxSum(tmp2);
                maxSum = Math.max(maxSum, currProduct);
                System.out.println("Burst for list: " + tmp + " currProduct = " + currProduct + " max: " + maxSum);
            }
            return maxSum;
        }

        private int calcProduct(int idx, List<Integer> nums) {
            if (nums.isEmpty()) {
                return 0;
            }
            int product = 1;
            for (int i = idx - 1; i <= idx + 1; i++) {
                if (i < 0 || i >= nums.size()) {
                    continue;
                }
                product = product * nums.get(i);
            }
            return product;
        }

    }

    @Test
    void testBurstBalloons() {
        SolutionBruteForce solution = new SolutionBruteForce();
        int[] nums = {4, 2, 3, 7};
        int result = solution.maxCoins(nums);
        System.out.println("Max coins: " + result);
        assertEquals(143, solution.maxCoins(nums));
    }

    @Test
    void testBurstBalloons2() {
        var solution = new BruteForce2();
        int[] nums = {4, 2, 3, 7};
        int result = solution.maxCoins(nums);
        System.out.println("Max coins: " + result);
        assertEquals(143, solution.maxCoins(nums));
    }

}
