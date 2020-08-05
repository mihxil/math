package org.meeuw.math.abstractalgebra;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface NumberTheory<E extends NumberElement<E>> extends ElementTheory<E> {

    @Property
    default void compareTo(@ForAll(ELEMENTS) E e1, @ForAll(ELEMENTS) E e2) {
        int ct = e1.compareTo(e2);
        if (ct == 0) {
            assertThat(e1).isEqualTo(e2);
        } else {
            assertThat(ct).isEqualTo(-1 * e2.compareTo(e1));
        }
    }
}
