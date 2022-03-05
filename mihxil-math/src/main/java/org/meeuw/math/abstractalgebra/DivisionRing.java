package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.ArrayUtils.cloneMatrix;
import static org.meeuw.math.ArrayUtils.swap;
import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.*;
import static org.meeuw.math.abstractalgebra.UnaryOperator.*;

/**
 * A division ring is a ring, where also the multiplicative inverse is defined, but where multiplication is not necessarily commutative.
 *
 * In other words it is  'skewed field', or  non-commutative field.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisionRing<E extends DivisionRingElement<E>> extends
    MultiplicativeGroup<E>,
    Ring<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(OPERATION, ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION);

    NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(IDENTIFY, NEGATION, RECIPROCAL);

    @Override
    E one();

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }


    /**
     * Given a (square) matrix of elements of this field, calculate its determinant.
     *
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
                if (!matrix[row][col].isZero()) {
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
                    E del = matrix[row][col].dividedBy(matrix[col][col]);
                    for (int j = col; j < n; ++j) {
                        matrix[row][j] = matrix[row][j].minus(del.times(matrix[col][j]));
                    }
                    if (matrix[row][col].isZero()) {
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
