package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupTheory<F extends MultiplicativeSemiGroupElement<F>> {

    @Property
    default void multiplicativeCommutativity (
        @ForAll("elements") F v1,
        @ForAll("elements") F v2) {
        assertThat(v1.times(v2)).isEqualTo(v2.times(v1));
    }

    @Provide
    Arbitrary<F> elements();
}
