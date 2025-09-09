package org.meeuw.assertj;

import java.util.Optional;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * Drop in replacement (and extension of {@link org.assertj.core.api.Assertions}). Adds support for {@link #assertThat(AlgebraicElement)}
 */
public class Assertions extends org.assertj.core.api.Assertions {

    /**
     * Same as {@link #assertThatAlgebraically(AlgebraicElement)} but with a more generic name.
     */
    public static <E extends AlgebraicElement<E>> AlgebraicElementAssert<E> assertThat(E actual) {
        return assertThatAlgebraically(actual);
    }

    /**
     * Many {@link AlgebraicElement} are also {@link Comparable}, so would also match {@link org.assertj.core.api.Assertions#assertThat(Comparable)}.
     *
     * @since 0.15
     */
    public static <E extends AlgebraicElement<E>> AlgebraicElementAssert<E> assertThatAlgebraically(E actual) {
        return new AlgebraicElementAssert<>(actual);
    }


    /**
     * Many {@link AlgebraicElement} are also {@link Comparable}, so would also match {@link org.assertj.core.api.Assertions#assertThat(Comparable)}.
     *
     * @since 0.19
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <E extends AlgebraicElement<E>> OptionalAlgebraicElementAssert<E> assertThatAlgebraically(Optional<E> actual) {
        return new OptionalAlgebraicElementAssert<>(actual);
    }

}
