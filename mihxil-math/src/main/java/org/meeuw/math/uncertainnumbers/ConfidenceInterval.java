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
package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import java.util.function.Predicate;

import org.meeuw.math.numbers.NumberOperations;

/**
 * A confidence interval is just a range around an uncertain {@link Number}, in which the actual value may confidently be.
 * <p>
 * The simplest version of this is just a symmetric interval, an equal number of {@link UncertainNumber#getUncertainty()}  lower and higher around the best guess ({@link UncertainNumber#getValue()}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @see UncertainNumber
 */
@Getter
public class ConfidenceInterval<N extends Number> implements Predicate<N> {

    private final N low;
    private final N high;
    private final Predicate<N> predicate;

    /**
     * Creates a {@link ConfidenceInterval}
     * @param value the central value of the interval
     * @param uncertainty the associated uncertainty
     * @param intervalSize the  {@code uncertainty} is multiplied by this, to get (half the) width of the entire interval.
     */
    public static <N extends Number> ConfidenceInterval<N> of(N value, N uncertainty, double intervalSize) {
        final NumberOperations<N> operations = NumberOperations.of(value);
        final N halfRange = operations.abs(operations.multiplyPrimitiveDouble(uncertainty, intervalSize).getValue());
        return new ConfidenceInterval<>(operations,
            operations.minus(value, halfRange),
            operations.add(value, halfRange)
        );
    }

    ConfidenceInterval(NumberOperations<N> op, N low, N high) {
        this.low = low;
        this.high = high;
        this.predicate = (v) -> op.gte(v, low) && op.lte(v, high);
    }

    public boolean contains(N value) {
        return predicate.test(value);
    }

    @Override
    public boolean test(N value) {
        return value != null && contains(value);
    }

    @Override
    public String toString() {
        return "[" + low + ',' + high + ']';
    }

}
