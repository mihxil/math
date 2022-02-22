package org.meeuw.math.abstractalgebra.gl;


import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.math.MatrixUtils;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.vectorspace.NVector;
import org.meeuw.math.exceptions.InvalidElementCreationException;


/**
 * Representation of a square, invertible matrix.
 */
public class InvertibleMatrix<E extends FieldElement<E>>
    implements MultiplicativeGroupElement<InvertibleMatrix<E>>,
    WithScalarOperations<InvertibleMatrix<E>, E> {

    final E[][] matrix;

    @Getter
    final GeneralLinearGroup<E> structure;

    final E determinant;

    InvertibleMatrix(@Nullable GeneralLinearGroup<E> structure, E[][] matrix) {
        this.matrix = matrix;
        this.structure = structure == null ? GeneralLinearGroup.of(this) : structure;
        this.determinant = this.structure.getElementField().determinant(matrix);
        if (this.determinant.isZero()) {
            throw new InvalidElementCreationException("The matrix " + MatrixUtils.toString(matrix) + " is not invertible");
        }
    }

    InvertibleMatrix(E[][] matrix) {
        this(null, matrix);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E extends FieldElement<E>> InvertibleMatrix<E> of(Class<E> clazz, E... matrix) {
        int dim = (int) Utils.sqrt(matrix.length);
        E[][] eMatrix = (E[][]) Array.newInstance(clazz, dim, dim);
        for (int i = 0; i < dim; i++) {
            System.arraycopy(matrix, i * dim, eMatrix[i], 0, dim);
        }
        return new InvertibleMatrix<>(eMatrix);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E extends FieldElement<E>> InvertibleMatrix<E> of(E... matrix) {
        return of((Class<E>) matrix[0].getClass(), matrix);
    }


    @Override
    public InvertibleMatrix<E> times(InvertibleMatrix<E> multiplier) {
        return of(getStructure().getElementField().product(matrix, multiplier.matrix));
    }

    public NVector<E> times(NVector<E> multiplier) {
        return NVector.of(getStructure().getElementField().product(matrix, multiplier.asArray()));
    }

    @Override
    public InvertibleMatrix<E> reciprocal() {
        return adjugate().dividedBy(determinant);
    }

    public InvertibleMatrix<E> adjugate() {
        return of(structure.getElementField().adjugate(matrix));
    }

    public E determinant() {
        return determinant;
    }


    @Override
    public String toString() {
        return MatrixUtils.toString(matrix);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvertibleMatrix<?> that = (InvertibleMatrix<?>) o;

        return Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

    @Override
    public InvertibleMatrix<E> times(E multiplier) {
        E[][] result = newMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = matrix[i][j].times(multiplier);
            }
        }
        return of(result);
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

    protected InvertibleMatrix<E> of(E[][] matrix) {
        return new InvertibleMatrix<>(matrix);
    }

    protected E[][] newMatrix() {
        return structure.getElementField().newMatrix(matrix.length, matrix.length);
    }
}
