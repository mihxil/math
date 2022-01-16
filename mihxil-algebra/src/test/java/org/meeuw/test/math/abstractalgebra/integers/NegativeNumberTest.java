package org.meeuw.test.math.abstractalgebra.integers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.NegativeInteger;
import org.meeuw.math.abstractalgebra.integers.PositiveInteger;
import org.meeuw.math.abstractalgebra.test.AdditiveAbelianSemiGroupTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.test.SizeableScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.NegativeInteger.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
class NegativeNumberTest implements
    AdditiveAbelianSemiGroupTheory<NegativeInteger>,
    SizeableScalarTheory<NegativeInteger, PositiveInteger> {

    @Test
    public void test() {
        assertThatThrownBy(() -> of(1)).isInstanceOf(InvalidElementCreationException.class);
        assertThat(of(-5).plus(of(-7))).isEqualTo(of(-12));
    }

    @Override
    public Arbitrary<NegativeInteger> elements() {
        return Arbitraries.randomValue(r ->
            of(-1 * Math.abs(r.nextInt(100_000) + 1))
        ).injectDuplicates(10);
    }
}
