/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.uncertainnumbers;

import java.math.BigDecimal;

import java.math.BigInteger;

import org.meeuw.math.Utils;
import org.meeuw.math.exceptions.NotComparableException;
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
public interface UncertainDouble<D extends UncertainDouble<D>> extends Scalar<D>, Uncertain {

    double NaN_EPSILON = 0.001;
    double EXACT = 0d;

    double getValue();

    double getUncertainty();

    default double getFractionalUncertainty() {
        return operations().getFractionalUncertainty(getValue(), getUncertainty());
    }

    @Override
    default boolean isExact() {
        return getUncertainty() == EXACT;
    }

    default D dividedBy(double divisor) {
        return times(1d / divisor);
    }

    default D plus(double summand) {
        return _of(summand + getValue(), getUncertainty());
    }

    default D minus(double subtrahend) {
        return plus(-1d * subtrahend);
    }

    /**
     * @param m  another uncertain real to combine with this one
     * @return a new uncertain number, combining this one with another one, representing a weighted average
     */
    default D combined(UncertainReal m) {
        double u = getUncertainty();
        double mu = m.getUncertainty();
        double weight = 1d / (u * u);
        double mweight = 1d / (mu * mu);
        double value = (getValue() * weight + m.getValue() * mweight) / (weight + mweight);

        // I'm not absolutely sure about this:
        double uncertainty = 1d/ Math.sqrt((weight + mweight));
        return _of(value, uncertainty);
    }

    /**
     * @param multiplier a double to multiply this with
     * @return a new {@link UncertainDouble} representing a multiple of this one.
     */
    default D times(double multiplier) {
        return _of(multiplier * getValue(),
            Math.abs(multiplier) * getUncertainty());
    }

    default D negation() {
        return times(-1);
    }

    default D times(D multiplier) {
        double newValue = getValue() * multiplier.getValue();
        return _of(newValue,
            Math.max(
                operations().multiplicationUncertainty(
                    newValue, getFractionalUncertainty(), multiplier.getFractionalUncertainty()
                ),
                Utils.uncertaintyForDouble(newValue)
            )
        );
    }

    default D plus(D summand) throws NotComparableException {
        double u = getUncertainty();
        double mu = summand.getUncertainty();
        return _of(
            getValue() + summand.getValue(),
            operations().additionUncertainty(u, mu));

    }

    default D pow(int exponent) {
        double v = Math.pow(getValue(), exponent);
        if (!Double.isFinite(v)) {
            throw new ArithmeticException("" + getValue() + "^" + exponent + "=" + v);
        }
        return _of(
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

    D _of(double value, double uncertainty);

    default DoubleConfidenceInterval getConfidenceInterval(double sds) {
        return DoubleConfidenceInterval.of(getValue(), getUncertainty(), sds);
    }

    default DoubleOperations operations() {
        return DoubleOperations.INSTANCE;
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
    default BigInteger bigIntegerValue() {
        return BigInteger.valueOf(longValue());
    }

    @Override
    default D abs() {
        return _of(Math.abs(getValue()), getUncertainty());
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
