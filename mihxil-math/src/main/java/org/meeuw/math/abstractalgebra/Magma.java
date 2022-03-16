package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;

import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;


/**
 * The most simple groupoid. Just defining one generic operation 'operate'.
 *
 * @see AdditiveSemiGroup        Where the operation is 'addition'
 * @see MultiplicativeSemiGroup  Where the operation is 'multiplication'
 * @since 0.8
 */
public interface Magma<E extends MagmaElement<E>> extends AlgebraicStructure<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(AlgebraicStructure.OPERATORS, BasicAlgebraicBinaryOperator.OPERATION);

    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    default boolean operationIsCommutative() {
        return false;
    }
}
