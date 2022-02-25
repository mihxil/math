package org.meeuw.math.abstractalgebra;

public interface Group<E extends AlgebraicElement<E>> extends AlgebraicStructure<E> {

    default boolean operationIsCommutative() {
        return false;
    }

    E unity();

}
