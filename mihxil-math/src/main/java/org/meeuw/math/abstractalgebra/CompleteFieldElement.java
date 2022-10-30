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

import org.meeuw.math.exceptions.IllegalLogException;

/**
 * Elements of a {@link CompleteField}.
 *
 * @see CompleteField
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> Self reference
 */
public interface CompleteFieldElement<E extends CompleteFieldElement<E>>
    extends
    FieldElement<E> {

    @Override
    CompleteField<E> getStructure();

    E sqrt();

    E sin();

    E cos();

    default E pow(E exponent) throws IllegalLogException {
        return (ln().times(exponent)).exp();
    }

    E exp();

    E ln();

    default E sinh() {
        return exp().minus(negation().exp()).dividedBy(2);
    }
    default E cosh() {
        return exp().plus(negation().exp()).dividedBy(2);
    }

}
