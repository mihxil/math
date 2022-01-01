package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.Ordered;
import org.meeuw.math.abstractalgebra.StrictlyOrdered;
import org.meeuw.math.uncertainnumbers.Uncertain;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface OrderedTheory<E extends StrictlyOrdered<E>> extends StrictlyOrderedTheory<E> {




    @Property
    default void orderedTransitive(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b, @ForAll(ELEMENTS) E c) {

        if (a instanceof Ordered) {
            assertThat(a).isNotInstanceOf(Uncertain.class);
            orderedTransitiveImpl(a, b, c);
        } else {
            assertThat(a).isInstanceOf(Uncertain.class);
            try {
                orderedTransitiveImpl(a, b, c);
            } catch (AssertionFailedError afe) {
                getLogger().info(afe.getMessage());
            }
        }
    }

    default void orderedTransitiveImpl(E a, E b, E c) {




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


}
