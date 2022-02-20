package org.meeuw.test.math.abstractalgebra.integers;

import java.util.stream.Collectors;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.NDivisibleInteger;
import org.meeuw.math.abstractalgebra.integers.NDivisibleIntegers;
import org.meeuw.math.abstractalgebra.test.RngTheory;
import org.meeuw.math.abstractalgebra.test.SignedNumberTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.test.SizeableScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.NDivisibleInteger.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
class NDivisibleIntegerTest implements
    RngTheory<NDivisibleInteger>,
    SizeableScalarTheory<NDivisibleInteger, NDivisibleInteger>,
    SignedNumberTheory<NDivisibleInteger> {

    @Test
    public void invalid() {
        assertThatThrownBy(() -> of(3, 1)).isInstanceOf(InvalidElementCreationException.class);
    }

    @Test
    public void test() {
        NDivisibleInteger six =  of(3, 6);
        NDivisibleIntegers structure = six.getStructure();
        assertThat(structure.getDivisor()).isEqualTo(3);
        assertThat(structure.getElementClass()).isEqualTo(NDivisibleInteger.class);

    }

    @Override
    public Arbitrary<NDivisibleInteger> elements() {
        return Arbitraries.randomValue((random) -> NDivisibleInteger.of(3, 3 * (random.nextLong() / 3)));
    }

    @Test
    void stream() {
        assertThat(NDivisibleIntegers.of(3).stream().limit(11).map(NDivisibleInteger::longValue)
            .collect(Collectors.toList())).containsExactly(0L, 3L, -3L, 6L, -6L, 9L, -9L, 12L, -12L, 15L, -15L);
    }

}
