package org.meeuw.assertj;

import org.assertj.core.api.AbstractAssert;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

public class AlgebraicElementAssert<E extends AlgebraicElement<E>> extends AbstractAssert<AlgebraicElementAssert<E>, E> {

    protected AlgebraicElementAssert(E o) {
        super(o, AlgebraicElementAssert.class);
    }

    @SuppressWarnings("UnusedReturnValue")
    public AlgebraicElementAssert<E> isEqTo(E other) {
        if (!actual.eq(other)) {
            throw new AssertionError(actual + " ≉ " + other);
        }
        return myself;
    }

}
