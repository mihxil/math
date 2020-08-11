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
    default void testEqualsSelf(@ForAll(ELEMENTS) E e) {
        assertThat(e.equals(e)).isTrue();
        assertThat(e.equals(null)).isFalse();
        assertThat(e.equals(new Object())).isFalse();
    }
      @Property
    default void testEquals(@ForAll(ELEMENTS) E e1, @ForAll(ELEMENT) E e2) {
        assertThat(e1.equals(e2)).isEqualTo(e2.equals(e1));
        if (e1.equals(e2)) {
            assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
        }

    }
}
