package org.meeuw.math.text;

import org.meeuw.math.Utils;

/**
 * Split a double up in 2 numbers: a double approximately 1 (the 'coefficient'), and an integer
 * indicating the order of magnitude (the 'exponent').
 */

class SplitNumber {
    public double coefficient;
    public int   exponent;

    public SplitNumber(double coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }


    @Override
    public String toString() {
        return coefficient + UncertainDoubleFormat.TIMES_10 + TextUtils.superscript(exponent);
    }

    static SplitNumber split(double in) {
        if (Double.isInfinite(in)) {
            return new SplitNumber(in == Double.NEGATIVE_INFINITY ? -1 : 1, Utils.log10(Integer.MAX_VALUE));
        }
        boolean negative = in < 0;
        double coefficient = Math.abs(in);
        int exponent    = 0;
        while (coefficient > 10) {
            coefficient /= 10;
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
