package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;

import org.meeuw.math.operators.AlgebraicUnaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;

/**
 * A general group , with one operation, and a 'unity' element.
 *
 * @see MultiplicativeGroup For a group where the operation is explicitely called 'multiplication'
 * @see AdditiveGroup       For a group where the operation is 'addition'.
 * @since 0.8
 */
public interface Group<E extends GroupElement<E>> extends Magma<E> {

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(Magma.UNARY_OPERATORS, BasicAlgebraicUnaryOperator.INVERSION);

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    E unity();

}
