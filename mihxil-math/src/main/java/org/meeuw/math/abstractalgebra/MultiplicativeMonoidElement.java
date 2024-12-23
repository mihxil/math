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

import jakarta.validation.constraints.Min;

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
     */
    @Override
    default E pow(@Min(0) int n) {
        if (n < 0) {
            throw new IllegalPowerException("Negative power", BasicAlgebraicIntOperator.POWER.stringify(toString(), Integer.toString(n)));
        }
        if (n == 0) {
            return getStructure().one();
        }
        return MultiplicativeSemiGroupElement.super.pow(n);
    }


    default boolean isOne() {
        return getStructure().one().equals(this);
    }


}
