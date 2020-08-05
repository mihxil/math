package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ElementTheory<E>  {

    String ELEMENT = "element";
    String ELEMENTS = "elements";

    @Provide
    Arbitrary<E> elements();

    @Provide
    default Arbitrary<E> element() {
        return Arbitraries.of(elements().sample());
    }

    @Property
    default void testEquals(@ForAll(ELEMENTS) E e) {
        assertThat(e).isEqualTo(e);
        assertThat(e).isNotEqualTo(new Object());
    }
}
