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
package org.meeuw.math.abstractalgebra;

import jakarta.validation.constraints.PositiveOrZero;

import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.Synonym;
import org.meeuw.math.exceptions.IllegalPowerException;

import static org.meeuw.math.operators.BasicAlgebraicIntOperator.POWER;

/**
 * Elements of a {@link MultiplicativeSemiGroup} can be multiplied by each other (via {@link #times(MultiplicativeSemiGroupElement)}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupElement<E extends MultiplicativeSemiGroupElement<E>>
    extends MagmaElement<E> {

    @Override
    @NonNull
    MultiplicativeSemiGroup<E> getStructure();

    /**
     * Multiplies this element with another element of the same type.
     * @param multiplier the element to multiply with
     * @return this * multiplier
     * @see #x(MultiplicativeSemiGroupElement)
     */
    @Synonym("x")
    E times(E multiplier);

    /**
     * less verbose version of {@link #times(MultiplicativeSemiGroupElement)}
     * @param multiplier the element to multiply with
     * @return this * multiplier
     * @see #times(MultiplicativeSemiGroupElement)
     */
    @Synonym("times")
    default E x(E multiplier) {
        return times(multiplier);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This is a default implementation that calls {@link #times(MultiplicativeSemiGroupElement)}.
     */
    @Override
    @Synonym("times")
    default E operate(E operand) {
        return times(operand);
    }


    /**
     * if multiplication is defined, then so is exponentiation, as long as the exponent is a positive integer.
     * <p>
     * This default implementation is doing <a href="https://en.wikipedia.org/wiki/Exponentiation_by_squaring">exponentiation by squaring</a>
     * @param exponent the exponent
     * @return this <sup>exponent</sup>
     * @see #pow(long)
     */
    @SuppressWarnings({"unchecked"})
    default E pow(@Positive int exponent) throws IllegalPowerException{
        if (exponent < 0) {
            throw new IllegalPowerException("Not defined for negative exponents", POWER.stringify(toString(), Integer.toString(exponent)));
        }
        if (exponent == 0) {
            throw new IllegalPowerException("Not defined for exponent = 0", POWER.stringify(toString(), Integer.toString(exponent)));
        }
        E y = null;
        E x = (E) this;
        while (exponent > 1) {
            if (exponent % 2 == 1) {
                y = y == null ? x : x.times(y);
                exponent = (exponent - 1) / 2;
            } else {
                exponent /= 2;
            }
            x = x.times(x);
        }
        return y == null ? x : x.times(y);
    }


    /**
     * Returns this <sup>n</sup> for a long exponent.
     * <p>
     * This default implementation is doing <a href="https://en.wikipedia.org/wiki/Exponentiation_by_squaring">exponentiation by squaring</a>.
     * @param exponent
     * @return
     * @throws IllegalPowerException
     * @see #pow(int)
     */
    @SuppressWarnings({"unchecked"})
    default E pow(@PositiveOrZero long exponent) throws IllegalPowerException{
        if (exponent < 0) {
            throw new IllegalPowerException("Not defined for negative exponents", POWER.stringify(toString(), Long.toString(exponent)));
        }
        if (exponent == 0) {
            throw new IllegalPowerException("Not defined for exponent = 0", POWER.stringify(toString(), Long.toString(exponent)));
        }
        E y = null;
        E x = (E) this;
        while (exponent > 1) {
            if (exponent % 2 == 1) {
                y = y == null ? x : x.times(y);
                exponent = (exponent - 1) / 2;
            } else {
                exponent /= 2;
            }
            x = x.times(x);
        }
        return y == null ? x : x.times(y);
    }

    /**
     * Since multiplication is defined, squaring is also defined, it is just the element {@link #times(MultiplicativeSemiGroupElement) multiplied} by itself.
     * @return this element multiplied by itself.
     */
    @SuppressWarnings("unchecked")
    default E sqr() {
        return times((E) this);
    }

}
