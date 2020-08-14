package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import java.util.function.Predicate;

import org.meeuw.math.abstractalgebra.*;

/**
 * A number with an uncertainty {@link #getUncertainty()}
 *
 * Also defines scalar operations.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainNumber extends NumberElement<UncertainNumber>  {

    double EXACT = 0d;

    @Override
    double doubleValue();

    double getUncertainty();

    default boolean isExact() {
        return getUncertainty() == EXACT;
    }

    default UncertainNumber dividedBy(double divisor) {
        return times(1d / divisor);
    }


    default UncertainNumber plus(double summand) {
        return new ImmutableUncertainNumber(summand + doubleValue(), getUncertainty());
    }

    default UncertainNumber minus(double subtrahend) {
        return plus(-1d * subtrahend);
    }

    /**
     * Creates a new uncertain number, combining this one with another one.
     */
    default UncertainNumber combined(UncertainNumber m) {
        double u = getUncertainty();
        double mu = m.getUncertainty();
        double weight = 1d / (u * u);
        double mweight = 1d / (mu * mu);
        double value = (doubleValue() * weight + m.doubleValue() * mweight) / (weight + mweight);

        // I'm not absolutely sure about this:
        double uncertaintity = 1d/ Math.sqrt((weight + mweight));
        return new ImmutableUncertainNumber(value, uncertaintity);
    }

    /**
     * Creates a new {@link UncertainNumber} representing a multiple of this one.
     */
    default UncertainNumber times(double multiplier) {
        return new ImmutableUncertainNumber(multiplier * doubleValue(),
            Math.abs(multiplier) * getUncertainty());
    }


    default UncertainNumber times(UncertainNumber multiplier) {
        double u = getUncertainty() / doubleValue();
        double mu = multiplier.getUncertainty() / multiplier.doubleValue();
        double newValue = doubleValue() * multiplier.doubleValue();
        return new ImmutableUncertainNumber(
            newValue,
            Math.abs(newValue) * Math.sqrt( (u * u)  + (mu * mu))
        );
    }

    default UncertainNumber pow(int exponent) {
        return new ImmutableUncertainNumber(Math.pow(doubleValue(), exponent),
            Math.abs(exponent) * Math.pow(doubleValue(), exponent -1) * getUncertainty());
    }

    default UncertainNumber plus(UncertainNumber summand) {
        double u = getUncertainty();
        double mu = summand.getUncertainty();
        return new ImmutableUncertainNumber(
            doubleValue() + summand.doubleValue(),
            Math.sqrt(u * u + mu * mu));

    }

    default boolean equals(Object value, double sds) {
        if (this == value) return true;
        if (! (value instanceof UncertainNumber)) {
            return false;
        }
        UncertainNumber other = (UncertainNumber) value;
        return getConfidenceInterval(sds).contains(other.doubleValue())
            ||  other.getConfidenceInterval(sds).contains(doubleValue());
    }

    default ConfidenceInterval getConfidenceInterval(double sds) {
        return ConfidenceInterval.of(doubleValue(), getUncertainty(), sds);
    }

    @Getter
    class ConfidenceInterval implements Predicate<Double> {
        private final double low;
        private final double high;

        public static ConfidenceInterval of(double value, double uncertainty, double interval) {
            double halfRange = uncertainty * interval;
            return new ConfidenceInterval(value - halfRange, value + halfRange);
        }

        public ConfidenceInterval(double low, double high) {
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
