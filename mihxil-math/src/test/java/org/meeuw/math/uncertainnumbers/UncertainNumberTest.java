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


}
