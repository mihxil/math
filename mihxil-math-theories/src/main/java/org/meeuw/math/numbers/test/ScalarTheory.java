package org.meeuw.math.numbers.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.numbers.Scalar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ScalarTheory<S extends Scalar<S>> {

    @Property
    default void integerValues(@ForAll("elements") S scalar) {
        assertThat(scalar.intValue()).isEqualTo((int) scalar.longValue());
        assertThat(scalar.byteValue()).isEqualTo((byte) scalar.intValue());
        assertThat(scalar.shortValue()).isEqualTo((short) scalar.intValue());

    }


    @Property
    default void floatValues(@ForAll("elements") S scalar) {
        assertThat(scalar.floatValue()).isCloseTo((float) scalar.doubleValue(), withPercentage(0.1d));

        assertThat(scalar.bigDecimalValue().doubleValue()).isEqualTo(scalar.doubleValue());
    }

    @Property
    default void isFinite(@ForAll("elements") S scalar) {
        if (scalar.isFinite()) {
            assertThat(Double.isFinite(scalar.doubleValue())).isTrue();
        } else {
            assertThat(Double.isFinite(scalar.doubleValue())).isFalse();
        }
    }
    @Property
    default void isNaN(@ForAll("elements") S scalar) {
        if (scalar.isNaN()) {
            assertThat(Double.isNaN(scalar.doubleValue())).isTrue();
        } else {
            assertThat(Double.isNaN(scalar.doubleValue())).isFalse();
        }
    }

}
