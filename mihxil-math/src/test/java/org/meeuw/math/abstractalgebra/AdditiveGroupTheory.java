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
public interface AdditiveGroupTheory<F extends AdditiveGroupElement<F>> {

    @Property
    default void minus(
            @ForAll("elements") F v1,
            @ForAll("elements") F v2) {
		assertThat(v1.minus(v2)).isEqualTo(v1.plus(v2.negation()));
    }

    @Property
    default void additiveCommutativity (
            @ForAll("elements") F v1,
            @ForAll("elements") F v2) {
        assertThat(v1.plus(v2)).isEqualTo(v2.plus(v1));
    }
    @Property
    default void zero(@ForAll("elements") F v) {
        assertThat(v.plus(v.structure().zero())).isEqualTo(v);
    }

    @Provide
    Arbitrary<F> elements();
}
