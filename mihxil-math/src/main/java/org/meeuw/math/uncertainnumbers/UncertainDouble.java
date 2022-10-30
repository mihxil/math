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

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.OptionalDouble;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.Utils;
import org.meeuw.math.exceptions.NotComparableException;
import org.meeuw.math.exceptions.WeighingExceptValuesException;
import org.meeuw.math.numbers.*;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * A number with an uncertainty {@link #doubleUncertainty()}
 *
 * Also defines scalar operations.
 * <p>
 * This differs from {@link UncertainNumber}, because it is implemented with primitive doubles.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface UncertainDouble<D extends UncertainDouble<D>>
    extends Scalar<D>,
    UncertainNumber<Double> {

    double NaN_EPSILON = 0.001;
    double EXACT = 0d;

    @Override
    double doubleValue();


    D _of(double value, double uncertainty);

    /**
     * @return Boxed version of {@link #doubleValue()}. Never {@code null}
     */
    @Override
    default Double getValue() {
        return doubleValue();
    }
    /**
     * The uncertainty in the {@link #doubleValue()}. May in some cases be {@link Double#NaN} (e.g. when it's a standard deviation of 1 value).
     */
    double doubleUncertainty();

    /**
     * @return Boxed version of {@link #doubleUncertainty()}. Never {@code null}
     */
    @Override
    default Double getUncertainty() {
        return doubleUncertainty();
    }
    /**
     * Returns {@link #doubleUncertainty()} but wrapped in an {@link OptionalDouble}.
     * This never contains {@link Double#NaN}, which is the point of this method.
     */
    default OptionalDouble getOptionalUncertainty() {
        double uncertainty = doubleUncertainty();
        if (Double.isNaN(uncertainty)) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(uncertainty);
    }

    default double doubleFractionalUncertainty() {
        return operations().getFractionalUncertainty(doubleValue(), doubleUncertainty());
    }

    @Override
    default Double getFractionalUncertainty() {
        return doubleFractionalUncertainty();
    }

    @Override
    default boolean isExact() {
        return doubleUncertainty() == EXACT;
    }

    default D dividedBy(double divisor) {
        return times(1d / divisor);
    }

    default D plus(double summand) {
        return _of(summand + doubleValue(), doubleUncertainty());
    }

    default D minus(double subtrahend) {
        return plus(-1d * subtrahend);
    }

    /**
     * @deprecated Use {@link #weightedAverage(UncertainDouble)}
     */
    @Deprecated
    default D combined(UncertainReal m) {
        return weightedAverage(m);
    }
    /**
     * @param combinand  another uncertain real to combine with this one
     * @return a new uncertain number, combining this one with another one, representing a weighted average
     */
    default D weightedAverage(UncertainDouble<?> combinand) {
        double u = doubleUncertainty();
        double mu = combinand.doubleUncertainty();
        double value = doubleValue();
        double mvalue = combinand.doubleValue();

        // if one of them is still undefined, guess that it would be the same as the other one.
        if (Double.isNaN(mu)) {
            if (Double.isNaN(u)) {
                // of both of them are, just make them some equal random number.
                u = 1d;
            }
            mu = u;
        } else if (Double.isNaN(u)) {
            u = mu;
        }

        if (u == 0d) {
            if (mu == 0d) {
                if (value != mvalue) {
                    throw new WeighingExceptValuesException("Can't combine 2 (different) exact values (" + this + " and " + combinand + ")");
                }
            }
            return _of(value, 0d);
        } else if (mu == 0d) {
            return _of(mvalue, 0d);
        }

        final double u2 = Math.max(u * u, Utils.uncertaintyForDouble(value));
        final double mu2 = Math.max(mu * mu, Utils.uncertaintyForDouble(mvalue));

        final double weight = Math.min(1d / u2, Double.MAX_VALUE);
        final double mweight = Math.min(1d / mu2, Double.MAX_VALUE);

        final double returnValue = (value * weight + mvalue * mweight) / (weight + mweight);

        // I'm not absolutely sure about this:
        final double returnUncertainty = 1d/ Math.sqrt((weight + mweight));
        return _of(returnValue, returnUncertainty);
    }

    /**
     * @param multiplier a double to multiply this with
     * @return a new {@link UncertainDouble} representing a multiple of this one.
     */
    default D times(double multiplier) {
        return _of(multiplier * doubleValue(),
            Math.abs(multiplier) * doubleUncertainty());
    }

    @Override
    default D times(Double multiplier) {
        return times(multiplier.doubleValue());
    }

    default D negation() {
        return times(-1);
    }

    default D times(D multiplier) {
        double newValue = doubleValue() * multiplier.doubleValue();
        return _of(newValue,
            Math.max(
                operations().multiplicationUncertainty(
                    newValue, doubleFractionalUncertainty(), multiplier.doubleFractionalUncertainty()
                ),
                Utils.uncertaintyForDouble(newValue)
            )
        );
    }

    default D plus(D summand) throws NotComparableException {
        double u = doubleUncertainty();
        double mu = summand.doubleUncertainty();
        return _of(
            doubleValue() + summand.doubleValue(),
            operations().additionUncertainty(u, mu));

    }

    default D pow(int exponent) {
        double v = Math.pow(doubleValue(), exponent);
        if (!Double.isFinite(v)) {
            throw new ArithmeticException("" + doubleValue() + "^" + exponent + "=" + v);
        }
        return _of(
            v,
            Math.abs(exponent) * Math.pow(doubleValue(), exponent - 1) * doubleUncertainty());
    }

    @Override
    default int signum() {
        return (int) Math.signum(doubleValue());
    }

    @SuppressWarnings("unchecked")
    default boolean equals(Object value, double sds) {
        if (this == value) return true;
        if (! (value instanceof UncertainDouble)) {
            return false;
        }
        D other = (D) value;
        if (Double.isNaN(doubleValue())) {
            return Double.isNaN(other.doubleValue());
        }
        if (Double.isNaN(doubleUncertainty()) && Double.isNaN(other.doubleUncertainty())) {
            return toString().equals(other.toString());

        }
        return getConfidenceInterval(sds).contains(other.doubleValue())
            ||  other.getConfidenceInterval(sds).contains(doubleValue());
    }

    default DoubleConfidenceInterval getConfidenceInterval(double sds) {
        return DoubleConfidenceInterval.of(doubleValue(), doubleUncertainty(), sds);
    }

    @Override
    default DoubleOperations operations() {
        return DoubleOperations.INSTANCE;
    }

    @Override
    default BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(doubleValue());
    }

    @Override
    default BigInteger bigIntegerValue() {
        return BigInteger.valueOf(longValue());
    }

    @Override
    default D abs() {
        return _of(Math.abs(doubleValue()), doubleUncertainty());
    }

    @Override
    default int compareTo(@NotNull @NonNull D o) {
        if (this.equals(o)) {
            return 0;
        } else {
            return Double.compare(doubleValue(), o.doubleValue());
        }
    }
}
