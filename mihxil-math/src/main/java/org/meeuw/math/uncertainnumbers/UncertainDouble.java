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

import java.util.OptionalDouble;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.DoubleUtils;
import org.meeuw.math.abstractalgebra.WithDoubleOperations;
import org.meeuw.math.exceptions.NotComparableException;
import org.meeuw.math.exceptions.WeighingExactValuesException;
import org.meeuw.math.numbers.DoubleOperations;

/**
 * A number with an uncertainty {@link #doubleUncertainty()}
 *<p>
 * Also defines scalar operations.
 * <p>
 * This is an extension of {@link UncertainNumber}, but it is implemented with primitive doubles, and the primitive
 * values are leading. {@link #getValue()}  and {@link #getUncertainty()} are just their boxed versions.
 * *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <SELF> self reference
 */
public interface UncertainDouble
    <SELF extends UncertainDouble<SELF>>
    extends
    UncertainScalar<Double, SELF>,
    WithDoubleOperations<SELF> {


    double EXACT = 0d;


    SELF immutableInstanceOfPrimitives(double value, double uncertainty);

    /**
     * @return Boxed version of {@link #doubleValue()}. Never {@code null}
     */
    @Override
    default Double getValue() {
        return doubleValue();
    }
    /**
     * The uncertainty in the {@link #doubleValue()}.
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


    default SELF plus(double summand) {
        return immutableInstanceOfPrimitives(summand + doubleValue(), doubleUncertainty());
    }

    default SELF minus(double subtrahend) {
        return plus(-1d * subtrahend);
    }

    /**
     * @param combinand  another uncertain real to combine with this one
     * @return a new uncertain number, combining this one with another one, representing a weighted average
     */
    default SELF weightedAverage(UncertainDouble<?> combinand) {
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
                    throw new WeighingExactValuesException("Can't combine 2 (different) exact values (" + this + " and " + combinand + ")");
                }
            }
            return immutableInstanceOfPrimitives(value, 0d);
        } else if (mu == 0d) {
            return immutableInstanceOfPrimitives(mvalue, 0d);
        }

        final double u2 = Math.max(u * u, DoubleUtils.uncertaintyForDouble(value));
        final double mu2 = Math.max(mu * mu, DoubleUtils.uncertaintyForDouble(mvalue));

        final double weight = Math.min(1d / u2, Double.MAX_VALUE);
        final double mweight = Math.min(1d / mu2, Double.MAX_VALUE);

        final double returnValue = (value * weight + mvalue * mweight) / (weight + mweight);

        // I'm not absolutely sure about this:
        final double returnUncertainty = 1d/ Math.sqrt((weight + mweight));
        return immutableInstanceOfPrimitives(returnValue, returnUncertainty);
    }

    /**
     * @param multiplier a double to multiply this with
     * @return a new {@link UncertainDouble} representing a multiple of this one.
     */
    @Override
    default SELF times(double multiplier) {
        return immutableInstanceOfPrimitives(multiplier * doubleValue(),
            Math.abs(multiplier) * doubleUncertainty());
    }

    @Override
    default SELF times(Double multiplier) {
        return times(multiplier.doubleValue());
    }

    default SELF negation() {
        return times(-1);
    }

    default SELF times(SELF multiplier) {
        double newValue = doubleValue() * multiplier.doubleValue();
        return immutableInstanceOfPrimitives(newValue,
            Math.max(
                operations().multiplicationUncertainty(
                    newValue, doubleFractionalUncertainty(), multiplier.doubleFractionalUncertainty()
                ),
                DoubleUtils.uncertaintyForDouble(newValue)
            )
        );
    }

    default SELF plus(SELF summand) throws NotComparableException {
        double u = doubleUncertainty();
        double mu = summand.doubleUncertainty();
        return immutableInstanceOfPrimitives(
            doubleValue() + summand.doubleValue(),
            operations().additionUncertainty(u, mu));

    }

    @Override
    default SELF pow(int exponent) {
        double v = Math.pow(doubleValue(), exponent);
        if (!Double.isFinite(v)) {
            throw new ArithmeticException("" + doubleValue() + "^" + exponent + "=" + v);
        }
        return immutableInstanceOfPrimitives(
            v,
            Math.abs(exponent) * Math.pow(doubleValue(), exponent - 1) * doubleUncertainty());
    }

    @Override
    default SELF abs() {
        return immutableInstanceOfPrimitives(Math.abs(doubleValue()), doubleUncertainty());
    }

    @Override
    default DoubleOperations operations() {
        return DoubleOperations.INSTANCE;
    }

    @Override
    default int compareTo(@NonNull SELF compare) {
        return Double.compare(doubleValue(), compare.doubleValue());
    }



}
