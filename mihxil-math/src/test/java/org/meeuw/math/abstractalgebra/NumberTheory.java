package org.meeuw.math.abstractalgebra;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.assertj.core.data.Offset;

import static java.lang.Math.signum;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface NumberTheory<E extends NumberElement<E>> extends ElementTheory<E> {

    @Property
    default void compareToConsistentWithEquals(@ForAll(ELEMENTS) E e1, @ForAll(ELEMENTS) E e2) {
        int ct = e1.compareTo(e2);
        if (ct == 0) {
            assertThat(e1).isEqualTo(e2);
            assertThat(e2).isEqualTo(e1);
            assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
        } else {
            assertThat(e1).isNotEqualTo(e2);
            assertThat(e2).isNotEqualTo(e1);
        }
        assertThat(signum(ct)).isEqualTo(-1 * signum(e2.compareTo(e1)));
    }

    @Property
    default void implementsNumber(@ForAll(ELEMENTS) E e1) {
        assertThat(e1.doubleValue()).isCloseTo(e1.floatValue(), Offset.offset(Math.abs(e1.doubleValue() / 1e7)));
        if (e1.longValue() < Integer.MAX_VALUE && e1.longValue() > Integer.MIN_VALUE) {
            assertThat(e1.longValue()).isEqualTo(e1.intValue());
        }
        assertThat(e1.compareTo(e1.longValue())).isEqualTo(0);
        assertThat(e1.compareTo(e1.longValue() + 1)).isLessThan(0);
        assertThat(e1.compareTo(e1.longValue() - 1)).isGreaterThan(0);
    }


}
