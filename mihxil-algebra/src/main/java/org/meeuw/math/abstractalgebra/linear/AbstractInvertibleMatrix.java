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
package org.meeuw.math.abstractalgebra.linear;


import lombok.Getter;

import java.util.Arrays;

import org.checkerframework.checker.nullness.qual.*;
import org.meeuw.math.ArrayUtils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.validation.Square;


/**
 * Representation of a square, invertible matrix.
 *
 */
public abstract class AbstractInvertibleMatrix<
    M extends AbstractInvertibleMatrix<M, MS, E, ES>,
    MS extends AbstractGeneralLinearGroup<M, MS, E, ES>,
    E extends RingElement<E>,
    ES extends Ring<E>
    >
    implements MultiplicativeGroupElement<M> {

    @Square(invertible = true)
    final E[][] matrix;

    @Getter
    final MS structure;

    @MonotonicNonNull
    E determinant;

    /**
     * General constructor, without checking.
     *
     * @param structure
     * @param matrix An invertible, square matrix, with the dimensions specified by the structure
     */
    AbstractInvertibleMatrix(@NonNull MS structure, @Square(invertible = true) E[][] matrix) {
        this.matrix = matrix;
        this.structure = structure;
    }

    @Override
    public M times(M multiplier) {
        return of(getStructure().getElementStructure().product(matrix, multiplier.matrix));
    }

    @Override
    public abstract M reciprocal();

    public M adjugate() {
        return of(structure.getElementStructure().adjugate(matrix));
    }

    @EnsuresNonNull("determinant")
    public E determinant() {
        if (determinant == null) {
            determinant = structure.getElementStructure().determinant(matrix);
        }
        return determinant;
    }

    @Override
    public String toString() {
        return ArrayUtils.toString(matrix);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        M that = (M) o;

        return Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

    public M times(E multiplier) {
        E[][] result = newMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = matrix[i][j].times(multiplier);
            }
        }
        return of(result);
    }



    abstract M of(@Square E[][] matrix);

    protected E[][] newMatrix() {
        return structure.getElementStructure().newMatrix(matrix.length, matrix.length);
    }
}
