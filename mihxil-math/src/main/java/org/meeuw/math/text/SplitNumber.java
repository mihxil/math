/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.text;

/**
 * Split a double up in 2 numbers: a double approximately 1 (the 'coefficient'), and an integer
 * indicating the order of magnitude (the 'exponent').
 * <p>
 * This is a protected utility. It's not a record, the fields are just access directly, and occasionally modified.
 */

class SplitNumber {
    double coefficient;
    int  exponent;

    SplitNumber(double coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    @Override
    public String toString() {
        return coefficient + UncertainDoubleFormat.TIMES_10 + TextUtils.superscript(exponent);
    }

    static SplitNumber split(double in) {
        if (Double.isInfinite(in)) {
            throw new IllegalArgumentException("" + in);
        }
        boolean negative = in < 0;
        double coefficient = Math.abs(in);
        int exponent    = 0;
        if (coefficient != 0) {
            while (coefficient >= 10) {
                coefficient /= 10;
                exponent++;
            }
            while (coefficient < 1) {
                coefficient *= 10;
                exponent--;
            }
        }
        if (negative) { // put sign back
            coefficient *= -1;
        }
        return new SplitNumber(coefficient, exponent);
    }

}
