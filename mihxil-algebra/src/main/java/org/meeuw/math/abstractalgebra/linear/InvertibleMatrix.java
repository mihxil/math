package org.meeuw.math.abstractalgebra.linear;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.ArrayUtils;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.vectorspace.NVector;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.NotASquareException;

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
    InvertibleMatrix(@NonNull GeneralLinearGroup<E> structure, E[][] matrix) {
        super(structure, matrix);
    }

    @SafeVarargs
    public static <E extends FieldElement<E>> InvertibleMatrix<E> of(Field<E> elementStructure, @NonNull E... matrix) {
        try {
            final int dim = Utils.sqrt(matrix.length);
            GeneralLinearGroup<E> structure = GeneralLinearGroup.of(dim, elementStructure);
            return of(structure, matrix);
        } catch (NotASquareException notASquareException) {
            throw new InvalidElementCreationException(notASquareException);
        }
    }

    @SafeVarargs
    public static <E extends FieldElement<E>> InvertibleMatrix<E> of(E... matrix) {
        return of(matrix[0].getStructure(), matrix);
    }


    @SafeVarargs
    static <E extends FieldElement<E>> InvertibleMatrix<E> of(GeneralLinearGroup<E> structure, @NonNull E... matrix) {
        try {
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
        } catch (NotASquareException notASquareException) {
            throw new InvalidElementCreationException(notASquareException);
        }
    }


    public NVector<E> times(NVector<E> multiplier) {
        return NVector.of(structure.getElementStructure().product(matrix, multiplier.asArray()));
    }

    @Override
    public InvertibleMatrix<E> reciprocal() {
        return adjugate().dividedBy(determinant());
    }

    @Override
    InvertibleMatrix<E> of(E[][] matrix) {
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
