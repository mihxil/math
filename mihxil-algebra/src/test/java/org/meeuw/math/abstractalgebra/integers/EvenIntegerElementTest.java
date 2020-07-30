package org.meeuw.math.abstractalgebra.integers;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.EvenIntegerElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class EvenIntegerElementTest {

    @Test
    public void test() {
        assertThatThrownBy(() -> of(1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(of(2).times(of(4))).isEqualTo(of(8));
        assertThat(of(2).plus(of(4))).isEqualTo(of(6));
        assertThat(of(2).plus(of(4).negation())).isEqualTo(of(-2));

        assertThat(of(2).plus(of(2).structure().zero())).isEqualTo(of(2));
    }

    @Test
    void stream() {
        assertThat(EvenIntegers.INSTANCE.stream().limit(11).map(EvenIntegerElement::longValue)
            .collect(Collectors.toList())).containsExactly(0L, 2L, -2L, 4L, -4L, 6L, -6L, 8L, -8L, 10L, -10L);
    }

}
