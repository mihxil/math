/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra;

/**
 * Elements of a {@link AdditiveSemiGroup} can be added to each other (via {@link #plus(AdditiveSemiGroupElement)}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveSemiGroupElement<E extends AdditiveSemiGroupElement<E>>
    extends MagmaElement<E> {

    @Override
    AdditiveSemiGroup<E> getStructure();

    /**
     * @param summand the element to add to this one
     * @return this + summand
     */
    E plus(E summand);

    @Override
    default E operate(E operand) {
        return plus(operand);
    }

    /**
     * less verbose version of {@link #plus(AdditiveSemiGroupElement)}
     * @param summand the element to add to this one
     * @return this + summand
     * @see #plus(AdditiveSemiGroupElement)
     */
    default E p(E summand) {
        return plus(summand);
    }

}
