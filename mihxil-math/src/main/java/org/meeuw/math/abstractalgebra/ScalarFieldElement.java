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

import org.meeuw.math.numbers.Scalar;

/**
 * A field element that is also a scalar, e.g. it is very much like a number.
 **
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ScalarFieldElement<E extends ScalarFieldElement<E>> extends
    FieldElement<E>,
    Scalar<E>,
    StrictlyOrdered<E> {

    @Override
    ScalarField<E> getStructure();

    @Override
    default boolean eq(E other) {
        return equals(other);
    }

    @Override
    default boolean isZero() {
        return FieldElement.super.isZero();
    }

}
