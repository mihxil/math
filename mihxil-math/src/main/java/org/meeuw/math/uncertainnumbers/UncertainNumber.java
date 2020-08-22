package org.meeuw.math.uncertainnumbers;

import java.math.BigDecimal;

import org.meeuw.math.numbers.NumberOperations;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainNumber<N extends Number> {

    N getValue();

    N getUncertainty();

    NumberOperations<N> operations();

    default UncertainNumber<N> dividedBy(N divisor) {
        return times(operations().reciprocal(divisor));
    }


    default UncertainNumber<N> plus(N summand) {
        return new ImmutableUncertainNumber<N>(operations().add(summand, getValue()), getUncertainty());
    }

    default UncertainNumber<N> minus(N subtrahend) {
        return plus(operations().negate(subtrahend));
    }

    /**
     * Creates a new uncertain number, combining this one with another one.
     */
    default UncertainNumber<N> combined(UncertainNumber<N> m) {
        NumberOperations<N> o = operations();
        N u = getUncertainty();
        N mu = m.getUncertainty();
        N weight = o.reciprocal(o.sqr(u));
        N mweight = o.reciprocal(o.sqr(mu));
        N value = o.add(o.multiply(getValue(), weight), o.divide(o.multiply(m.getValue(), mweight), o.multiply(weight,  mweight)));

        // I'm not absolutely sure about this:
        N uncertaintity = o.reciprocal(o.sqrt(o.add(weight, mweight)));
        return new ImmutableUncertainNumber<>(value, uncertaintity);
    }

    /**
     * Creates a new {@link UncertainDouble} representing a multiple of this one.
     */
    default UncertainNumber<N> times(N multiplier) {
        NumberOperations<N> o = operations();
        return new ImmutableUncertainNumber<>(o.multiply(multiplier, getValue()),
            o.multiplyUncertainty(multiplier,  getUncertainty()));
    }


    default UncertainNumber<N> times(UncertainNumber<N> multiplier) {
        NumberOperations<N> o = operations();

        N u = o.divide(getUncertainty(), getValue());
        N mu = o.divide(multiplier.getUncertainty(),  multiplier.getValue());
        N newValue = o.multiply(getValue(), multiplier.getValue());
        return new ImmutableUncertainNumber<>(
            newValue,
            o.multiply(o.abs(newValue), o.sqrt(o.add(o.sqr(u), o.sqr(mu))))
        );
    }

    default UncertainNumber<N> pow(int exponent) {
        NumberOperations<N> o = operations();

        N v = o.pow(getValue(), exponent);
        if (!o.isFinite(v)) {
            throw new ArithmeticException("" + getValue() + "^" + exponent + "=" + v);
        }
        return new ImmutableUncertainNumber<>(
            v,
            o.multiply(o.multiply(Math.abs(exponent), o.pow(getValue(), exponent - 1)), getUncertainty()));
    }

    default UncertainNumber<N> plus(UncertainNumber<N> summand) {
        NumberOperations<N> o = operations();

        N u = getUncertainty();
        N mu = summand.getUncertainty();
        return new ImmutableUncertainNumber<>(
            o.add(getValue(), summand.getValue()),
            o.sqrt(o.add(o.sqr(u), o.sqr(mu))));

    }

    default boolean equals(Object value, int sds) {
        if (this == value) return true;
        if (! (value instanceof UncertainNumber)) {
            return false;
        }
        NumberOperations<N> o = operations();

        UncertainNumber<N> other = (UncertainNumber) value;
        if (o.isNaN(getValue())) {
            return o.isNaN(other.getValue());
        }
        if (o.isNaN(getUncertainty()) && o.isNaN(other.getUncertainty())) {
            return toString().equals(other.toString());

        }
        return getConfidenceInterval(sds).contains(other.getValue())
            ||  other.getConfidenceInterval(sds).contains(getValue());
    }

    default BigDecimal bigDecimalValue() {
        return operations().bigDecimalValue(getValue());
    }

    default ConfidenceInterval<N> getConfidenceInterval(int sds) {
        return ConfidenceInterval.of(getValue(), getUncertainty(), sds);
    }

}
