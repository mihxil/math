package org.meeuw.assertj;

import java.util.Optional;

import org.assertj.core.api.AbstractObjectAssert;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * As {@link AlgebraicElementAssert} but for an {@link Optional}. If the {@code Optional} is empty, the assertion will fail. Otherwise, the assertion will be delegated to the wrapped {@link AlgebraicElementAssert}.
 * @see AlgebraicElementAssert
 * @param <E>
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OptionalAlgebraicElementAssert<E extends AlgebraicElement<E>> extends AbstractObjectAssert<OptionalAlgebraicElementAssert<E>, Optional<E>> {

    protected final AlgebraicElementAssert<E> wrapped;


    protected OptionalAlgebraicElementAssert(Optional<E> o) {
        super(o, OptionalAlgebraicElementAssert.class);
        wrapped = new AlgebraicElementAssert<>(o.orElse(null));
    }


    /**
     * Like {@link AlgebraicElementAssert#isEqTo(AlgebraicElement)}} but for an {@link Optional}.
     * @param expected The value expected to be contained in the {@code Optional}
     */
    @SuppressWarnings("UnusedReturnValue")
    public OptionalAlgebraicElementAssert<E> containsEq(E expected) {
        if (actual.isEmpty()) {
            assertionError("Expected to contain %s, but was empty".formatted(expected));
        } else {
            wrapped.isEqTo(expected);
        }
        return myself;
    }


    @SuppressWarnings("UnusedReturnValue")
    public OptionalAlgebraicElementAssert<E> containsNotEq(E expected) {
        if (actual.isEmpty()) {
            assertionError("Expected to contain not  %s, but was empty".formatted(expected));
        } else {
            wrapped.isNotEqTo(expected);
        }
        return myself;
    }

    @SuppressWarnings("UnusedReturnValue")
    public OptionalAlgebraicElementAssert<E> containsExact() {
        if (actual.isEmpty()) {
            assertionError("Expected to be exact but was empty");
        } else {
            wrapped.isExact();
        }
        return myself;
    }

    protected String toString(E element) {
        return wrapped.toString(element);
    }

    /**
     * @since 0.19
     */
    public OptionalAlgebraicElementAssert<E> withIncludeClassNames(boolean b) {
        wrapped.withIncludeClassNames(b);
        return myself;
    }

    /**
     * Provides a string describing the expected value
     * @since 0.19
     */
    public OptionalAlgebraicElementAssert<E> withExpectedDescription(String s) {
        wrapped.withExpectedDescription(s);
        return myself;
    }

    /**
     * Provides a string describing the actual value (e.g. for describing how it was obtained)
     * @since 0.19
     */
    public OptionalAlgebraicElementAssert<E> withActualDescription(String s) {
        wrapped.withActualDescription(s);
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
