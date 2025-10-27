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

import lombok.With;

import org.meeuw.math.numbers.NumberOperations;

import static org.meeuw.math.text.ScientificNotation.TIMES_10;

/**
 * Split a double up in 2 numbers: a double approximately 1 (the 'coefficient'), and an integer
 * indicating the order of magnitude (the 'exponent').
 * <p>
 * This is a protected utility. It's not a record, the fields are just access directly, and occasionally modified.
 */

class SplitNumber<N extends Number> {
    @With N coefficient;
    @With int exponent;

    SplitNumber(N coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    @Override
    public String toString() {
        return coefficient + TIMES_10 + TextUtils.superscript(exponent);
    }

    static <N extends Number> SplitNumber<N> split(N in) {
        NumberOperations<N> operations = NumberOperations.of(in);
        return split(operations, in);
    }


    static <N extends Number> SplitNumber<N> split(NumberOperations<N> operations, N in) {

        if (! operations.isFinite(in)) {
            throw new IllegalArgumentException("Not a finite number: " + in);
        }

        boolean negative = operations.signum(in) < 0;
        N coefficient = operations.abs(in);
        int exponent    = 0;
        if (!operations.isZero(coefficient)) {
            // use operations.scaleByPowerOf10?
            while (operations.gte(coefficient, 10)) {
                coefficient = operations.divideInt(coefficient, 10);
                exponent++;
            }

            while (operations.lt(coefficient, 1)) {
                coefficient = operations.multiply(coefficient, 10);
                exponent--;
            }
        }
        if (negative) { // put sign back
            coefficient = operations.negate(coefficient);
        }
        return new SplitNumber<>(coefficient, exponent);
    }

}
