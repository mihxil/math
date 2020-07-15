package org.meeuw.math;

/**
 * Split a double up in 2 numbers: a double approximately 1 (the 'coefficent'), and an integer
 * indicating the order of magnitude (the 'exponent').
 */

public class SplitNumber {
    public double coefficient;
    public int   exponent;

    public SplitNumber(double coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }


    @Override
    public String toString() {
        return coefficient + "\u00B710" + Utils.superscript(exponent);
        //return coefficient + "E" + exponent;
    }

    public static SplitNumber split(double in) {
        boolean negative = in < 0;
        double coefficient = Math.abs(in);
        int exponent    = 0;
        while (coefficient > 10) {
            coefficient /=10;
            exponent++;
        }
        while (coefficient > 0 && coefficient < 1) {
            coefficient *=10;
            exponent--;
        }
        if (negative) coefficient *= -1;
        return new SplitNumber(coefficient, exponent);
    }

}
