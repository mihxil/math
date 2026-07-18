/*
 *  Copyright 2026 Michiel Meeuwissen
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
package org.meeuw.math.abstractalgebra.dim3;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 */
public class CompleteFieldVector3Space<E  extends CompleteScalarFieldElement<E>>
    implements VectorSpace<E, CompleteFieldVector3<E>> {

    private static final Map<ScalarField<?, ?>, CompleteFieldVector3Space<?>> INSTANCES = new ConcurrentHashMap<>();

    final CompleteScalarField<E> scalarField;

    public CompleteFieldVector3Space(CompleteScalarField<E
        > scalarField) {
        this.scalarField = scalarField;
    }

    @SuppressWarnings("unchecked")
    public static <C extends CompleteScalarFieldElement<C>> CompleteFieldVector3Space<C> of(CompleteScalarField<C> field) {
        return (CompleteFieldVector3Space<C>) INSTANCES.computeIfAbsent(field, k -> new CompleteFieldVector3Space<>(field));
    }

    @Override
    public int getDimension() {
        return 3;
    }

    @Override
    public CompleteFieldVector3<E> zero() {
        return CompleteFieldVector3.of(scalarField.zero(), scalarField.zero(), scalarField.zero());
    }

    @Override
    public CompleteFieldVector3<E> nextRandom(Random random) {
        return CompleteFieldVector3.of(scalarField.nextRandom(random), scalarField.nextRandom(random), scalarField.nextRandom(random));
    }

    @Override
    public CompleteScalarField<E> getField() {
        return scalarField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompleteFieldVector3Space<?> that = (CompleteFieldVector3Space<?>) o;

        return scalarField.equals(that.scalarField);
    }

    @Override
    public int hashCode() {
        return scalarField.hashCode();
    }

    @Override
    public CompleteFieldVector3<E> one() {
        return CompleteFieldVector3.of(scalarField.one(), scalarField.one(), scalarField.one());
    }

    @Override
    public Cardinality getCardinality() {
        return scalarField.getCardinality();
    }

    @Override
    public Class<CompleteFieldVector3<E>> getElementClass() {
        return (Class<CompleteFieldVector3<E>>) zero().getClass();
    }
}
