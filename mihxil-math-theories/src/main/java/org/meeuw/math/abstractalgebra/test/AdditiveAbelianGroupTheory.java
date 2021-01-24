package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.AdditiveGroupElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveAbelianGroupTheory <E extends AdditiveGroupElement<E>>
    extends AdditiveGroupTheory<E> {


    @Property
    default void minusIsAnticommutative(
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2) {
        assertThat(v1.minus(v2)).isEqualTo(v2.minus(v1).negation());
    }

    @Property
    default void additiveCommutativity (
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2) {
        assertThat(v1.plus(v2)).isEqualTo(v2.plus(v1));
    }
}
