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

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> The type of the elements of the vectors in this space
 * @param <V> The type of the vectors itself.
 */
public interface VectorSpace<E extends FieldElement<E>, V extends Vector<V, E>> extends AbelianRing<V>, CompoundStructure<Field<E>> {

    int getDimension();

    @Override
    V zero();

    Field<E> getField();

    /**
     * The elements of a vector space are built from elements of its {@link #getField() field}.
     */
    @Override
    default Field<E> getElementStructure() {
        return getField();
    }


}
