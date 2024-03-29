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

import org.meeuw.math.WithScalarOperations;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <V> self
 * @param <S> {@link FieldElement type} of the elements of the vector, mu
 */
public interface Vector<V extends Vector<V, S>, S extends FieldElement<S>>
    extends Iterable<S>,
    AbelianRingElement<V>,
    WithScalarOperations<V, S> {

    @Override
    V times(S multiplier);

    @Override
    V plus(V summand);

    S dot(V multiplier);

    //V cross(V multiplier);

    @Override
    V negation();

    S get(int i) throws ArrayIndexOutOfBoundsException;

    VectorSpace<S, V> getSpace();

}
