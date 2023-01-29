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
import org.meeuw.math.numbers.DoubleOperations;

/**
 * Like {@link ConfidenceInterval}, but backed by primitive doubles.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Getter
public class DoubleConfidenceInterval implements Predicate<Double> {

    private final double low;
    private final double high;

    /**
     * @see ConfidenceInterval#of(Number, Number, int)
     */
    public static DoubleConfidenceInterval of(double value, double uncertainty, double interval) {
        double halfRange = Double.isNaN(uncertainty) ? Math.abs(value * UncertainDouble.NaN_EPSILON) : uncertainty * interval;
        return new DoubleConfidenceInterval(value - halfRange, value + halfRange);
    }

    public DoubleConfidenceInterval(double low, double high) {
        this.low = low;
        this.high = high;
    }

    public ConfidenceInterval<Double> asConfidenceInterval() {
        return new ConfidenceInterval<>(DoubleOperations.INSTANCE, low, high);
    }
    public boolean contains(double value) {
        return this.low <= value && value <= this.high;
    }

    @Override
    public boolean test(Double value) {
        return value != null && contains(value);
    }

    @Override
    public String toString() {
        return "[" + low + "," + high + "]";
    }
}
