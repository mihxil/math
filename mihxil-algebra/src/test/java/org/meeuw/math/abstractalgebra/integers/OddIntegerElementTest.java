package org.meeuw.math.abstractalgebra.integers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.*;
import org.meeuw.math.numbers.test.ScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.OddIntegerElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class OddIntegerElementTest implements MultiplicativeMonoidTheory<OddIntegerElement>, SignedNumberTheory<OddIntegerElement>, ScalarTheory<OddIntegerElement> {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void test() {
        assertThatThrownBy(() -> of(0)).isInstanceOf(IllegalArgumentException.class);

        assertThat(of(3).times(of(5))).isEqualTo(of(15));
        assertThat(of(3).plus(4)).isEqualTo(of(7));
        assertThat(of(3).plus(-4)).isEqualTo(of(-1));

        assertThatThrownBy(() -> of(1).plus(1)).isInstanceOf(ArithmeticException.class);


        assertThat(of(3).times(OddIntegerElement.ONE)).isEqualTo(of(3));
    }


    @Override
    public Arbitrary<OddIntegerElement> elements() {
        return Arbitraries.randomValue((random) -> OddIntegerElement.of(2 * (random.nextLong() / 2) + 1));
    }

    @Test
    void stream() {
        assertThat(OddIntegers.INSTANCE.stream().limit(10).map(OddIntegerElement::longValue)
            .collect(Collectors.toList()))
            .containsExactly(1L, -1L, 3L, -3L, 5L, -5L, 7L, -7L, 9L, -9L);
    }

}
