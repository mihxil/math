package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.AbelianRingElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface AbelianRingTheory<E extends AbelianRingElement<E>> extends RingTheory<E> {


    @Property
    default void ringCommutative(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        assertThat(a.times(b)).isEqualTo(b.times(a));
    }
}
