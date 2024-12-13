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
package org.meeuw.math.abstractalgebra.dim3;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 */
public class FieldVector3Space<E extends ScalarFieldElement<E>>
    implements VectorSpace<E, FieldVector3<E>> {

    private static final Map<ScalarField<?>, FieldVector3Space<?>> INSTANCES = new ConcurrentHashMap<>();

    final ScalarField<E> scalarField;

    public FieldVector3Space(ScalarField<E> scalarField) {
        this.scalarField = scalarField;
    }

    @SuppressWarnings("unchecked")
    public static <F extends ScalarFieldElement<F>> FieldVector3Space<F> of(ScalarField<F> field) {
        return (FieldVector3Space<F>) INSTANCES.computeIfAbsent(field, k -> new FieldVector3Space<>(field));
    }

    @Override
    public int getDimension() {
        return 3;
    }

    @Override
    public FieldVector3<E> zero() {
        return FieldVector3.of(scalarField.zero(), scalarField.zero(), scalarField.zero());
    }

    @Override
    public FieldVector3<E> nextRandom(Random random) {
        return FieldVector3.of(scalarField.nextRandom(random), scalarField.nextRandom(random), scalarField.nextRandom(random));
    }

    @Override
    public ScalarField<E> getField() {
        return scalarField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldVector3Space<?> that = (FieldVector3Space<?>) o;

        return scalarField.equals(that.scalarField);
    }

    @Override
    public int hashCode() {
        return scalarField.hashCode();
    }

    @Override
    public FieldVector3<E> one() {
        return FieldVector3.of(scalarField.one(), scalarField.one(), scalarField.one());
    }

    @Override
    public Cardinality getCardinality() {
        return scalarField.getCardinality();
    }

    @Override
    public Class<FieldVector3<E>> getElementClass() {
        return (Class<FieldVector3<E>>) zero().getClass();
    }
}
