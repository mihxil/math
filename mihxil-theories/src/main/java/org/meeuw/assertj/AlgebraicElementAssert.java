package org.meeuw.assertj;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.AbstractObjectAssert;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.uncertainnumbers.Uncertain;
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
            if (actual instanceof Uncertain uncertain) {
                assertionError(
                    "\n%s %s\n≉\n%s (%s)".formatted(
                        toStringWithUncertainty(uncertain),
                        info.hasDescription() ?  "(" + info.descriptionText() + ") " : "",
                            toStringWithUncertainty((Uncertain) expected),
                        expectedDescription
                    ));
            } else {
                assertionError(
                    "\n%s %s\n≉\n%s (%s)".formatted(
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

    @SafeVarargs
    @SuppressWarnings("UnusedReturnValue")
    public final AlgebraicElementAssert<E> isEqIn(E... expecteds) {
        for(E expected : expecteds) {
             if (actual.eq(expected)) {
                 return myself;
             }
        }
         assertionError("%s %s ≉ any of %s (%s)".formatted(toString(actual), info.hasDescription() ?  "(" + info.descriptionText() + ") " : "", toString(expecteds), expectedDescription));
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

    protected String toStringWithUncertainty(Uncertain element) {
        return (includeClassNames ? element.getClass().getSimpleName() + " " : "" ) + element.toStringWithUncertainty();
    }

    @SafeVarargs
    protected final String toString(E... element) {
        return Stream.of(element).map(this::toString).collect(Collectors.joining(","));
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
