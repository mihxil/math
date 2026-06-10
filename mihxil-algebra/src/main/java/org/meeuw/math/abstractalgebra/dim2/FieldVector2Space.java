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
package org.meeuw.math.abstractalgebra.dim2;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.NotParsable;
import org.meeuw.math.text.TextUtils;

/**
 * @author Michiel Meeuwissen
 */
public class FieldVector2Space<E extends ScalarFieldElement<E, C>, C extends CompleteScalarFieldElement<C>>
    implements VectorSpace<E, FieldVector2<E, C>> {

    private static final Map<ScalarField<?, ?>, FieldVector2Space<?, ?>> INSTANCES = new ConcurrentHashMap<>();

    final ScalarField<E, C> scalarField;

    public FieldVector2Space(ScalarField<E, C> scalarField) {
        this.scalarField = scalarField;
    }

    @SuppressWarnings("unchecked")
    public static <F extends ScalarFieldElement<F, C>, C extends CompleteScalarFieldElement<C>> FieldVector2Space<F, C> of(ScalarField<F, C> field) {
        return (FieldVector2Space<F, C>) INSTANCES.computeIfAbsent(field, k -> new FieldVector2Space<>(field));
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public FieldVector2<E, C> zero() {
        return FieldVector2.of(scalarField.zero(), scalarField.zero());
    }
    @Override
    public FieldVector2<E, C> nextRandom(Random random) {
        return FieldVector2.of(scalarField.nextRandom(random), scalarField.nextRandom(random));
    }

    @Override
    public ScalarField<E, C> getField() {
        return scalarField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldVector2Space<?, ?> that = (FieldVector2Space<?, ?>) o;

        return scalarField.equals(that.scalarField);
    }

    @Override
    public int hashCode() {
        return scalarField.hashCode();
    }

    @Override
    public FieldVector2<E, C> one() {
        return FieldVector2.of(scalarField.one(), scalarField.one());
    }

    @Override
    public Cardinality getCardinality() {
        return scalarField.getCardinality();
    }

    @Override
    public Class<FieldVector2<E, C>> getElementClass() {
        return (Class<FieldVector2<E, C>>) zero().getClass();
    }

    @Override
    public FieldVector2<E, C> fromString(String s) {
        String stripped = TextUtils.stripParentheses(s);
        String[] parts = stripped.split(",");
        if (parts.length != 2) {
            throw new NotParsable("Cannot parse " + s + " as " + this);
        }
        return FieldVector2.of(
            getField().fromString(parts[0]),
            getField().fromString(parts[1])
        );

    }
}
