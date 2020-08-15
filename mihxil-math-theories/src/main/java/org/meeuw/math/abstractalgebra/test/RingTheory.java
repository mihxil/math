package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.RingElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface RingTheory<E extends RingElement<E>> extends AdditiveGroupTheory<E>, RngTheory<E> {

    @Property
    default void one(
        @ForAll(ELEMENTS) E v) {
        assertThat(v.times(v.getStructure().one())).isEqualTo(v);
    }
}
