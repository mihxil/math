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
package org.meeuw.math;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * A {@link org.meeuw.functional.Equivalence} with two params with the same type, with associated semantics of 'equivalence'.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> argument type
 */
@FunctionalInterface
public interface Equivalence<E> extends org.meeuw.functional.Equivalence<E> {

    /**
     * Tests whether two objects are 'equivalent'. This may simply mean that they are {@link Object#equals(Object) equal}, but
     * it may also be a bit more relaxed like {@link org.meeuw.math.abstractalgebra.AlgebraicElement#eq(AlgebraicElement) approximately or probably equal}
     * @param e1 one object
     * @param e2 another object
     * @return {@code true} if both these objects are 'equivalent'.
     */
    @Override
    boolean test(E e1, E e2);

}
