package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;


/**
 * The most simple groupoid. Just defining one generic operation 'operate'.
 *
 * @see AdditiveSemiGroup        Where the operation is 'addition'
 * @see MultiplicativeSemiGroup  Where the operation is 'multiplication'
 * @since 0.8
 */
public interface Magma<E extends MagmaElement<E>> extends AlgebraicStructure<E> {

    NavigableSet<Operator> OPERATORS = navigableSet(AlgebraicStructure.OPERATORS, Operator.OPERATION);

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    default boolean operationIsCommutative() {
        return false;
    }
}
