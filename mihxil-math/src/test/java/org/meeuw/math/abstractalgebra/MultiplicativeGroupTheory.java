package org.meeuw.math.abstractalgebra;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupTheory<E extends MultiplicativeGroupElement<E>> extends MultiplicativeSemiGroupTheory<E> {

    @Property
    default void division(
        @ForAll(ELEMENTS) E v1,
        @ForAll(ELEMENTS) E v2) {

        assertThat(v1.dividedBy(v2)).isEqualTo(v1.times(v2.reciprocal()));
    }
    @Property
    default void one(
        @ForAll(ELEMENTS) E v) {
        assertThat(v.times(v.structure().one())).isEqualTo(v);
    }


}
