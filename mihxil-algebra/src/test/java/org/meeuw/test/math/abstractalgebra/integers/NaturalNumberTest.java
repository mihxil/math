package org.meeuw.test.math.abstractalgebra.integers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.NaturalNumber;
import org.meeuw.math.abstractalgebra.test.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.test.ScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.NaturalNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class NaturalNumberTest implements
    MultiplicativeMonoidTheory<NaturalNumber>,
    AdditiveMonoidTheory<NaturalNumber>,
    AdditiveAbelianSemiGroupTheory<NaturalNumber>,
    ScalarTheory<NaturalNumber> {

    @Test
    public void test() {
        assertThatThrownBy(() -> of(1).times(of(-1))).isInstanceOf(InvalidElementCreationException.class);
        assertThat(of(5).plus(of(7))).isEqualTo(of(12));
    }

    @Override
    public Arbitrary<NaturalNumber> elements() {
        return Arbitraries.randomValue(r ->
            new NaturalNumber(Math.abs(r.nextInt(100_000)))).injectDuplicates(10);
    }
}
