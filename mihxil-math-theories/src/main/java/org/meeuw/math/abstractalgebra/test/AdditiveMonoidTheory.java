package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.AdditiveMonoidElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveMonoidTheory<E extends AdditiveMonoidElement<E>>
    extends AdditiveSemiGroupTheory<E> {

    @Property
    default void zero(@ForAll(ELEMENTS) E e) {
        assertThat(e.plus(e.getStructure().zero())).isEqualTo(e);
    }

}
