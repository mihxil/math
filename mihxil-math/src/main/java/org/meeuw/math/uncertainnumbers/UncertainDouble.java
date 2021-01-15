package org.meeuw.math.uncertainnumbers;

import java.math.BigDecimal;

import org.meeuw.math.numbers.*;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

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
public interface UncertainDouble<D extends UncertainDouble<D>> extends  Scalar<D> {

    double NaN_EPSILON = 0.001;
    double EXACT = 0d;

    double getValue();

    double getUncertainty();

    default double getFractionalUncertainty() {
        return operations().getFractionalUncertainty(getValue(), getUncertainty());
    }

    default boolean isExact() {
        return getUncertainty() == EXACT;
    }

    default D dividedBy(double divisor) {
        return times(1d / divisor);
    }

    default D plus(double summand) {
        return of(summand + getValue(), getUncertainty());
    }

    default D minus(double subtrahend) {
        return plus(-1d * subtrahend);
    }

    /**
     * Creates a new uncertain number, combining this one with another one.
     */
    default D combined(UncertainReal m) {
        double u = getUncertainty();
        double mu = m.getUncertainty();
        double weight = 1d / (u * u);
        double mweight = 1d / (mu * mu);
        double value = (getValue() * weight + m.getValue() * mweight) / (weight + mweight);

        // I'm not absolutely sure about this:
        double uncertainty = 1d/ Math.sqrt((weight + mweight));
        return of(value, uncertainty);
    }

    /**
     * Creates a new {@link UncertainDouble} representing a multiple of this one.
     */
    default D times(double multiplier) {
        return of(multiplier * getValue(),
            Math.abs(multiplier) * getUncertainty());
    }

    default D negation() {
        return times(-1);
    }

    default D times(D multiplier) {
        double newValue = getValue() * multiplier.getValue();
        return of(newValue,
            operations().multipliedUncertainty(newValue, getFractionalUncertainty(), multiplier.getFractionalUncertainty())
        );
    }

    default D plus(D summand) {
        double u = getUncertainty();
        double mu = summand.getUncertainty();
        return of(
            getValue() + summand.getValue(),
            operations().addUncertainty(u, mu));

    }

    default D pow(int exponent) {
        double v = Math.pow(getValue(), exponent);
        if (!Double.isFinite(v)) {
            throw new ArithmeticException("" + getValue() + "^" + exponent + "=" + v);
        }
        return of(
            v,
            Math.abs(exponent) * Math.pow(getValue(), exponent - 1) * getUncertainty());
    }

    @Override
    default int signum() {
        return (int) Math.signum(getValue());
    }

    default boolean equals(Object value, double sds) {
        if (this == value) return true;
        if (! (value instanceof UncertainDouble)) {
            return false;
        }
        D other = (D) value;
        if (Double.isNaN(getValue())) {
            return Double.isNaN(other.getValue());
        }
        if (Double.isNaN(getUncertainty()) && Double.isNaN(other.getUncertainty())) {
            return toString().equals(other.toString());

        }
        return getConfidenceInterval(sds).contains(other.getValue())
            ||  other.getConfidenceInterval(sds).contains(getValue());
    }

    D of(double value, double uncertainty);

    default DoubleConfidenceInterval getConfidenceInterval(double sds) {
        return DoubleConfidenceInterval.of(getValue(), getUncertainty(), sds);
    }

    default DoubleOperations operations() {
        return DoubleOperations.INSTANCE;
    }


    @Override
    default long longValue() {
        return (long) getValue();
    }

    @Override
    default double doubleValue() {
        return getValue();
    }

    @Override
    default BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(getValue());
    }

    @Override
    default D abs() {
        return of(Math.abs(getValue()), getUncertainty());
    }

    @Override
    default int compareTo(D o) {
        if (this.equals(o)) {
            return 0;
        } else {
            return Double.compare(getValue(), o.getValue());
        }
    }
}
