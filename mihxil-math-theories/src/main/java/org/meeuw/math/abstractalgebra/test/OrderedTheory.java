package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.Ordered;
import org.meeuw.util.test.ElementTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface OrderedTheory<E extends Ordered<E>> extends ElementTheory<E> {


    @Property
    default void orderedReflexive(@ForAll(ELEMENTS) E e) {
        assertThat(e.lte(e)).isTrue();
    }

    @Property
    default void orderedTransitive(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b, @ForAll(ELEMENTS) E c) {
        if (a.lte(b)) {
            // a <= b
            if (b.lte(c)) {
                // a <= b, b <= c ->  a <= c
                assertThat(a.lte(c)).withFailMessage("%s <= %s and %s <= %s -> %s <= %s", a, b, b, c, a, c).isTrue();
            } else {
                getLogger().debug("a <= b,  c <=  b");
            }
        } else {
            // b < a
            if (a.lte(c)) {
                // b < a, a < c
                assertThat(b.lt(c)).withFailMessage("%s < %s and %s < %s -> %s < %s", b, a, a, c, b, c).isTrue();
            } else {
                // b < a, a > c
                getLogger().debug("b > a,  a < c");
            }
        }
    }

    @Property
    default void orderedAntisymmetric(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        if (a.lte(b) && b.lte(a)) {
            assertThat(a.equals(b)).isTrue();
        }
    }

    @Property
    default void orderedStronglyConnected(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        assertThat(a.lte(b) || b.lte(a)).withFailMessage("%s <= %s = %s %s <= %s = %s, on of these must be true", a, b, a.lte(b), b, a,b.lte(a)).isTrue();
    }
}
