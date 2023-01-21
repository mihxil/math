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

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.Ring;
import org.meeuw.math.abstractalgebra.RingElement;
import org.meeuw.math.abstractalgebra.integers.IntegerElement;
import org.meeuw.math.abstractalgebra.integers.Integers;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.text.TextUtils;


/**
 * As long as division is not required for inversion, a general linear group can also be defined on a {@link Ring}.
 * <p>
 * This requirement boils down to the {@link AbstractInvertibleMatrix#determinant()} to be plus or minus one.
 * @param <E>  The type of elements of the {@link SpecialLinearMatrix} that are elements of this group.
 */
public class SpecialLinearGroup<E extends RingElement<E>> extends
    AbstractGeneralLinearGroup<
        SpecialLinearMatrix<E>,
        SpecialLinearGroup<E>,
        E,
        Ring<E>
        > implements Randomizable<SpecialLinearMatrix<E>> {


    private static final Map<Key, SpecialLinearGroup<?>> INSTANCES = new ConcurrentHashMap<>();


    @SuppressWarnings("unchecked")
    public static <E extends RingElement<E>> SpecialLinearGroup<E> of(final int length, final Ring<E> elementStructure) {
        final Key key = new Key(length, elementStructure);
        return (SpecialLinearGroup<E>) INSTANCES.computeIfAbsent(key, k -> new SpecialLinearGroup<E>(
            elementStructure,
            k.dimension
        ));
    }

    @Example(Ring.class)
    public static SpecialLinearGroup<IntegerElement> SL_N = SpecialLinearGroup.of(3, Integers.INSTANCE);


    protected SpecialLinearGroup(@NonNull Ring<E> elementStructure, int dimension) {
        super(elementStructure, dimension);
    }

    @Override
    SpecialLinearMatrix<E> of(/*@Square(invertible = true)*/ E[][] elements) {
        return  new SpecialLinearMatrix<>(this, elements);
    }

    @Override
    public SpecialLinearMatrix<E> newElement(/* @Square(invertible = true)*/ E[][] matrix) throws InvalidElementCreationException {
        SpecialLinearMatrix<E> m = super.newElement(matrix);
        E det = m.determinant();
        if (! (det.eq(elementStructure.one()) || det.eq(elementStructure.one().negation()))) {
            throw new InvalidElementCreationException("The matrix " + m + " is not invertible, because det=" + det + " !in (1,-1)");
        }
        return m;
    }


    @Override
    public String toString() {
        return "SL" + TextUtils.subscript(dimension) + "(" + elementStructure.toString() + ")";
    }

    @Override
    public Stream<SpecialLinearMatrix<E>> stream() {
        return super.stream();
    }


    @Override
    public SpecialLinearMatrix<E> nextRandom(Random random) {
        E[][] matrix = ArrayUtils.newSquareMatrix(elementStructure.getElementClass(), dimension);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == j) {
                    matrix[i][j] = elementStructure.one();
                    if (random.nextBoolean()) {
                        matrix[i][j]  = matrix[i][j].negation();
                    }
                } else if (i < j) {
                    matrix[i][j] = elementStructure.zero();
                } else {
                    matrix[i][j] = ((Randomizable<E>) elementStructure).nextRandom(random);
                }
            }
        }
        ArrayUtils.shuffle(random, matrix);

        return of(matrix);
    }
}

