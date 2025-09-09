package org.meeuw.assertj;

import java.util.Optional;

import org.assertj.core.api.AbstractObjectAssert;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OptionalAlgebraicElementAssert<E extends AlgebraicElement<E>> extends AbstractObjectAssert<OptionalAlgebraicElementAssert<E>, Optional<E>> {


    protected OptionalAlgebraicElementAssert(Optional<E> o) {
        super(o, OptionalAlgebraicElementAssert.class);
    }



    @SuppressWarnings("UnusedReturnValue")
    public OptionalAlgebraicElementAssert<E> containsEq(E expected) {
        if (actual.isEmpty()) {
            assertionError("Expected to contain %s, but was empty".formatted(expected));
        }
        if (!actual.get().eq(expected)) {
            assertionError("%s ≉ %s".formatted(expected, actual.get()));
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
