package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import java.util.function.Predicate;

import org.meeuw.math.abstractalgebra.NumberElement;

/**
 * A number with an uncertainty {@link #getUncertainty()}
 *
 * Also defines scalar operations.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainNumber<T extends UncertainNumber<T>> extends NumberElement<T> {

    double EXACT = 0d;

    @Override
    double doubleValue();

    double getUncertainty();

    default boolean isExact() {
        return getUncertainty() == EXACT;
    }

    default T dividedBy(double divisor) {
        return times(1d / divisor);
    }

    T plus(double summand);

    default T minus(double subtrahend) {
        return plus(-1d * subtrahend);
    }

    /**
     * Creates a new uncertain number, combining this one with another one.
     */
    T combined(T m);

    /**
     * Creates a new {@link UncertainNumber} representing a multiple of this one.
     */
    T times(double multiplier);

    default boolean equals(UncertainNumber<?> value, double interval) {
        return Range.of(doubleValue(), getUncertainty(), interval).contains(value.doubleValue())
            ||  Range.of(value.doubleValue(), value.getUncertainty(), interval).contains(doubleValue());
    }

    @Getter
    class Range implements Predicate<Double> {
        private final double low;
        private final double high;

        public static Range of(double value, double uncertainty, double interval) {
            double halfRange = uncertainty * interval;
            return new Range(value - halfRange, value + halfRange);
        }

        public Range(double low, double high) {
            this.low = low;
            this.high = high;
        }

        public boolean contains(double value) {
            return this.low <= value && this.high >= value;
        }
        @Override
        public boolean test(Double value) {
            return value != null && contains(value);
        }
    }

}
