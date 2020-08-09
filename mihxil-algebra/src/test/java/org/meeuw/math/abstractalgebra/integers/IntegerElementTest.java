package org.meeuw.math.abstractalgebra.integers;

import net.jqwik.api.*;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.NumberTheory;
import org.meeuw.math.abstractalgebra.RingTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.integers.IntegerElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class IntegerElementTest implements RingTheory<IntegerElement>, NumberTheory<IntegerElement> {

    @Test
    void test() {
        assertThat(of(0).plus(of(1))).isEqualTo(of(1));
        assertThat(of(1).repeatedPlus(8)).isEqualTo(of(8));
        assertThat(of(1).minus(of(5).negation())).isEqualTo(of(6));

        assertThat(of(2).times(of(-5))).isEqualTo(of(-10));
        IntegerElement two = of(2);
        assertThat(two.times(two.getStructure().one())).isEqualTo(two);

        assertThat(two.plus(two.getStructure().zero())).isEqualTo(two);
    }

    @Property
    void strings(@ForAll(ELEMENTS) IntegerElement integerElement) {
        assertThat(integerElement.toString()).isEqualTo(Long.toString(integerElement.longValue()));
    }
    @Property
    void doubles(@ForAll(ELEMENTS) IntegerElement integerElement) {
        assertThat(integerElement.doubleValue()).isEqualTo(Double.valueOf(integerElement.longValue()));
    }

    @Test
    void stream() {
        assertThat(Integers.INSTANCE.stream().limit(11).map(IntegerElement::longValue)
            .collect(Collectors.toList())).containsExactly(0L, 1L, -1L, 2L, -2L, 3L, -3L, 4L, -4L, 5L, -5L);
    }

    @Provide
    public Arbitrary<IntegerElement> elements() {
        return Arbitraries.randomValue((random) -> IntegerElement.of(random.nextLong()));
    }
}
