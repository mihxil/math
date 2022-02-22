package org.meeuw.math.abstractalgebra;

import org.checkerframework.checker.nullness.qual.NonNull;

import static org.meeuw.math.MatrixUtils.cloneMatrix;
import static org.meeuw.math.MatrixUtils.minor;

/**
 * <a href="https://en.wikipedia.org/wiki/Field_(mathematics)">Field</a>
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Field<E extends FieldElement<E>> extends
    DivisionRing<E>,
    AbelianRing<E>,
    DivisibleGroup<E> {

    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    E one();


    /**
     * Given a (square) matrix of elements of this field, calculate its determinant.
     *
     * Using Gaussian elimination.
     */
    default E determinant(E[][] source) {
        // make a copy of the matrix first, since we're going to modify it.
        E[][] matrix = cloneMatrix(source[0][0].getStructure().getElementClass(), source);
        E z = zero();
        E det = one();

        for (int i = 0; i < matrix.length; ++i) {
            boolean flag = false; // flag := are all entries below a[i][i] including it zero?
            if (matrix[i][i].equals(z)) { // If a[i][i] is zero then check rows below and  swap
                flag = true;
                for (int j = i; j < matrix.length; ++j) {
                    if (!matrix[j][i].equals(z)) {
                        det = det.negation();
                        // swap rows
                        E[] rowa = matrix[i];
                        E[] rowb = matrix[j];
                        matrix[j] = rowa;
                        matrix[i] = rowb;
                        flag = false;
                    }
                }
            }
            if (flag) {
                det = z;
                break;
            } else {
                for (int j = i+1; j < matrix.length; ++j) {
                    E store = matrix[j][i];
                    for (int k = i; k < matrix.length; ++k) {
                        matrix[j][k] = matrix[j][k].minus(matrix[i][k].times(store).dividedBy(matrix[i][i]));
                    }
                }
                det = det.times(matrix[i][i]);
            }
        }
        return det;
    }

    /**
     * Given a (square) matrix of elements of this field, calculate its 'adjugate' matrix.
     *
     */
    default E[][] adjugate(E[][] matrix) {
        final E[][] adjugate = newMatrix(matrix.length, matrix.length);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                E[][] minor = minor(getElementClass(), matrix, i, j);
                adjugate[j][i] = determinant(minor);
                //System.out.println("|" + MatrixUtils.toString(minor) + "| = " + adjugate[j][i]);
                if ((i + j) % 2 == 1) {
                    adjugate[j][i] = adjugate[j][i].negation();
                }
            }
        }
        return adjugate;
    }

    default E[][] product(E[][] matrix1, E[][] matrix2) {
        E[][] result = newMatrix(matrix1.length, matrix2.length);
        E z = zero();
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[i].length; j++) {
                result[i][j] = z;
                for (int k = 0; k < matrix1.length; k++) {
                    result[i][j] = result[i][j].plus(matrix1[i][k].times(matrix2[k][j]));
                }
            }
        }
        return result;
    }

    default E[] product(E[]@NonNull[] matrix, E[] vector) {
        E[] result = newArray(matrix.length);
        for (int i = 0; i < matrix.length; i++) {
            result[i] = zero();
            for (int j = 0; j < matrix[i].length; j++) {
                result[i] = result[i].plus(matrix[i][j].times(vector[j]));
            }
        }
        return result;
    }



}
