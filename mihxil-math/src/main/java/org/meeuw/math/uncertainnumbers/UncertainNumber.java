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

import java.math.BigDecimal;

import org.meeuw.math.numbers.NumberOperations;
import org.meeuw.math.numbers.UncertaintyNumberOperations;

/**
 * The interface representing an uncertain number. It makes no
 * assumptions about the implemented algebra yet, it just is based on some {@link Number}
 *
 * @author Michiel Meeuwissen
 * @see UncertainDouble
 * @since 0.4
 * @param <N> The {@link Number} implementation this is based on
 */
public interface UncertainNumber<N extends Number> extends Uncertain {

    N getValue();

    N getUncertainty();

    @Override
    default boolean isExact() {
        return getUncertainty().doubleValue() == 0d;
    }

    default N getFractionalUncertainty() {
        return operations().getFractionalUncertainty(getValue(), getUncertainty());
    }

    default UncertaintyNumberOperations<N> operations() {
        return UncertaintyNumberOperations.of(getValue());
    }

    /**
     * When calculating the uncertainty it is normally enough to use a version of {@link #operations()} that does calculations
     * with less precision
     * @return the operations object which is used for uncertainty propagation
     */
    default UncertaintyNumberOperations<N> uncertaintyOperations() {
        return operations();
    }

    /**
     * Creates a new {@link org.meeuw.math.uncertainnumbers.ImmutableUncertainNumber} representing a multiple of this one.
     * <p>
     * @param multiplier a number to multiply this white
     * @return a new (possibly, and default, immutable) uncertain number representing a multiple of this one
     */
    default UncertainNumber<N> times(N multiplier) {
        NumberOperations<N> o = operations();
        N newvalue = o.multiply(multiplier, getValue());
        return new ImmutableUncertainNumber<N>(
            newvalue,
            () -> o.multiply(o.abs(multiplier), getUncertainty())
        );
    }

    default UncertainNumber<N> dividedBy(N divisor) {
        return times(operations().reciprocal(divisor));
    }

    default UncertainNumber<N> plus(N summand) {
        return new ImmutableUncertainNumber<N>(operations().add(summand, getValue()), this::getUncertainty);
    }

    default UncertainNumber<N> minus(N subtrahend) {
        return plus(operations().negate(subtrahend));
    }

    /**
     * Creates a new uncertain number, combining this one with another one.
     * <a href="https://en.wikipedia.org/wiki/Weighted_arithmetic_mean#Variance_weights">wikipedia</a>
     * @param m another uncertain number
     * @return a new (immutable) uncertain number representing the (weighted) average of this one and {@code m}
     */
    default UncertainNumber<N> combined(UncertainNumber<N> m) {
        NumberOperations<N> o = operations();
        N u = getUncertainty();
        N mu = m.getUncertainty();
        N weight = o.reciprocal(o.sqr(u)).getValue();
        N mweight = o.reciprocal(o.sqr(mu)).getValue();
        UncertainNumber<N> value = o.divide(
            o.add(o.multiply(getValue(), weight), o.multiply(m.getValue(), mweight)),
            o.add(weight,  mweight)

        );

        NumberOperations<N> uo = uncertaintyOperations();

        N uncertainty = uo.sqrt(
            o.reciprocal(
                o.add(o.reciprocal(o.sqr(u)).getValue(), o.reciprocal(o.sqr(mu)).getValue())
            ).getValue()
        ).getValue();
        return new ImmutableUncertainNumber<>(value.getValue(), uncertainty);
    }

    default UncertainNumber<N> times(UncertainNumber<N> multiplier) {
        N newValue = operations().multiply(getValue(), multiplier.getValue());
        return new ImmutableUncertainNumber<N>(
            newValue,
            () -> operations().multiplicationUncertainty(
                newValue, getFractionalUncertainty(),
                multiplier.getFractionalUncertainty()
            )
        );
    }

    default UncertainNumber<N> plus(UncertainNumber<N> summand) {
        NumberOperations<N> o = operations();
        return new ImmutableUncertainNumber<N>(
            o.add(getValue(), summand.getValue()),
            () -> operations().additionUncertainty(getUncertainty(), summand.getUncertainty())
        );
    }

    default UncertainNumber<N> pow(int exponent) {
        NumberOperations<N> o = operations();

        N v = o.pow(getValue(), exponent);
        if (!o.isFinite(v)) {
            throw new ArithmeticException("" + getValue() + "^" + exponent + "=" + v);
        }
        return new ImmutableUncertainNumber<N>(
            v,
            () -> o.multiply(o.multiply(Math.abs(exponent), o.pow(getValue(), exponent - 1)), getUncertainty()));
    }

    default int signum() {
        return  operations().signum(getValue());
    }

    @SuppressWarnings("unchecked")
    default boolean equals(Object value, int sds) {
        if (this == value) return true;
        if (! (value instanceof UncertainNumber)) {
            return false;
        }
        NumberOperations<N> o = operations();

        UncertainNumber<N> other = (UncertainNumber<N>) value;
        if (o.isNaN(getValue())) {
            return o.isNaN(other.getValue());
        }
        if (o.isNaN(other.getValue())) {
            return o.isNaN(getValue());
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
