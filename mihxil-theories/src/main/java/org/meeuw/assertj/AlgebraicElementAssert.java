package org.meeuw.assertj;

import org.assertj.core.api.AbstractObjectAssert;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

public class AlgebraicElementAssert<E extends AlgebraicElement<E>> extends AbstractObjectAssert<AlgebraicElementAssert<E>, E> {

    protected boolean includeClassNames = false;

    protected String expectedDescription = "expected";

    protected String actualDescription = "";


    protected AlgebraicElementAssert(E o) {
        super(o, AlgebraicElementAssert.class);
    }

    @SuppressWarnings("UnusedReturnValue")
    public AlgebraicElementAssert<E> isEqTo(E expected) {
        if (!actual.eq(expected)) {
            if (actual instanceof UncertainNumber<?> uncertainActual) {
                assertionError(
                    "%s %s ≉ %s (%s)".formatted(
                        toString(actual) + " " + uncertainActual.getConfidenceInterval(),
                        info.hasDescription() ?  "(" + info.descriptionText() + ") " : "",
                        toString(expected) + " " + (expected instanceof UncertainNumber<?> uncertainExpected ? uncertainExpected.getConfidenceInterval() : ""),
                        expectedDescription
                    ));
            } else {
                assertionError(
                    "%s %s ≉ %s (%s)".formatted(
                        toString(actual),
                        info.hasDescription() ?  "(" + info.descriptionText() + ") " : "",
                        toString(expected),
                        expectedDescription
                    ));
            }

        }
        return myself;
    }

    @SuppressWarnings("UnusedReturnValue")
    public AlgebraicElementAssert<E> isNotEqTo(E other) {
        if (actual.eq(other)) {
            assertionError("%s %s≈ %s (%s)".formatted(toString(actual), actualDescription.isEmpty() ? "" : " (" + actualDescription + ") ", toString(other), expectedDescription));
        }
        return myself;
    }

    @SuppressWarnings("UnusedReturnValue")
    public AlgebraicElementAssert<E> isExact() {
        if (actual instanceof UncertainNumber<?> uncertain) {
            if (! uncertain.isExact()) {
                assertionError("%s is not exact. It has an error of %s".formatted(toString(actual), uncertain.getUncertainty()));
            }
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
    public AlgebraicElementAssert<E> withActualDescription(String s) {
         actualDescription = s;
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
