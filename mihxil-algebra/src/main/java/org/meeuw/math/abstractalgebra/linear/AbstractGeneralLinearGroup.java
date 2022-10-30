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
package org.meeuw.math.abstractalgebra.linear;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Objects;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.ArrayUtils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.streams.StreamUtils;
import org.meeuw.math.text.TextUtils;
import org.meeuw.math.validation.Square;

import static org.meeuw.math.ArrayUtils.squareMatrix;

@Log
public abstract class AbstractGeneralLinearGroup<
    M extends AbstractInvertibleMatrix<M, MS, E, ES>,
    MS extends AbstractGeneralLinearGroup<M, MS, E, ES>,
    E extends RingElement<E>,
    ES extends Ring<E>
    >
    extends AbstractAlgebraicStructure<M>
    implements MultiplicativeGroup<M>,
    Streamable<M>
{

    @Getter
    protected final int dimension;

    @Getter
    protected final ES elementStructure;

    protected final M one;


    protected AbstractGeneralLinearGroup(
        @NonNull ES elementStructure,
        int dimension) {
        super();
        this.dimension = dimension;
        this.elementStructure = elementStructure;
        this.one = of(one(elementStructure, dimension));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<M> stream() {
        if (elementStructure.getCardinality().compareTo(Cardinality.ALEPH_0) <=0) {
            return StreamUtils.cartesianStream(() -> ((Streamable<E>) elementStructure).stream(), dimension * dimension)
                .map(es -> {
                    try {
                        return newElement(es);
                    } catch (InvalidElementCreationException ive) {
                        log.info(() -> "Skipped " + ArrayUtils.toString(es) + ": " + ive.getMessage());
                        return null;
                    }
                    }
                )
                .filter(Objects::nonNull);
        } else {
            throw new NotStreamable();
        }
    }

    @SafeVarargs
    public final M newElement(@Square E... elements) throws InvalidElementCreationException {
        return newElement(squareMatrix(elementStructure.getElementClass(), elements));
    }

    abstract M of(@Square E[][] elements);

    public M newElement(@Square E[][] matrix) throws InvalidElementCreationException {
        if (matrix.length != dimension) {
            throw new InvalidElementCreationException("Dimension of matrix is not " + dimension + " (but " + matrix.length + ")");
        }
        M m = of(matrix);
        if (m.determinant().eq(elementStructure.zero())) {
            throw new InvalidElementCreationException("The matrix " + m + " is not invertible. Its determinant is zero");
        }
        return m;
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return dimension < 2;
    }

    @EqualsAndHashCode
    public static class Key {
        final int dimension;
        final Ring<?> field;

        public Key(int dimension, Ring<?> field) {
            this.dimension = dimension;
            this.field = field;
        }
    }

    @Override
    public Cardinality getCardinality() {
        return elementStructure.getCardinality();
    }

    @Override
    public M one() {
        return one;
    }

    @Override
    public String toString() {
        return "GL" + TextUtils.subscript(dimension) + "(" + elementStructure.toString() + ")";
    }

    protected static <E extends RingElement<E>> E[][] one(Ring<E> elementStructure, int dimension) {
        E[][] values = elementStructure.newMatrix(dimension, dimension);
        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++)
                if (i == j) {
                    values[i][j] = elementStructure.one();
                } else {
                    values[i][j] = elementStructure.zero();
                }
        }
        return values;
    }
}
