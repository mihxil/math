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
import org.meeuw.math.abstractalgebra.Ring;
import org.meeuw.math.abstractalgebra.RingElement;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.validation.Square;

public class SpecialLinearMatrix<E extends RingElement<E>>
    extends AbstractInvertibleMatrix<
    SpecialLinearMatrix<E>,
    SpecialLinearGroup<E>,
    E,
    Ring<E>
    > {

    /**
     * General constructor, without checking.
     *
     * @param matrix An invertible, square matrix, with the dimensions specified by the structure
     */
    SpecialLinearMatrix(@NonNull SpecialLinearGroup<E> structure, @Square E[][] matrix) {
        super(structure, matrix);
    }


    @SafeVarargs
    public static <E extends RingElement<E>> SpecialLinearMatrix<E> of(@Square E... matrix) {
        return of(matrix[0].getStructure(), matrix);
    }

    @SafeVarargs
    public static <E extends RingElement<E>> SpecialLinearMatrix<E> of(Ring<E> elementStructure, @NonNull @Square E... matrix) {
        final int dim = IntegerUtils.sqrt(matrix.length);
        SpecialLinearGroup<E> structure = SpecialLinearGroup.of(dim, elementStructure);
        return of(structure, matrix);
    }

    @SafeVarargs
    private static <E extends RingElement<E>> SpecialLinearMatrix<E> of(SpecialLinearGroup<E> structure, @NonNull @Square E... matrix) {
        // no need to check arguments
        E[][] invertibleMatrix = ArrayUtils.squareMatrix(structure.getElementStructure().getElementClass(), matrix);
        SpecialLinearMatrix<E> result = new SpecialLinearMatrix<>(structure, invertibleMatrix);
        result.determinant = result.structure.getElementStructure().determinant(invertibleMatrix);
        if (result.determinant.isZero()) {
            throw new InvalidElementCreationException("The matrix " + ArrayUtils.toString(invertibleMatrix) + " is not invertible. Determinant: " + result.determinant);
        }
        if (! (result.determinant().eq(structure.getElementStructure().one()) ||
            result.determinant().eq(structure.getElementStructure().one().negation()))
        ) {
            throw new InvalidElementCreationException("The matrix " + ArrayUtils.toString(invertibleMatrix) + " is not invertible. Determinant: " + result.determinant);
        }

        return result;
    }

    @Override
    public SpecialLinearMatrix<E> reciprocal() {
        return adjugate()
            .times(determinant()); // we're only interested in the sign of the determinant, it is either 1 or -1.
    }

    @Override
    SpecialLinearMatrix<E> of(@Square E[][] matrix) {
        return structure.of(matrix);
    }


}
