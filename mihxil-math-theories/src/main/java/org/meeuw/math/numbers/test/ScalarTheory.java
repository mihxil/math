package org.meeuw.math.numbers.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.numbers.Scalar;
import org.meeuw.util.test.ElementTheory;

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

}
