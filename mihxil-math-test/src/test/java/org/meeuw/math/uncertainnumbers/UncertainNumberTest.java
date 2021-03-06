package org.meeuw.math.uncertainnumbers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.meeuw.util.test.ElementTheory;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class UncertainNumberTest implements ElementTheory<UncertainNumberTest.A> {

    @Override
    public Arbitrary<? extends A> elements() {
        return Arbitraries.randomValue((random) -> new A(BigDecimal.valueOf(random.nextDouble() * 1000 - 500),
            BigDecimal.valueOf(random.nextDouble() * 10)))
            .injectDuplicates(0.1);
    }

    public static class A extends ImmutableUncertainNumber<BigDecimal> {
        public A(BigDecimal value, BigDecimal uncertainty) {
            super(value, uncertainty);
        }
    }

    public static class UND extends ImmutableUncertainNumber<Double> {

        public UND(Double value, Double uncertainty) {
            super(value, uncertainty);
        }
    }


    @Test
    void times() {
        A a = new A(valueOf(1), valueOf(0.1));
        UncertainNumber<BigDecimal> product = a.times(valueOf(2));
        assertThat(product.getValue()).isEqualTo(valueOf(2));
        assertThat(product.getUncertainty()).isEqualTo(valueOf(0.2));
    }

    @Test
    void dividedBy() {
        A a = new A(valueOf(1), valueOf(0.1));
        UncertainNumber<BigDecimal> quotient = a.dividedBy(valueOf(2));
        assertThat(quotient.getValue()).isEqualTo(valueOf(0.5));
        assertThat(quotient.getUncertainty()).isEqualTo(valueOf(0.05));
    }


    @Test
    void plus() {
        A a = new A(valueOf(1), valueOf(0.1));
        UncertainNumber<BigDecimal> sum = a.plus(valueOf(2));
        assertThat(sum.getValue()).isEqualTo(valueOf(3));
        assertThat(sum.getUncertainty()).isEqualTo(valueOf(0.1));
    }


    @Test
    void minus() {
        A a = new A(valueOf(1), valueOf(0.1));
        UncertainNumber<BigDecimal> difference = a.minus(valueOf(2));
        assertThat(difference.getValue()).isEqualTo(valueOf(-1));
        assertThat(difference.getUncertainty()).isEqualTo(valueOf(0.1));
    }

    @Test
    void pow() {
        A a = new A(valueOf(2), valueOf(0.1));
        UncertainNumber<BigDecimal> pow = a.pow(3);
        assertThat(pow.getValue()).isEqualTo(valueOf(8));
        assertThat(pow.getUncertainty()).isEqualTo(valueOf(1.2));
    }

    @Test
    void combined() {
        A a1 = new A(valueOf(1), valueOf(0.1));
        A a2 = new A(valueOf(1.1), valueOf(0.05));
        UncertainNumber<BigDecimal> combined = a1.combined(a2);
        assertThat(combined.bigDecimalValue()).isEqualTo(valueOf(1.08));
        assertThat(combined.getUncertainty()).isEqualTo(valueOf(0.044721359549995794));
    }

    @Test
    void equals() {
        A a = new A(valueOf(1), valueOf(0.1));
        A b = new A(valueOf(1.05), valueOf(0.0001));
        assertThat(a.equals(b, 1)).isTrue();
        assertThat(b.equals(a, 1)).isTrue();
        assertThat(a.equals(a, 1)).isTrue();

        UND d1 = new UND(1d, 0.1);
        UND d2 = new UND(Double.NaN, 0.1);
        assertThat(d1.equals(d2, 1)).isFalse();
        assertThat(d2.equals(d1, 1)).isFalse();

        assertThat(d2.equals(new UND(Double.NaN, 2d), 1)).isTrue();
    }

}
