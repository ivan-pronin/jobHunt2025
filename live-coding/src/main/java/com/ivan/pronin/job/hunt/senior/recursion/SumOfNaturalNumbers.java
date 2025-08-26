package com.ivan.pronin.job.hunt.senior.recursion;

/**
 * @author Ivan Pronin
 * @since 08.08.2025
 */
public class SumOfNaturalNumbers {

    public int getSum(int number){
        if (number == 1) return number;
        return number + getSum(number - 1);
    }

}
