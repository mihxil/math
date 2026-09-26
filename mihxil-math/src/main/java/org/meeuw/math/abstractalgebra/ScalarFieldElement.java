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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.WithDoubleOperations;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.TranscendentalFunctionsNumber;
import org.meeuw.math.validation.NotZero;

/**
 * A {@link FieldElement field element} that is also a {@link Scalar scalar}, e.g. it is very much like a 'number'.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ScalarFieldElement<E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>> extends
    FieldElement<E>,
    Scalar<E>,
    TranscendentalFunctionsNumber<E, C>,
    WithDoubleOperations<E> {

    @Override
    @NonNull
    ScalarField<E, C> getStructure();

    @Override
    default boolean isZero() {
        return FieldElement.super.isZero();
    }

    @SuppressWarnings("unchecked")
    default C complete() {
        return getStructure().complete((E) this);
    }

    /**
     * Returns the non-negative remainder after division by a non-zero scalar.
     *
     * @param divisor the non-zero divisor
     * @return a scalar greater than or equal to zero and smaller than {@code abs(divisor)}
     */
    @NonAlgebraic(reason = NonAlgebraic.Reason.NON_ALL_ELEMENTS)
    default E mod(@NotZero E divisor) throws DivisionByZeroException {
        if (divisor.signum() == 0) {
            throw new DivisionByZeroException("Modulus must be non-zero", this);
        }
        divisor = divisor.abs();
        return minus(dividedBy(divisor).floor().times(divisor));
    }


    /**
     * the largest (closest to positive infinity) value that less than or equal and is equal to a mathematical integer.
     */
    E floor();

    default <A extends ScalarFieldElement<A, C>> A approx(ScalarField<A, C> field) {
        return field.approx(complete());
    }

}
