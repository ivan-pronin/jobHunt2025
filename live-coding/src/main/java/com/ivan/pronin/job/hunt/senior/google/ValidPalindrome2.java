package com.ivan.pronin.job.hunt.senior.google;

/**
 * @author Ivan Pronin
 * @since 03.09.2025
 *
 * "Given a string, return true if it can become a palindrome by deleting ≤1 char.
 * Example: "abca" → true ("aba" or "aca")."
 */
public class ValidPalindrome2 {

    public static boolean canBePalindrome(String input){
        if (null == input || input.isEmpty()) return false;
        if (input.length() == 2) return true;
        if (isPalindrome(input)) return true;

        for (int i =0; i< input.length(); i++){
            StringBuilder stringBuilder = new StringBuilder(input);
            stringBuilder.deleteCharAt(i);
            if (isPalindrome(stringBuilder.toString())) return true;
        }
        return false;
    }

    private static boolean isPalindrome(String string) {
        int l = 0;
        int r = string.length() - 1;
        while (l <= r){
            if (string.charAt(l) != string.charAt(r)) return false;
            l++;
            r--;
        }
        return true;
    }

}
