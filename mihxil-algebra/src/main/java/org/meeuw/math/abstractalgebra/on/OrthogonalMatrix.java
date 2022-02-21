package org.meeuw.math.abstractalgebra.on;


import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.math.MatrixUtils;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;


public class OrthogonalMatrix<E extends FieldElement<E>>
    implements MultiplicativeGroupElement<OrthogonalMatrix<E>>,
    WithScalarOperations<OrthogonalMatrix<E>, E> {

    final E[][] matrix;

    @Getter
    final OrthogonalGroup<E> structure;

    final E determinant;

    OrthogonalMatrix(@Nullable OrthogonalGroup<E> structure, E[][] matrix, boolean orthogonal) {
        this.matrix = matrix;
        this.structure = structure == null ? OrthogonalGroup.of(this, orthogonal) : structure;
        this.determinant = this.structure.getElementField().determinant(matrix);

        if (this.structure.isOrthogonal() && (! (determinant.isOne() || determinant.equals(this.structure.getElementField().one().negation())))) {
            throw new InvalidElementCreationException("Not orthogonal");
        }
    }

    OrthogonalMatrix(E[][] matrix, boolean orthogonal) {
        this(null, matrix, orthogonal);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E extends ScalarFieldElement<E>> OrthogonalMatrix<E> of(Class<E> clazz, boolean orthogonal, E... matrix) {
        int dim = (int) Utils.sqrt(matrix.length);
        E[][] eMatrix = (E[][]) Array.newInstance(clazz, dim, dim);
        for (int i = 0; i < dim; i++) {
            System.arraycopy(matrix, i * dim, eMatrix[i], 0, dim);
        }
        return new OrthogonalMatrix<>(eMatrix, orthogonal);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E extends ScalarFieldElement<E>> OrthogonalMatrix<E> of(E... matrix) {
        return of((Class<E>) matrix[0].getClass(), false, matrix);
    }

    @Override
    public OrthogonalMatrix<E> times(OrthogonalMatrix<E> multiplier) {
        E[][] result = newMatrix();
        E z = structure.getElementField().zero();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i][j] = z;
                for (int k = 0; k < matrix.length; k++) {
                    result[i][j] = result[i][j].plus(matrix[i][k].times(multiplier.matrix[k][j]));
                }
            }
        }
        return of(result);
    }

    @Override
    public OrthogonalMatrix<E> reciprocal() {
        return adjugate().dividedBy(determinant);
    }

    public OrthogonalMatrix<E> adjugate() {
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

        OrthogonalMatrix<?> that = (OrthogonalMatrix<?>) o;

        return Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }

    @Override
    public OrthogonalMatrix<E> times(E multiplier) {
        E[][] result = newMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i][j] = matrix[i][j].times(multiplier);
            }
        }
        return of(result);
    }

    @Override
    public OrthogonalMatrix<E> dividedBy(E divisor) {
        E[][] result = newMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i][j] = matrix[i][j].dividedBy(divisor);
            }
        }
        return of(result);
    }

    protected OrthogonalMatrix<E> of(E[][] matrix) {
        return new OrthogonalMatrix<>(matrix, structure.isOrthogonal());
    }

    protected E[][] newMatrix() {
        return MatrixUtils.newMatrix(structure.getElementField().getElementClass(), matrix.length, matrix.length);
    }
}
