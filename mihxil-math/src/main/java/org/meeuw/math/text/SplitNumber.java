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

import java.util.Optional;

import org.meeuw.math.exceptions.NotFiniteException;
import org.meeuw.math.numbers.NumberOperations;

import static org.meeuw.math.text.ScientificNotation.TIMES_10;

/**
 * The result of a split of number of type {@code N} in 2 parts: a number of (of type {@code N}) approximately 1 (the 'coefficient'), and an integer
 * indicating the order of magnitude (the 'exponent').
 * <p>
 * It is e.g. used when constructing {@link ScientificNotation scientific notation}
 * <p>
 * This is a protected utility. It's not a record, the fields are just access directly, and occasionally modified.
 *
 * @param <N> The type of the number. E.g. a {@link Double}
 */

public class SplitNumber<N extends Number> {
    public @With N coefficient;
    public @With int exponent;

    SplitNumber(N coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    @Override
    public String toString() {
        return coefficient + TIMES_10 + TextUtils.superscript(exponent);
    }

    public static <N extends Number> Optional<SplitNumber<N>> split(N in) {
        NumberOperations<N> operations = NumberOperations.of(in);
        return split(operations, in);
    }


    /**
     * Created {@link SplitNumber} from given {@link Number}
     *
     * @param operations To allow for generic operation specify the {@link NumberOperations} instance to use
     * @throws NotFiniteException if the incoming number is not finite
     * @return An optional of {@link SplitNumber}, empty if the incoming number is exactly {@link NumberOperations#isZero(Number) zero}
     */

    static <N extends Number> Optional<SplitNumber<N>> split(
        NumberOperations<N> operations,
        N in) {

        if (! operations.isFinite(in)) {
            throw new NotFiniteException("Not a finite number: " + in);
        }
        if (operations.isZero(in)) {
            return Optional.empty();
        }

        boolean negative = operations.signum(in) < 0;
        N coefficient = operations.abs(in);
        int exponent    = 0;
        if (!operations.isZero(coefficient)) {
            // use operations.scaleByPowerOf10?
            while (operations.gte(coefficient, 10)) {
                coefficient = operations.scaleByPowerOfTenExact(coefficient, -1);
                exponent++;
            }

            while (operations.lt(coefficient, 1)) {
                coefficient = operations.scaleByPowerOfTenExact(coefficient, 1);
                exponent--;
            }
        }
        if (negative) { // put sign back
            coefficient = operations.negate(coefficient);
        }
        return Optional.of(new SplitNumber<>(coefficient, exponent));
    }

}
