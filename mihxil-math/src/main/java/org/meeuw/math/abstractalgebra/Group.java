package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;


/**
 * A general group , with one operation, and a 'unity' element.
 *
 * @see MultiplicativeGroup For a group where the operation is explicitely called 'multiplication'
 * @see AdditiveGroup       For a group where the operation is 'addition'.
 */
public interface Group<E extends AlgebraicElement<E>> extends AlgebraicStructure<E> {

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return navigableSet(Operator.OPERATION);
    }

    default boolean operationIsCommutative() {
        return false;
    }

    E unity();

}
