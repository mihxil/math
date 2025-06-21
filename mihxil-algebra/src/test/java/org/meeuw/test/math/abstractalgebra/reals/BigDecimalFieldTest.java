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
package org.meeuw.test.math.abstractalgebra.reals;

import java.math.BigDecimal;
import java.math.MathContext;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.DoubleArbitrary;
import org.junit.jupiter.api.Test;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.theories.abstractalgebra.CompleteScalarFieldTheory;
import org.meeuw.theories.abstractalgebra.MetricSpaceTheory;
import org.meeuw.math.numbers.BigDecimalOperations;
import org.meeuw.math.numbers.MathContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalField.INSTANCE;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class BigDecimalFieldTest implements
    CompleteScalarFieldTheory<BigDecimalElement>,
    MetricSpaceTheory<BigDecimalElement, BigDecimalElement> {

    @Test
    public void test() {
        withLooseEquals(() -> {
            assertThat(
                of(5d).times(2).times(of(6d))
            ).isEqualTo(of(60d));
        });
    }

    @Test
    public void uncertaintyOfDoubles() {

        // uncertainty in the double
        assertThat(of(5).getUncertainty()).isEqualTo("1.8E-15");
        assertThat(of(50).getUncertainty()).isEqualTo("1.4E-14");
        assertThat(of(5e-4).getUncertainty()).isEqualTo("2.2E-19");

        assertThat(of(5e-4).times(5).getUncertainty()).isEqualTo("1.1E-18");
        assertThat(of(4_503_599_627_370_497d).getUncertainty()).isEqualTo("2.0");
    }

    @Test
    public void uncertaintyOfBigDecimal() {
        // BigDecimals intrinsically know their uncertainty, and can be exact.
        assertThat(of("5").getUncertainty()).isEqualTo("0");
    }
    @Test
    public void uncertaintyPropagation() {
        assertThat(
            of(4_503_599_627_370_497d)
                .minus(
                    of(4_503_599_627_370_496d
                    )).getUncertainty())
            .isCloseTo(new BigDecimal("2.8"), Offset.offset(new BigDecimal("0.1")));
    }

    @Test
    public void divisionUncertainty() {
        // by division, exactness gets lost
        BigDecimalElement half = of("1").dividedBy(of("2"));
        assertThat(half.getUncertainty()).isEqualTo("1e-" + BigDecimalOperations.INSTANCE.context().getPrecision()); //

    }

    @Test
    public void divisionUncertaintyConfiguredLessPrecise() {
        ConfigurationService.withAspect(MathContextConfiguration.class, mc ->
            mc.withContext(new MathContext(2)), () -> {
            BigDecimalElement half = of("1").dividedBy(of("2"));
            assertThat(half.getUncertainty()).isEqualTo("0.01"); //
        });

    }

    @Test
    public void basic() {
        assertThat(of(5).minus(of(4))).isEqualTo(of(1));
        assertThat(of(5).p(INSTANCE.zero())).isEqualTo(of(5));
        assertThat(of("-539.4562718339926").plus(INSTANCE.zero())).isEqualTo(of("-539.4562718339926"));
    }

    @Test
    public void reciprocalExample() {
        BigDecimalElement e = of(-859.3420301563415);
        BigDecimalElement reciprocal = e.reciprocal();
        BigDecimalElement timesItself = reciprocal.times(e);
        assertThat(timesItself.eq(e.getStructure().one()))
            .withFailMessage("%s · %s ^ -1 = %s != 1", e, e, timesItself )
            .isTrue();
    }

    @Test
    public void pow() {
        BigDecimalElement base = of(300);
        BigDecimalElement exponent = of(-30.1);
        assertThat(base.pow(exponent).doubleValue()).isNotEqualTo(0d);
    }

    @Test
    public void tetration() {

        BigDecimalElement two = of(2);

        withLooseEquals(() -> {

            assertThat(two.tetration(2)).isEqualTo(of(4));
            assertThat(two.tetration(3)).isEqualTo(of(16));
            assertThat(two.tetration(4)).isEqualTo(of(65_536));
            assertThat(two.tetration(5)).isEqualTo(of("2.003529930406846464979072351560255750447825475569751419265016973710894059556311453089506130880933348E+19728"));

        });


        BigDecimalElement three = of(3);

        withLooseEquals(() -> {
            assertThat(three.tetration(2)).isEqualTo(of(27));
            assertThat(three.tetration(3)).isEqualTo(of("7625597484987"));
        });

    }


    @Property
    public void timesDouble(
        @ForAll(ELEMENTS) BigDecimalElement e,
        @ForAll("doubles") Double multiplier) {
        assertThat(e.times(multiplier).getValue().doubleValue())
            .isCloseTo(e.getValue().doubleValue() * multiplier, Percentage.withPercentage(0.001));
    }

    @Provide
    public DoubleArbitrary doubles() {
        return Arbitraries.doubles();
    }

    @Override
    @Provide
    public Arbitrary<BigDecimalElement> elements() {
        return Arbitraries.randomValue((random) -> of(2000 * random.nextDouble() - 1000))
            .injectDuplicates(0.1)
            .dontShrink()
            .edgeCases(config -> {
                config.add(BigDecimalElement.ZERO);
                config.add(BigDecimalElement.ONE);
                config.add(BigDecimalElement.ONE.negation());
            })
            ;
    }
}
