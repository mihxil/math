package org.meeuw.math.abstractalgebra.gl;


import lombok.Getter;

import java.util.Arrays;

import org.checkerframework.checker.nullness.qual.*;
import org.meeuw.math.MatrixUtils;
import org.meeuw.math.abstractalgebra.*;


/**
 * Representation of a square, invertible matrix.
 *
 *
 */
public abstract class AbstractInvertibleMatrix<
    M extends AbstractInvertibleMatrix<M, MS, E, ES>,
    MS extends AbstractGeneralLinearGroup<M, MS, E, ES>,
    E extends RingElement<E>,
    ES extends Ring<E>
    >
    implements MultiplicativeGroupElement<M> {

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
    AbstractInvertibleMatrix(@NonNull MS structure, E[][] matrix) {
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
        return MatrixUtils.toString(matrix);
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



    abstract M of(E[][] matrix);

    protected E[][] newMatrix() {
        return structure.getElementStructure().newMatrix(matrix.length, matrix.length);
    }
}
