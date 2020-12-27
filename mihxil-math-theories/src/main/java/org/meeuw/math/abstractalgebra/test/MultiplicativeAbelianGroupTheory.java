package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeAbelianGroupTheory<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeGroupTheory<E> {

    @Property
    default void multiplicativeCommutativity (
        @ForAll(ELEMENTS) E v1,
        @ForAll(ELEMENTS) E v2) {
        assertThat(v1.times(v2)).isEqualTo(v2.times(v1));
    }

}
