package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import java.math.BigDecimal;

import org.assertj.core.data.Offset;
import org.meeuw.math.numbers.NumberElement;

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
        assertThat(e1).isInstanceOf(Number.class);
        assertThat(e1.doubleValue()).isCloseTo(e1.floatValue(), Offset.offset(Math.abs(e1.doubleValue() / 1e7)));
        if (e1.longValue() < Integer.MAX_VALUE && e1.longValue() > Integer.MIN_VALUE) {
            assertThat(e1.longValue()).isEqualTo(e1.intValue());
        }

        if (e1.longValue() < Byte.MAX_VALUE && e1.longValue() > Byte.MIN_VALUE) {
            assertThat(e1.longValue()).isEqualTo(e1.byteValue());
        }

        if (e1.longValue() < Short.MAX_VALUE && e1.longValue() > Short.MIN_VALUE) {
            assertThat(e1.longValue()).isEqualTo(e1.shortValue());
        }
        assertThat(e1.compareTo(e1.bigDecimalValue())).withFailMessage("Not equal to its bigDecimal value %s != %s", e1, e1.bigDecimalValue()).isEqualTo(0);
        BigDecimal offset = BigDecimal.ONE;
        getLogger().debug("Offset for {} {}", e1.bigDecimalValue(), offset);
        BigDecimal plus  = e1.bigDecimalValue().add(offset);
        BigDecimal minus  = e1.bigDecimalValue().add(offset.negate());
        assertThat(e1.compareTo(plus)).withFailMessage("%s %s", e1, plus).isLessThan(0);
        assertThat(e1.compareTo(minus)).withFailMessage("%s %s", e1, minus).isGreaterThan(0);

    }


}
