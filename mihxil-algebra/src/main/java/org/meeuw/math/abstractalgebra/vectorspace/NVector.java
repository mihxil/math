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
package org.meeuw.math.abstractalgebra.vectorspace;

import java.util.*;
import java.util.stream.Collectors;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.Vector;

/**
 * An {@code n}-dimensional {@link Vector}.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NVector<E extends FieldElement<E>> implements
    Vector<NVector<E>, E>, Iterable<E> {

    private final E[] values;

    NVector(E[] values) {
        this.values = values;
    }

    public E[] asArray() {
        return Arrays.copyOf(values, values.length);
    }

    @SafeVarargs
    public static <E extends FieldElement<E>> NVector<E> of(E... values) {
        return new NVector<>(values);
    }

    @Override
    public NVector<E> times(E multiplier) {
        final E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].times(multiplier);
        }
        return new NVector<>(copy);
    }

    @Override
    public NVector<E> dividedBy(E divisor) {
        final E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].dividedBy(divisor);
        }
        return new NVector<>(copy);
    }

    @Override
    public NVector<E> plus(NVector<E> summand) {
        final E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].plus(summand.values[i]);
        }
        return new NVector<>(copy);
    }

    @Override
    public NVector<E> negation() {
        final E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].negation();
        }
        return new NVector<>(copy);
    }

    @Override
    public E dot(NVector<E> e) {
        E result = values[0].getStructure().zero();
        for (int i = 0; i < values.length; i++) {
            result = result.plus(values[i].times(e.values[i]));
        }
        return result;
    }

    @Override
    public E get(int i) {
        return values[i];
    }

    @Override
    public NVectorSpace<E> getSpace() {
        return NVectorSpace.of(values.length, values[0].getStructure());
    }

    @Override
    public @NonNull Iterator<E> iterator() {
        return new Iterator<E>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public E next() {
                return values[i++];
            }
        };
    }

    @Override
    public Spliterator<E> spliterator() {
        return Arrays.spliterator(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NVector<?> vector = (NVector<?>) o;
        return Arrays.equals(values, vector.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return "(" + Arrays.stream(values).map(Object::toString).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public AbelianRing<NVector<E>> getStructure() {
        return getSpace();
    }

    @Override
    public NVector<E> times(NVector<E> multiplier) {
        E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].times(multiplier.values[i]);
        }
        return new NVector<>(copy);
    }
}
