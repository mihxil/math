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

import org.meeuw.math.WithDoubleOperations;
import org.meeuw.math.validation.NotZero;

/**
 *
 * @see DivisibleGroup
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface DivisibleGroupElement<E extends DivisibleGroupElement<E>>
    extends MultiplicativeGroupElement<E>,
    WithDoubleOperations<E> {

    @Override
    DivisibleGroup<E> getStructure();

    /**
     * Returns the result of dividing this element by the given divisor.
     */
    E dividedBy(@NotZero long divisor);

    /**
     * Returns the result of multiplying this element by the given multiplier.
     */
    E times(long multiplier);



}
