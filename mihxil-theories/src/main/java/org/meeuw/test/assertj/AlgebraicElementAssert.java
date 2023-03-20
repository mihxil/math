package org.meeuw.test.assertj;

import org.assertj.core.api.AbstractAssert;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

public class AlgebraicElementAssert<E extends AlgebraicElement<E>> extends AbstractAssert<AlgebraicElementAssert<E>, E> {

    protected AlgebraicElementAssert(E o) {
        super(o, AlgebraicElementAssert.class);
    }

    @SuppressWarnings("UnusedReturnValue")
    public AlgebraicElementAssert<E> isEqTo(E other) {
        if (!actual.eq(other)) {
            assertionError(actual + " ≉ " + other);
        }
        return myself;
    }

    @SuppressWarnings("UnusedReturnValue")
    public AlgebraicElementAssert<E> isNotEqTo(E other) {
        if (actual.eq(other)) {
            assertionError(actual + " ≈ " + other);
        }
        return myself;
    }

    void assertionError(String fallback) {
        String error = info.overridingErrorMessage();
        if (error == null) {
            throw new AssertionError(fallback);
        } else {
            throw new AssertionError(error);
        }
    }

}
