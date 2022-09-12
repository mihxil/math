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
package org.meeuw.math.abstractalgebra;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.ArrayUtils;
import org.meeuw.math.validation.Square;

import static org.meeuw.math.ArrayUtils.minor;

/**
 * A ring is a {@link AdditiveGroup}, but also defines multiplication, though an inverse {@link MultiplicativeGroupElement#reciprocal()} is not defined (That would make it a {@link Field})
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Ring<E extends RingElement<E>> extends Rng<E> {

    E one();

     /**
     * Given a (square) matrix of elements of this Ring, calculate its determinant.
     * <p>
     * Using Leibniz formula
      *
      * @return the determinant of the give matrix
     */
    default E determinant(@Square E[][] source) {
        E det = zero();
        final int[] permutation = new int[source.length];
        for (int i = 0; i < permutation.length; i++) {
            permutation[i] = i;
        }
        E sign = one();

        while(true) {
            E product = sign;
            for (int i = 0; i < source.length; i++) {
                product = product.times(source[permutation[i]][i]);
            }
            det = det.plus(product);
            int swaps = ArrayUtils.permute(permutation);
            if (swaps == 0)   {
                break;
            }
            if (swaps % 2 == 1) {
                sign = sign.negation();
            }
        }
        return det;
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

    /**
     * Given a (square) matrix of elements of this Ring, calculate its 'adjugate' matrix.
     *
     * @return the (new) adjugate matrix
     */
     default @Square E[][] adjugate(@Square E[][] matrix) {
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


    default E[] product(E[]@NonNull [] matrix, E[] vector) {
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
