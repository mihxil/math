package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class UncertainNumberTest {

    public static class A implements UncertainNumber<BigDecimal> {
        @Getter
        final BigDecimal value;
        @Getter
        final BigDecimal uncertainty;

        public A(BigDecimal value, BigDecimal uncertainty) {
            this.value = value;
            this.uncertainty = uncertainty;
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
    void combined() {
        A a1 = new A(valueOf(1), valueOf(0.1));
        A a2 = new A(valueOf(1.1), valueOf(0.05));
        UncertainNumber<BigDecimal> combined = a1.combined(a2);
        assertThat(combined.getValue()).isEqualTo(valueOf(1.08));
        assertThat(combined.getUncertainty()).isEqualTo(valueOf(0.044721359549995794));
    }


}
