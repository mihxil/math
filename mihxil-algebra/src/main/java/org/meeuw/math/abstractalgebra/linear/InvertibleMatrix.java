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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.vectorspace.NVector;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.validation.Square;

public class InvertibleMatrix<E extends FieldElement<E>>
    extends AbstractInvertibleMatrix<
    InvertibleMatrix<E>,
    GeneralLinearGroup<E>,
    E,
    Field<E>
    >
    implements WithScalarOperations<InvertibleMatrix<E>, E> {

    /**
     * General constructor, without checking.
     *
     * @param matrix An invertible, square matrix, with the dimensions specified by the structure
     */
    InvertibleMatrix(@NonNull GeneralLinearGroup<E> structure, @Square(invertible = true) E[][] matrix) {
        super(structure, matrix);
    }

    @SafeVarargs
    public static <E extends FieldElement<E>> InvertibleMatrix<E> of(Field<E> elementStructure, @NonNull @Square(invertible = true) E... matrix) {
        final int dim = IntegerUtils.sqrt(matrix.length);
        GeneralLinearGroup<E> structure = GeneralLinearGroup.of(dim, elementStructure);
        return of(structure, matrix);
    }

    @SafeVarargs
    public static <E extends FieldElement<E>> InvertibleMatrix<E> of(@Square(invertible = true) E... matrix) {
        return of(matrix[0].getStructure(), matrix);
    }


    @SafeVarargs
    static <E extends FieldElement<E>> InvertibleMatrix<E> of(GeneralLinearGroup<E> structure, @NonNull @Square(invertible = true) E... matrix) {
        int dimension = structure.getDimension();
        if (matrix.length != dimension * dimension) {
            throw new InvalidElementCreationException("Wrong dimensions");
        }
        E[][] invertibleMatrix = ArrayUtils.squareMatrix(structure.getElementStructure().getElementClass(), matrix);
        InvertibleMatrix<E> result = new InvertibleMatrix<>(structure, invertibleMatrix);
        result.determinant = result.structure.getElementStructure().determinant(invertibleMatrix);
        if (result.determinant.isZero()) {
            throw new InvalidElementCreationException("The matrix " + ArrayUtils.toString(invertibleMatrix) + " is not invertible");
        }
        return result;
    }


    public NVector<E> times(NVector<E> multiplier) {
        return NVector.of(structure.getElementStructure().product(matrix, multiplier.asArray()));
    }

    @Override
    public InvertibleMatrix<E> reciprocal() {
        return adjugate().dividedBy(determinant());
    }

    @Override
    InvertibleMatrix<E> of(@Square E[][] matrix) {
        return new InvertibleMatrix<>(structure, matrix);
    }

    @Override
    public InvertibleMatrix<E> dividedBy(E divisor) {
        E[][] result = newMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = matrix[i][j].dividedBy(divisor);
            }
        }
        return of(result);
    }

}
