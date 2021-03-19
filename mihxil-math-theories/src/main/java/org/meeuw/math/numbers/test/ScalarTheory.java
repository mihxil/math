package org.meeuw.math.numbers.test;

import java.math.BigDecimal;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.data.Offset;

import org.meeuw.math.numbers.Scalar;
import org.meeuw.util.test.ElementTheory;

import static java.lang.Math.signum;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ScalarTheory<S extends Scalar<S>> extends ElementTheory<S> {

    @Property
    default void abs(@ForAll(ELEMENTS) S scalar) {
        S abs = scalar.abs();
        getLogger().debug("abs({}) = {}", scalar, abs);
        assertThat(abs.isNegative()).withFailMessage(() -> "abs(" + scalar  + ") = " + abs + " is negative").isFalse();
        assertThat(abs.doubleValue()).isEqualTo(Math.abs(scalar.doubleValue()));
    }

    @Property
    default void compareTo(@ForAll(ELEMENTS) S scalar1, @ForAll(ELEMENTS) S scalar2) {
        if (scalar1.compareTo(scalar2) > 0) {
            assertThat(scalar1.doubleValue() - scalar2.doubleValue()).isGreaterThan(0);
        }
        if (scalar1.compareTo(scalar2) < 0) {
            assertThat(scalar1.doubleValue() - scalar2.doubleValue()).isLessThan(0);
        }
    }

    @Property
    default void ltgt(@ForAll(ELEMENTS) S scalar1, @ForAll(ELEMENTS) S scalar2) {
        if (scalar1.lt(scalar2)) {
            assertThat(scalar2.lt(scalar1)).isFalse();
            assertThat(scalar2.gt(scalar1)).isTrue();
            assertThat(scalar1.gt(scalar2)).isFalse();
        } else {
            assertThat(scalar2.lte(scalar1)).isTrue();
            assertThat(scalar1.gte(scalar2)).isTrue();
        }
    }
    @Property
    default void ltegte(@ForAll(ELEMENTS) S scalar1, @ForAll(ELEMENTS) S scalar2) {
        if (scalar1.lte(scalar2)) {
            assertThat(scalar1.lt(scalar2) || scalar2.equals(scalar1)).isTrue();
        } else {
            assertThat(scalar1.gt(scalar2)).isTrue();
        }
    }


    @Property
    default void integerValues(@ForAll(ELEMENTS) S scalar) {
        assertThat(scalar.intValue()).isEqualTo((int) scalar.longValue());
        assertThat(scalar.byteValue()).isEqualTo((byte) scalar.intValue());
        assertThat(scalar.shortValue()).isEqualTo((short) scalar.intValue());

    }


    @Property
    default void floatValues(@ForAll(ELEMENTS) S scalar) {
        assertThat(scalar.floatValue()).isCloseTo((float) scalar.doubleValue(), withPercentage(0.1d));
        assertThat(scalar.bigDecimalValue().doubleValue()).isEqualTo(scalar.doubleValue());
    }

    @Property
    default void isFinite(@ForAll(ELEMENTS) S scalar) {
        if (scalar.isFinite()) {
            assertThat(Double.isFinite(scalar.doubleValue())).isTrue();
        } else {
            assertThat(Double.isFinite(scalar.doubleValue())).isFalse();
        }
    }
    @Property
    default void isNaN(@ForAll(ELEMENTS) S scalar) {
        if (scalar.isNaN()) {
            assertThat(Double.isNaN(scalar.doubleValue())).isTrue();
        } else {
            assertThat(Double.isNaN(scalar.doubleValue())).isFalse();
        }
    }

    @Property
    default void compareToConsistentWithEquals(@ForAll(ELEMENTS) S e1, @ForAll(ELEMENTS) S e2) {
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
    default void implementsScalar(@ForAll(ELEMENTS) S e1) {
        assertThat(e1).isInstanceOf(Scalar.class);
        assertThat(e1.doubleValue()).isCloseTo(e1.floatValue(), Offset.offset(Math.abs(e1.doubleValue() / 5e6)));
        if (e1.longValue() < Integer.MAX_VALUE && e1.longValue() > Integer.MIN_VALUE) {
            assertThat(e1.longValue()).isEqualTo(e1.intValue());
        }

        if (e1.longValue() < Byte.MAX_VALUE && e1.longValue() > Byte.MIN_VALUE) {
            assertThat(e1.longValue()).isEqualTo(e1.byteValue());
        }

        if (e1.longValue() < Short.MAX_VALUE && e1.longValue() > Short.MIN_VALUE) {
            assertThat(e1.longValue()).isEqualTo(e1.shortValue());
        }
        //assertThat(e1.compareTo(e1.bigDecimalValue())).withFailMessage("Not equal to its bigDecimal value %s != %s", e1, e1.bigDecimalValue()).isEqualTo(0);
        BigDecimal offset = BigDecimal.ONE;
        getLogger().debug("Offset for {} {}", e1.bigDecimalValue(), offset);
        BigDecimal plus  = e1.bigDecimalValue().add(offset);
        BigDecimal minus  = e1.bigDecimalValue().add(offset.negate());
        //assertThat(e1.compareTo(plus)).withFailMessage("%s %s", e1, plus).isLessThan(0);
        //assertThat(e1.compareTo(minus)).withFailMessage("%s %s", e1, minus).isGreaterThan(0);
    }



}
