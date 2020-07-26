package org.meeuw.math.abstractalgebra.integers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class IntegerElementTest {

    @Test
    void test() {
        assertThat(new IntegerElement(0).plus(new IntegerElement(1))).isEqualTo(new IntegerElement(1));
        assertThat(new IntegerElement(1).repeatedPlus(8)).isEqualTo(new IntegerElement(8));
        assertThat(new IntegerElement(1).minus(new IntegerElement(-5))).isEqualTo(new IntegerElement(6));


    }
}
