package com.ivan.pronin.job.hunt.senior.binary;

/**
 * @author Ivan Pronin
 * @since 08.08.2025
 */
public class DecimalToBinary {

    public String toBinaryIterative(int number) {
        if (number == 0) return "0";
        var sb = new StringBuilder();
        int n = number;
        while (n > 0) {
            int remainder = n % 2;
            sb.append(remainder);
            n = n / 2;
        }
        final String result = sb.reverse().toString();
        System.out.println("Num " + number + " -> " + result);
        return result;
    }

    public String toBinaryRecursive(int number, String result) {
        if (number == 0) return result;
        int remainder = number % 2;
        result = remainder + result;

        return toBinaryRecursive(number / 2, result);
    }

}
