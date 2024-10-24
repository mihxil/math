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

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.exceptions.ReciprocalException;

/**
 * An element of an algebraic Field. Next to multiplication, also addition is defined.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface FieldElement<E extends FieldElement<E>> extends
    DivisionRingElement<E>,
    AbelianRingElement<E>,
    DivisibleGroupElement<E>,
    GroupElement<E> {

    @Override
    Field<E> getStructure();

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME, value="Cannot divide be zero")
    default E dividedBy(E divisor) throws ReciprocalException {
        return DivisionRingElement.super.dividedBy(divisor);
    }

    @Override
    @NonAlgebraic(reason = NonAlgebraic.Reason.SOME, value="Zero has no reciprocal")
    E reciprocal() throws ReciprocalException;



}

