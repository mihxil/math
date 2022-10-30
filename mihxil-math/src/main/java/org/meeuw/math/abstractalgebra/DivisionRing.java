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
package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.operators.*;

import static org.meeuw.math.ArrayUtils.cloneMatrix;
import static org.meeuw.math.ArrayUtils.swap;
import static org.meeuw.math.CollectionUtils.navigableSet;

/**
 * A division ring is a ring, where also the multiplicative inverse is defined, but where multiplication is not necessarily commutative.
 * <p>
 * In other words it is  'skewed field', or  non-commutative field.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisionRing<E extends DivisionRingElement<E>> extends
    @NonAlgebraic  MultiplicativeGroup<E>,
    MultiplicativeMonoid<E>,
    Ring<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(MultiplicativeGroup.OPERATORS, Ring.OPERATORS);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(MultiplicativeGroup.UNARY_OPERATORS, Ring.UNARY_OPERATORS);

    @Override
    E one();

    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    default BasicAlgebraicBinaryOperator groupOperator() {
        return ConfigurationService.getConfigurationAspect(GenericGroupConfiguration.class).getGroupOperator();
    }
    @Override
    default E unity() {
        return groupOperator().unity(this);
    }

    /**
     * Given a (square) matrix of elements of this field, calculate its determinant.
     * <p>
     * Using Gaussian elimination.
     */
    @Override
    default E determinant(E[][] source) {
        // make a copy of the matrix first, since we're going to modify it.
        E[][] matrix = cloneMatrix(source[0][0].getStructure().getElementClass(), source);
        final E z = zero();

        int n = matrix.length;
        int swaps = 0;
        for(int col = 0; col < n; ++col) {
            boolean found = false;
            for(int row = col; row < n; ++row) {
                if (! z.eq(matrix[row][col])) {
                    if ( row != col ) {
                        swap(matrix, row, col);
                        swaps++;
                    }
                    found = true;
                    break;
                }
            }

            if(!found) {
                return z;
            }

            for(int row = col + 1; row < n; ++row) {
                while(true) {
                    final E del = matrix[row][col].dividedBy(matrix[col][col]);
                    for (int j = col; j < n; ++j) {
                        matrix[row][j] = matrix[row][j].minus(del.times(matrix[col][j]));
                    }
                    if (z.eq(matrix[row][col])) {
                        break;
                    } else {
                        swap(matrix, row, col);
                        swaps++;
                    }
                }
            }
        }
        E det = swaps % 2 == 0 ? one() : one().negation();
        for(int i = 0; i < n; ++i) {
            det = det.times(matrix[i][i]);
        }
        return det;
    }

}
