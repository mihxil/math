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

import org.checkerframework.checker.index.qual.NonNegative;
import org.meeuw.math.exceptions.IllegalPowerException;
import org.meeuw.math.operators.BasicAlgebraicIntOperator;

/**
 * An element of the {@link MultiplicativeMonoid} structure.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoidElement<E extends MultiplicativeMonoidElement<E>>
    extends MultiplicativeSemiGroupElement<E> {

    @Override
    MultiplicativeMonoid<E> getStructure();

    /**
     * if multiplication is defined, then so is integer exponentiation, as long as the exponent is non-negative
     * <p>
     * This default implementation is based on {@link MultiplicativeSemiGroupElement#pow(int)}
     * If the argument is {@code 0}, then return {@link #getStructure()}{@link MultiplicativeMonoid#one()}
     * @see  BasicAlgebraicIntOperator#POWER
     */
    @Override
    default E pow(@NonNegative int exponent) {
        if (exponent < 0) {
            throw new IllegalPowerException("Negative power", BasicAlgebraicIntOperator.POWER.stringify(toString(), Integer.toString(exponent)));
        }
        if (exponent == 0) {
            return getStructure().one();
        }
        return MultiplicativeSemiGroupElement.super.pow(exponent);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This extends {@link MultiplicativeSemiGroupElement#pow(long)}, but doesn't give an exception if the argument is {@code 0}, but will return {@link MultiplicativeGroup#one()}.
     * </p>
     * @param exponent
     * @return
     */
    default E pow(@PositiveOrZero long exponent) {
        if (exponent < 0) {
            throw new IllegalPowerException("Negative power", BasicAlgebraicIntOperator.POWER.stringify(toString(), Long.toString(exponent)));
        }
        if (exponent == 0) {
            return getStructure().one();
        }
        return MultiplicativeSemiGroupElement.super.pow(exponent);
    }


    /**
     * Whether this element is the identity of the monoid.
     */
    default boolean isOne() {
        return getStructure().one().equals(this);
    }


}
