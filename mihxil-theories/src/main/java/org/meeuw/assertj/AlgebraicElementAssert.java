package org.meeuw.assertj;

import java.util.Optional;

import org.assertj.core.api.AbstractObjectAssert;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

public class AlgebraicElementAssert<E extends AlgebraicElement<E>> extends AbstractObjectAssert<AlgebraicElementAssert<E>, E> {

    protected boolean includeClassNames = false;

    protected String expectedDescription = "expected";

    protected String valueDescription = "";


    protected AlgebraicElementAssert(E o) {
        super(o, AlgebraicElementAssert.class);
    }

    @SuppressWarnings("UnusedReturnValue")
    public AlgebraicElementAssert<E> isEqTo(E expected) {
        if (!actual.eq(expected)) {
            assertionError("%s %s≉ %s (%s)".formatted(toString(actual), valueDescription.isEmpty() ? "" : " (" + valueDescription + ") ", toString(expected), expectedDescription));
        }
        return myself;
    }

    @SuppressWarnings("UnusedReturnValue")
    public AlgebraicElementAssert<E> containsEq(E expected) {
        if (!actual.eq(expected)) {
            assertionError("%s %s≉ %s (%s)".formatted(toString(actual), valueDescription.isEmpty() ? "" : " (" + valueDescription + ") ", toString(expected), expectedDescription));
        }
        return myself;
    }

    @SuppressWarnings("UnusedReturnValue")
    public AlgebraicElementAssert<E> isNotEqTo(E other) {
        if (actual.eq(other)) {
            assertionError("%s %s≈ %s (%s)".formatted(toString(actual), valueDescription.isEmpty() ? "" : " (" + valueDescription + ") ", toString(other), expectedDescription));
        }
        return myself;
    }

    protected String toString(E element) {
        return (includeClassNames ? element.getClass().getSimpleName() + " " : "" ) + element.toString();
    }

    /**
     * @since 0.19
     */
    public AlgebraicElementAssert<E> withIncludeClassNames(boolean b) {
         includeClassNames= b;
         return myself;
    }

    /**
     * @since 0.19
     */
    public AlgebraicElementAssert<E> withExpectedDescription(String s) {
         expectedDescription = s;
         return myself;
    }

    /**
     * @since 0.19
     */
    public AlgebraicElementAssert<E> withValueDescription(String s) {
         valueDescription = s;
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
