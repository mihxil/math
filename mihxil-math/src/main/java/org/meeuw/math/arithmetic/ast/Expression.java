package org.meeuw.math.arithmetic.ast;

import org.meeuw.math.abstractalgebra.*;

public interface Expression<E extends FieldElement<E>>  extends Comparable<Expression<E>> {

    E eval();


    default Expression<E> canonize(AlgebraicStructure<E> structure) {
        return this;
    };


}
