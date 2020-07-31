package org.meeuw.math.abstractalgebra.integers;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

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
        assertThat(of(1).minus(of(5).negation())).isEqualTo(of(6));

        assertThat(of(2).times(of(-5))).isEqualTo(of(-10));
        IntegerElement two = of(2);
        assertThat(two.times(two.structure().one())).isEqualTo(two);

        assertThat(two.plus(two.structure().zero())).isEqualTo(two);

    }

    @Test
    void stream() {
        assertThat(Integers.INSTANCE.stream().limit(11).map(IntegerElement::longValue)
            .collect(Collectors.toList())).containsExactly(0L, 1L, -1L, 2L, -2L, 3L, -3L, 4L, -4L, 5L, -5L);
    }
}