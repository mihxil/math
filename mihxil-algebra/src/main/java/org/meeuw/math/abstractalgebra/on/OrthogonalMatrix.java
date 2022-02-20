package org.meeuw.math.abstractalgebra.on;


import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.meeuw.math.MatrixUtils;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;

public class OrthogonalMatrix<E extends FieldElement<E>>
    implements MultiplicativeGroupElement<OrthogonalMatrix<E>>,
    WithScalarOperations<OrthogonalMatrix<E>, E> {

    final E[][] matrix;

    @Getter
    final OrthogonalGroup<E> structure;

    OrthogonalMatrix(OrthogonalGroup<E> structure, E[][] matrix) {
        this.matrix = matrix;
        this.structure = structure;
        if (determinant().eq(structure.getElementField().zero())) {
            //throw new Ill
        }
    }

    OrthogonalMatrix(E[][] matrix) {
        this.matrix = matrix;
        this.structure = OrthogonalGroup.of(this);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E extends ScalarFieldElement<E>> OrthogonalMatrix<E> of(Class<E> clazz, E... matrix) {
        int dim = (int) Utils.sqrt(matrix.length);
        E[][] eMatrix = (E[][]) Array.newInstance(clazz, dim, dim);
        for (int i = 0; i < dim; i++) {
            System.arraycopy(matrix, i * dim, eMatrix[i], 0, dim);
        }
        return new OrthogonalMatrix<>(eMatrix);
    }

    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <E extends ScalarFieldElement<E>> OrthogonalMatrix<E> of (E... matrix) {
        return of((Class<E>) matrix[0].getClass(), matrix);
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
        return new OrthogonalMatrix<>(result);
    }

    @Override
    public OrthogonalMatrix<E> reciprocal() {
        return adjugate().dividedBy(determinant());
    }

    public OrthogonalMatrix<E> adjugate() {
        return new OrthogonalMatrix<>(structure.getElementField().adjugate(matrix));
    }

    public E determinant() {
        return structure.getElementField().determinant(matrix);
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
        return new OrthogonalMatrix<>(result);
    }

    @Override
    public OrthogonalMatrix<E> dividedBy(E divisor) {
        E[][] result = newMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                result[i][j] = matrix[i][j].dividedBy(divisor);
            }
        }
        return new OrthogonalMatrix<>(result);
    }

    protected E[][] newMatrix() {
        return MatrixUtils.newMatrix(structure.getElementField().getElementClass(), matrix.length, matrix.length);
    }
}
