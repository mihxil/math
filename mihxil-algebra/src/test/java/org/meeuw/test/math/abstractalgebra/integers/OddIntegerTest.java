package org.meeuw.test.math.abstractalgebra.integers;

import java.util.stream.Collectors;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.lifecycle.BeforeContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.integers.*;
import org.meeuw.math.abstractalgebra.test.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.test.ScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.OddInteger.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class OddIntegerTest implements
    MultiplicativeMonoidTheory<OddInteger>,
    MultiplicativeAbelianSemiGroupTheory<OddInteger>,
    SignedNumberTheory<OddInteger>,
    ScalarTheory<OddInteger> {

    @BeforeAll
    @BeforeContainer
    public static void setup() {
        PositiveIntegerTest.setup();
    }

    @Test
    public void test() {
        assertThatThrownBy(() -> of(0)).isInstanceOf(InvalidElementCreationException.class);

        assertThat(of(3).times(of(5))).isEqualTo(of(15));
        assertThat(of(3).plus(EvenInteger.of(4))).isEqualTo(of(7));
        assertThat(of(3).plus(EvenInteger.of(-8))).isEqualTo(of(-5));

        assertThat(of(3).plus(EvenInteger.of(-4))).isEqualTo(of(-1));

        assertThat(of(3).times(OddInteger.ONE)).isEqualTo(of(3));
    }


    @Override
    public Arbitrary<OddInteger> elements() {
        return Arbitraries.randomValue((random) -> OddInteger.of(2 * (random.nextLong() / 2) + 1));
    }

    @Test
    void stream() {
        Assertions.assertThat(OddIntegers.INSTANCE.stream().limit(10).map(OddInteger::longValue)
            .collect(Collectors.toList()))
            .containsExactly(1L, -1L, 3L, -3L, 5L, -5L, 7L, -7L, 9L, -9L);
    }

}
