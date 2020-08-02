package org.meeuw.math.abstractalgebra;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupTheory<F extends MultiplicativeGroupElement<F>> extends MultiplicativeSemiGroupTheory<F> {

    @Property
    default void division(
        @ForAll("elements") F v1,
        @ForAll("elements") F v2) {

        assertThat(v1.dividedBy(v2)).isEqualTo(v1.times(v2.reciprocal()));
    }
    @Property
    default void one(
        @ForAll("elements") F v) {
        assertThat(v.times(v.structure().one())).isEqualTo(v);
    }

    @Provide
    Arbitrary<F> elements();
}
