package org.meeuw.assertj;

import org.assertj.core.api.AbstractObjectAssert;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

public class AlgebraicElementAssert<E extends AlgebraicElement<E>> extends AbstractObjectAssert<AlgebraicElementAssert<E>, E> {

    protected AlgebraicElementAssert(E o) {
        super(o, AlgebraicElementAssert.class);
    }

    @SuppressWarnings("UnusedReturnValue")
    public AlgebraicElementAssert<E> isEqTo(E expected) {
        if (!actual.eq(expected)) {
            assertionError(actual + " ≉ " + expected + "(expected)");
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
