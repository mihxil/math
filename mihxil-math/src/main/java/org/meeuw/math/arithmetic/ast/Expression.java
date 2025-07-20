package org.meeuw.math.arithmetic.ast;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.AlgebraicStructure;

public interface Expression<E extends AlgebraicElement<E>> extends Comparable<Expression<E>> {

    E eval();

    default Expression<E> canonize(AlgebraicStructure<E> structure) {
        return this;
    };
}
