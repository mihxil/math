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

import lombok.extern.java.Log;

import org.meeuw.math.abstractalgebra.*;

/**
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 * @param <S> structure element type
 * @param <E> type of real and imaginary fields
 */
@Log
public abstract class CompleteComplexNumbers<
    S extends CompleteComplexNumber<S, E, ES>,
    E extends CompleteScalarFieldElement<E>,
    ES extends CompleteScalarField<E>>
    extends AbstractComplexNumbers<S, E, ES>
    implements CompleteField<S> ,
    MetricSpace<S, E> {


    CompleteComplexNumbers(Class<S> elem, ES elementStructure) {
        super(elem, elementStructure);
    }

    abstract E atan2(E imaginary, E real);

    @Override
    public S pi() {
        return of(getElementStructure().pi());
    }

    @Override
    public S e() {
        return of(getElementStructure().e());
    }

    @Override
    public S 𝜑() {
        return of(getElementStructure().𝜑());
    }




}
