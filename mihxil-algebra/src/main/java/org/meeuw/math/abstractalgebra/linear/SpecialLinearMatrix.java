package org.meeuw.math.abstractalgebra.linear;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.ArrayUtils;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.NotASquareException;

public class SpecialLinearMatrix<E extends RingElement<E>>
    extends AbstractInvertibleMatrix<
    SpecialLinearMatrix<E>,
    SpecialLinearGroup<E>,
    E,
    Ring<E>
    >{

    /**
     * General constructor, without checking.
     *
     * @param matrix An invertible, square matrix, with the dimensions specified by the structure
     */
    SpecialLinearMatrix(@NonNull SpecialLinearGroup<E> structure, E[][] matrix) {
        super(structure, matrix);
    }


    @SafeVarargs
    public static <E extends RingElement<E>> SpecialLinearMatrix<E> of(E... matrix) {
        return of(matrix[0].getStructure(), matrix);
    }

    @SafeVarargs
    public static <E extends RingElement<E>> SpecialLinearMatrix<E> of(Ring<E> elementStructure, @NonNull E... matrix) {
        try {
            final int dim = Utils.sqrt(matrix.length);
            SpecialLinearGroup<E> structure = SpecialLinearGroup.of(dim, elementStructure);
            return of(structure, matrix);
        } catch (NotASquareException notASquareException) {
            throw new InvalidElementCreationException(notASquareException);
        }
    }

    @SafeVarargs
    static <E extends RingElement<E>> SpecialLinearMatrix<E> of(SpecialLinearGroup<E> structure, @NonNull E... matrix) {
        try {
            int dimension = structure.getDimension();
            if (matrix.length != dimension * dimension) {
                throw new InvalidElementCreationException("Wrong dimensions");
            }
            E[][] invertibleMatrix = ArrayUtils.squareMatrix(structure.getElementStructure().getElementClass(), matrix);
            SpecialLinearMatrix<E> result = new SpecialLinearMatrix<>(structure, invertibleMatrix);
            result.determinant = result.structure.getElementStructure().determinant(invertibleMatrix);
            if (result.determinant.isZero()) {
                throw new InvalidElementCreationException("The matrix " + ArrayUtils.toString(invertibleMatrix) + " is not invertible");
            }
            if (! (result.determinant().eq(structure.getElementStructure().one()) ||
                result.determinant().eq(structure.getElementStructure().one().negation()))
            ) {
                throw new InvalidElementCreationException("The matrix " + ArrayUtils.toString(invertibleMatrix) + " is not invertible");
            }

            return result;
        } catch (NotASquareException notASquareException) {
            throw new InvalidElementCreationException(notASquareException);
        }
    }

    @Override
    public SpecialLinearMatrix<E> reciprocal() {
        return adjugate()
            .times(determinant()); // we're only interested in the sign of the determinant, it is either 1 or -1.
    }

    @Override
    SpecialLinearMatrix<E> of(E[][] matrix) {
        return structure.of(matrix);
    }


}
