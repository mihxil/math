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
package org.meeuw.math.abstractalgebra.complex;

import lombok.Getter;
import lombok.extern.java.Log;

import org.meeuw.math.abstractalgebra.*;

/**
 * An abstract implementation of complex number, which remains agnostic about the exact type of the real an imaginary components
 * Normally it would be {@link org.meeuw.math.abstractalgebra.reals.RealNumber}'s leading to {@link ComplexNumber}, but it can
 * (e.g.) also be {@link org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber} and then this becomes a {@link GaussianRational}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <S> structure element type
 * @param <E> type of real and imaginary fields
 * @see AbstractComplexNumber
 */
@Log
public abstract class AbstractComplexNumbers<
    S extends AbstractComplexNumber<S, E, ES>,
    E extends ScalarFieldElement<E>,
    ES extends ScalarField<E>
    >
    extends AbstractAlgebraicStructure<S>
    implements Field<S> {

    @Getter
    private final ES elementStructure;

    private final S zero;
    private final S one;
    private final S i;

    AbstractComplexNumbers(Class<S> elem, ES elementStructure) {
        super(elem);
        this.elementStructure = elementStructure;
        this.zero = of(this.elementStructure.zero(), this.elementStructure.zero());
        this.one  = of(this.elementStructure.one(), this.elementStructure.zero());
        this.i    = of(this.elementStructure.zero(), this.elementStructure.one());
    }

    abstract S of(E real, E imaginary);

    @Override
    public S zero() {
        return zero;
    }

    @Override
    public S one() {
        return one;
    }

    public S i() {
        return i;
    }

    @Override
    public Cardinality getCardinality() {
        return getElementStructure().getCardinality();
    }
}
