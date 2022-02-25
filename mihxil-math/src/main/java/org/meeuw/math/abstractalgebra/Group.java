package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import static org.meeuw.math.Utils.navigableSet;

public interface Group<E extends AlgebraicElement<E>> extends AlgebraicStructure<E> {

    @Override
    default NavigableSet<Operator> getSupportedOperators() {
        return navigableSet(Operator.OPERATE);
    }

    default boolean operationIsCommutative() {
        return false;
    }

    E unity();

}
