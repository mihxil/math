package org.meeuw.math.abstractalgebra.integers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.integers.IntegerElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class IntegerElementTest {

    @Test
    void test() {
        assertThat(of(0).plus(of(1))).isEqualTo(of(1));
        assertThat(of(1).repeatedPlus(8)).isEqualTo(of(8));
        assertThat(of(1).minus(of(-5))).isEqualTo(of(6));


    }
}
