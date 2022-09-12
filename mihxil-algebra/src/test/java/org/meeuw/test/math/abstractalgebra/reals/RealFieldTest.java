/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.abstractalgebra.reals;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.test.*;
import org.meeuw.math.exceptions.InvalidUncertaintyException;

import static java.lang.Double.NaN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.reals.RealField.INSTANCE;
import static org.meeuw.math.abstractalgebra.reals.RealNumber.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class RealFieldTest implements
    CompleteScalarFieldTheory<RealNumber>,
    MetricSpaceTheory<RealNumber, RealNumber>,
    UncertainDoubleTheory<RealNumber> {

    @Test
    public void test() {
        assertThatThrownBy(() -> new RealNumber(1, 0)._of(1, -1)).isInstanceOf(InvalidUncertaintyException.class);
        assertThat(of(5d).times(2).times(of(6d))).isEqualTo(of(60d));
        assertThat(of(0d).getConfidenceInterval().getLow()).isEqualTo(RealNumber.EPSILON_FACTOR * -4.9E-324);
        assertThat(of(0d).getConfidenceInterval().getHigh()).isEqualTo(RealNumber.EPSILON_FACTOR * 4.9E-324);

    }

    @Test
    public void confidenceInterval() {
        assertThat(new RealNumber(5, 0.5).equals(new RealNumber(6, 2))).isTrue();
        assertThat(new RealNumber(6, 2).equals(new RealNumber(5, 0.5))).isTrue();
        assertThat(new RealNumber(6, 2).equals(new RealNumber(5, 1.5))).isTrue();
        assertThat(new RealNumber(6, 0.1).equals(new RealNumber(5, 0.1))).isFalse();
    }

    @Test
    public void string() {
        assertThat(RealNumber.of(1).toString()).isEqualTo("1");
        RealNumber half  = of(1).dividedBy(of(2));
        assertThat(half.getUncertainty()).isEqualTo(8.881784197001247E-16);
        assertThat(half.toString()).isEqualTo("0.5"); // rounding errors only
        assertThat(new RealNumber(5, 0.1).toString()).isEqualTo("5.00 ± 0.10");
    }

    @Test
    public void minus() {
        assertThat(of(1).minus(of(0))).isEqualTo(of(1));
    }

    @Test
    public void fractionalUncertainty() {
        RealNumber ex = new RealNumber(2.36, 0.04);
        assertThat(ex.getFractionalUncertainty()).isEqualTo(0.016666666666666666);
        assertThat(ex.sqr().getFractionalUncertainty()).isEqualTo(0.03225806451612903);
        assertThat(ex.sqr().toString()).isEqualTo("5.57 ± 0.19");
    }

    @Test
    public void nearZero() {
        RealNumber zero = RealNumber.SMALLEST;
        RealNumber someNumber = new RealNumber(5, 0.1);

        RealNumber product = someNumber.times(zero);
        log.info("{} . {} = {}", someNumber, zero, product);
    }

    @Test
    public void considerMultiplicationByZero() {
        RealNumber nan = new RealNumber(NaN, 1d);
        RealNumber zero = new RealNumber(0, 1d);

        Assertions.assertThat(INSTANCE.considerMultiplicationBySpecialValues(nan, zero).getValue()).isEqualTo(Double.valueOf(NaN));
        assertThat(INSTANCE.considerMultiplicationBySpecialValues(zero, nan).getValue()).isEqualTo(Double.valueOf(NaN));

        assertThat(new RealNumber(5, 1).times(ZERO)).isEqualTo(ZERO);
        assertThat(new RealNumber(5, 1).times(new RealNumber(0, 1)).getValue()).isEqualTo(0);
        assertThat(new RealNumber(5, 1).times(new RealNumber(0, 1)).getUncertainty()).isEqualTo(1);
        assertThat(new RealNumber(0, 1).times(new RealNumber(0, 1)).getUncertainty()).isEqualTo(4.9E-324);

    }

    @Test
    public void considerMultiplicationByNaN() {
        RealNumber a = new RealNumber(NaN, 1d);
        RealNumber b = new RealNumber(1, 1d);


        assertThat(INSTANCE.considerMultiplicationBySpecialValues(a, b).getValue()).isEqualTo(Double.valueOf(NaN));
        assertThat(INSTANCE.considerMultiplicationBySpecialValues(b, a).getValue()).isEqualTo(Double.valueOf(NaN));

        assertThat(new RealNumber(5, 1).times(ZERO)).isEqualTo(ZERO);
    }

    @Test
    public void divideOne() {
        RealNumber divided = ONE.dividedBy(999999L);
        RealNumber multiplied = divided.times(999999L);
        assertThat(multiplied).isEqualTo(ONE);
    }

    @Test
    public void adjugate() {
        RealNumber[][] realNumbers = new RealNumber[][] {
            new RealNumber[]{of(-3), of(2), of(-5)},
            new RealNumber[]{of(-1), of(0), of(-2)},
            new RealNumber[]{of(3), of(-4), of(-1)}
        };

        assertThat(INSTANCE.adjugate(realNumbers)).isDeepEqualTo(
            new RealNumber[][] {
                new RealNumber[]{of(-8), of(22), of(-4)},
                new RealNumber[]{of(-7), of(18), of(-1)},
                new RealNumber[]{of(4), of(-6), of(2)}
            }
        );
    }

    @Test
    public void ln() {
        RealNumber ln = of(800).ln();
        log.info("ln(800) = {}", ln);
        assertThat(ln.toString()).isEqualTo("6.68461172766793");
    }

    @Test
    public void weightedAverageOfZeros() {
        RealNumber n1 = RealNumber.of(0);
        RealNumber n2 = RealNumber.of(0);
        RealNumber weighted = n1.weightedAverage(n2);
        assertThat(weighted).isEqualTo(RealNumber.of(0));
    }

    @Test
    public void determinant2() {
           RealNumber[][] realNumbers = new RealNumber[][] {
            new RealNumber[]{RealNumber.of(1), RealNumber.of(2)},
            new RealNumber[]{RealNumber.of(3), RealNumber.of(4)},
        };

        assertThat(RealField.INSTANCE.determinant(realNumbers)).isEqualTo(RealNumber.of(-2));
    }

    @Test
    public void weightedAverageOfZero() {
        RealNumber n1 = RealNumber.of(-906.2970587338823);
        RealNumber n2 = RealNumber.of(0);
        RealNumber weighted = n1.weightedAverage(n2);
        assertThat(weighted).isEqualTo(RealNumber.of(0));
    }

    @Override
	@Provide
    public Arbitrary<RealNumber> elements() {
        return
            Arbitraries.randomValue(
                (random) -> of(2000 * random.nextDouble() - 1000))
                .injectDuplicates(0.1)
                .dontShrink()
                .edgeCases(realNumberConfig -> {
                    realNumberConfig.add(RealNumber.of(0));
                    realNumberConfig.add(RealNumber.of(-1));
                    realNumberConfig.add(ONE);
                    realNumberConfig.add(ZERO);
                    realNumberConfig.add(RealNumber.of(1));
                })
            ;
    }
}
