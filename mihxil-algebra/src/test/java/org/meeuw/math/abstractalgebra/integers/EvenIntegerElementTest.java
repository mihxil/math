package org.meeuw.math.abstractalgebra.integers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.RngTheory;
import org.meeuw.math.abstractalgebra.test.SignedNumberTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.EvenIntegerElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class EvenIntegerElementTest implements RngTheory<EvenIntegerElement>, SignedNumberTheory<EvenIntegerElement> {

    @Test
    public void test() {
        assertThatThrownBy(() -> of(1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(of(2).times(of(4))).isEqualTo(of(8));
        assertThat(of(2).plus(of(4))).isEqualTo(of(6));
        assertThat(of(2).plus(of(4).negation())).isEqualTo(of(-2));

        assertThat(of(2).plus(of(2).getStructure().zero())).isEqualTo(of(2));
    }

    @Test
    public void compareTo() {
        //assertThat(new EvenIntegerElement(-2019178024101599495L).compareTo(
        //new BigDecimal(-2019178024101599496L))).isGreaterThan(0);
    }

    @Override
    public Arbitrary<EvenIntegerElement> elements() {
        return Arbitraries.randomValue((random) -> EvenIntegerElement.of(2 * (random.nextLong() / 2)));
    }

    @Test
    void stream() {
        assertThat(EvenIntegers.INSTANCE.stream().limit(11).map(EvenIntegerElement::longValue)
            .collect(Collectors.toList())).containsExactly(0L, 2L, -2L, 4L, -4L, 6L, -6L, 8L, -8L, 10L, -10L);
    }

}
