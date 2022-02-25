package org.meeuw.test.math.abstractalgebra.integers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.PositiveInteger;
import org.meeuw.math.abstractalgebra.integers.PositiveIntegers;
import org.meeuw.math.abstractalgebra.test.AdditiveAbelianSemiGroupTheory;
import org.meeuw.math.abstractalgebra.test.MultiplicativeMonoidTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.test.ScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.PositiveInteger.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
class PositiveIntegerTest implements
    MultiplicativeMonoidTheory<PositiveInteger>,
    AdditiveAbelianSemiGroupTheory<PositiveInteger>,
    ScalarTheory<PositiveInteger> {

    @Test
    public void test() {
        assertThatThrownBy(() -> of(1).times(of(-1))).isInstanceOf(InvalidElementCreationException.class);
        assertThat(of(5).plus(of(7))).isEqualTo(of(12));
    }

    @Override
    public Arbitrary<PositiveInteger> elements() {
        return Arbitraries.randomValue(
            PositiveIntegers.INSTANCE::nextRandom
            ).injectDuplicates(10);
    }
}
