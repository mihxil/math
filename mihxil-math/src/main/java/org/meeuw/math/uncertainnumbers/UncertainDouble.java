package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.function.Predicate;

import org.meeuw.math.numbers.SignedNumberElement;

/**
 * A number with an uncertainty {@link #getUncertainty()}
 *
 * Also defines scalar operations.
 *
 * This differs from {@link UncertainNumber}, because it is implemented with primitive doubles.
 *
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainDouble extends Comparable<Number>, SignedNumberElement<UncertainDouble> {

    double NaN_EPSILON = 0.001;
    double EXACT = 0d;

    double doubleValue();

    double getUncertainty();

    default boolean isExact() {
        return getUncertainty() == EXACT;
    }

    default UncertainDouble dividedBy(double divisor) {
        return times(1d / divisor);
    }

    default UncertainDouble plus(double summand) {
        return new ImmutableUncertainDouble(summand + doubleValue(), getUncertainty());
    }

    default UncertainDouble minus(double subtrahend) {
        return plus(-1d * subtrahend);
    }

    /**
     * Creates a new uncertain number, combining this one with another one.
     */
    default UncertainDouble combined(UncertainDouble m) {
        double u = getUncertainty();
        double mu = m.getUncertainty();
        double weight = 1d / (u * u);
        double mweight = 1d / (mu * mu);
        double value = (doubleValue() * weight + m.doubleValue() * mweight) / (weight + mweight);

        // I'm not absolutely sure about this:
        double uncertaintity = 1d/ Math.sqrt((weight + mweight));
        return new ImmutableUncertainDouble(value, uncertaintity);
    }

    /**
     * Creates a new {@link UncertainDouble} representing a multiple of this one.
     */
    default UncertainDouble times(double multiplier) {
        return new ImmutableUncertainDouble(multiplier * doubleValue(),
            Math.abs(multiplier) * getUncertainty());
    }


    default UncertainDouble times(UncertainDouble multiplier) {
        double u = getUncertainty() / doubleValue();
        double mu = multiplier.getUncertainty() / multiplier.doubleValue();
        double newValue = doubleValue() * multiplier.doubleValue();
        return new ImmutableUncertainDouble(
            newValue,
            Math.abs(newValue) * Math.sqrt( (u * u)  + (mu * mu))
        );
    }

    default UncertainDouble pow(int exponent) {
        double v = Math.pow(doubleValue(), exponent);
        if (!Double.isFinite(v)) {
            throw new ArithmeticException("" + doubleValue() + "^" + exponent + "=" + v);
        }
        return new ImmutableUncertainDouble(
            v,
            Math.abs(exponent) * Math.pow(doubleValue(), exponent - 1) * getUncertainty());
    }

    default UncertainDouble plus(UncertainDouble summand) {
        double u = getUncertainty();
        double mu = summand.getUncertainty();
        return new ImmutableUncertainDouble(
            doubleValue() + summand.doubleValue(),
            Math.sqrt(u * u + mu * mu));

    }

    default boolean equals(Object value, double sds) {
        if (this == value) return true;
        if (! (value instanceof UncertainDouble)) {
            return false;
        }
        UncertainDouble other = (UncertainDouble) value;
        if (Double.isNaN(doubleValue())) {
            return Double.isNaN(other.doubleValue());
        }
        if (Double.isNaN(getUncertainty()) && Double.isNaN(other.getUncertainty())) {
            return toString().equals(other.toString());

        }
        return getConfidenceInterval(sds).contains(other.doubleValue())
            ||  other.getConfidenceInterval(sds).contains(doubleValue());
    }

    default BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(doubleValue());
    }

    default ConfidenceInterval getConfidenceInterval(double sds) {
        return ConfidenceInterval.of(doubleValue(), getUncertainty(), sds);
    }

    @Getter
    class ConfidenceInterval implements Predicate<Double> {
        private final double low;
        private final double high;

        public static ConfidenceInterval of(double value, double uncertainty, double interval) {
            double halfRange = Double.isNaN(uncertainty) ? Math.abs(value * NaN_EPSILON) : uncertainty * interval;
            return new ConfidenceInterval(value - halfRange, value + halfRange);
        }

        public ConfidenceInterval(double low, double high) {
            this.low = low;
            this.high = high;
        }

        public boolean contains(double value) {
            return this.low <= value && value <= this.high;
        }
        @Override
        public boolean test(Double value) {
            return value != null && contains(value);
        }
    }

}
