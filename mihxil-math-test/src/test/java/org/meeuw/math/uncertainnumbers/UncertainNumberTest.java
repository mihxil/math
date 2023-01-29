/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.uncertainnumbers;

import java.math.BigDecimal;
import java.math.MathContext;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;
import org.assertj.core.data.Offset;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.numbers.MathContextConfiguration;
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
        assertThat(quotient.getUncertainty()).isCloseTo(valueOf(0.0455), Offset.offset(valueOf(0.001)));
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
        ConfigurationService.withAspect(MathContextConfiguration.class,
            (o) -> o.withContext(new MathContext(4)),
            () -> {
                A a1 = new A(valueOf(1), valueOf(0.1));
                A a2 = new A(valueOf(1.1), valueOf(0.05));
                UncertainNumber<BigDecimal> combined = a1.combined(a2);
                assertThat(combined.getValue()).isEqualTo(valueOf(1.08));
                assertThat(combined.getUncertainty()).isEqualTo(new BigDecimal("0.04472"));
            }
        );
    }

    @Test
    void equals() {
        A a = new A(valueOf(1), valueOf(0.1));
        A b = new A(valueOf(1.05), valueOf(0.0001));
        assertThat(a.eq(b, 1)).isTrue();
        assertThat(b.eq(a, 1)).isTrue();
        assertThat(a.eq(a, 1)).isTrue();

        UND d1 = new UND(1d, 0.1);
        UND d2 = new UND(Double.NaN, 0.1);
        assertThat(d1.eq(d2, 1)).isFalse();
        assertThat(d2.eq(d1, 1)).isFalse();

        assertThat(d2.eq(new UND(Double.NaN, 2d), 1)).isTrue();
    }

}
