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
package org.meeuw.math.abstractalgebra.reals;

import lombok.extern.java.Log;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.exceptions.InvalidUncertaintyException;
import org.meeuw.theories.abstractalgebra.*;

import static java.lang.Double.NaN;
import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.assertj.Assertions.assertThat;
import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.abstractalgebra.reals.DoubleElement.*;
import static org.meeuw.math.abstractalgebra.reals.RealField.INSTANCE;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;

/**
 * @author Michiel Meeuwissen
 * @since 0.4

 */
@Log
class RealFieldTest implements
    CompleteScalarFieldTheory<RealNumber>,
    MetricSpaceTheory<RealNumber, RealNumber>,
    UncertainDoubleTheory<RealNumber> {

    @Test
    public void test() {
        assertThatThrownBy(() -> new DoubleElement(1, 0).immutableInstanceOfPrimitives(1, -1)).isInstanceOf(InvalidUncertaintyException.class);
        assertThatAlgebraically(exactly(5d).times(2).times(of(6d))).isEqualTo(exactly(60d));
        assertThat(of(0d).getConfidenceInterval().getLow()).isEqualTo(DoubleElement.EPSILON_FACTOR * -4.9E-324);
        assertThat(of(0d).getConfidenceInterval().getHigh()).isEqualTo(DoubleElement.EPSILON_FACTOR * 4.9E-324);
    }

    @Test
    public void testToString() {
        DoubleElement uncertainDouble = new DoubleElement(5, 1);
        assertThat(uncertainDouble.toString()).isEqualTo("5.0 ± 1.0");
    }

    @Test
    public void confidenceInterval() {
        assertThat(new DoubleElement(5, 0.5).eq(new DoubleElement(6, 2))).isTrue();
        assertThat(new DoubleElement(6, 2).eq(new DoubleElement(5, 0.5))).isTrue();
        assertThat(new DoubleElement(6, 2).eq(new DoubleElement(5, 1.5))).isTrue();
        assertThat(new DoubleElement(6, 0.1).eq(new DoubleElement(5, 0.1))).isFalse();
    }

    @Test
    public void confidenceIntervalInf() {
        assertThat(new DoubleElement(Double.POSITIVE_INFINITY, 10).eq(new DoubleElement(Double.POSITIVE_INFINITY, 20))).isTrue();
    }

    @Test
    public void string() {
        assertThat(DoubleElement.of(1).toString()).isEqualTo("1.0000000000000000");
        RealNumber half  = of(1).dividedBy(of(2));
        assertThat(half.doubleUncertainty()).isEqualTo(6.661338147750939E-16);
        assertThat(half.toString()).isEqualTo("0.5000000000000000"); // rounding errors only
        assertThat(new DoubleElement(5, 0.1).toString()).isEqualTo("5.00 ± 0.10");
    }
    @Test
    public void stringOfExact() {
        double val = 0.1482401572043123_1;
        RealNumber rn = RealNumber.of(val);
        assertThat(rn.toString()).isEqualTo("" + val);
    }

    @Test
    public void minus() {
        assertThatAlgebraically(of(1).minus(of(0))).isEqualTo(of(1));
    }

    @Test
    public void fractionalUncertainty() {
        DoubleElement ex = new DoubleElement(2.36, 0.04);
        assertThat(ex.doubleFractionalUncertainty()).isEqualTo(0.016666666666666666);
        assertThat(ex.sqr().doubleFractionalUncertainty()).isEqualTo(0.03278688524590194);
        assertThat(ex.sqr().toString()).isEqualTo("5.57 ± 0.19");
    }

    @Test
    public void nearZero() {
        DoubleElement zero = DoubleElement.SMALLEST;
        DoubleElement someNumber = new DoubleElement(5, 0.1);

        DoubleElement product = someNumber.times(zero);
        log.info("%s . %s = %s".formatted(someNumber, zero, product));
    }

    @Test
    public void considerMultiplicationByZero() {
        DoubleElement nan = new DoubleElement(NaN, 1d);
        DoubleElement zero = new DoubleElement(0, 1d);

        Assertions.assertThat(DoubleElement.considerMultiplicationBySpecialValues(nan, zero).doubleValue()).isEqualTo(Double.valueOf(NaN));
        assertThat(DoubleElement.considerMultiplicationBySpecialValues(zero, nan).doubleValue()).isEqualTo(Double.valueOf(NaN));

        assertThatAlgebraically(new DoubleElement(5, 1).times(ZERO)).isEqualTo(ZERO);
        assertThat(new DoubleElement(5, 1).times(new DoubleElement(0, 1)).doubleValue()).isEqualTo(0);
        assertThat(new DoubleElement(5, 1).times(new DoubleElement(0, 1)).doubleUncertainty()).isEqualTo(6);
        assertThat(new DoubleElement(0, 1).times(new DoubleElement(0, 1)).doubleUncertainty()).isEqualTo(2);

    }

    @Test
    public void considerMultiplicationByNaN() {
        DoubleElement a = new DoubleElement(NaN, 1d);
        DoubleElement b = new DoubleElement(1, 1d);


        assertThat(DoubleElement.considerMultiplicationBySpecialValues(a, b).doubleValue()).isEqualTo(Double.valueOf(NaN));
        assertThat(DoubleElement.considerMultiplicationBySpecialValues(b, a).doubleValue()).isEqualTo(Double.valueOf(NaN));

        assertThatAlgebraically(new DoubleElement(5, 1).times(ZERO)).isEqualTo(ZERO);
    }

    @Test
    public void divideOne() {
        withLooseEquals(() -> {
            RealNumber divided = ONE.dividedBy(999999L);
            RealNumber multiplied = divided.times(999999L);
            assertThatAlgebraically(multiplied).isEqualTo(ONE);
        });
    }

    @Test
    public void adjugate() {
        withLooseEquals(() -> {

            DoubleElement[][] realNumbers = new DoubleElement[][]{
                new DoubleElement[]{exactly(-3), exactly(2), exactly(-5)},
                new DoubleElement[]{exactly(-1), exactly(0), exactly(-2)},
                new DoubleElement[]{exactly(3), exactly(-4), exactly(-1)}
            };

            assertThat(INSTANCE.adjugate(realNumbers)).isDeepEqualTo(
                new DoubleElement[][]{
                    new DoubleElement[]{exactly(-8), exactly(22), exactly(-4)},
                    new DoubleElement[]{of(-7), of(18), of(-1)},
                    new DoubleElement[]{of(4), of(-6), of(2)}
                }
            );
        });
    }

    @Test
    public void ln() {
        RealNumber ln = of(800).ln();
        log.info("ln(800) = " + ln);
        assertThat(ln.toString()).isEqualTo("6.68461172766793");
    }

    @Test
    public void weightedAverageOfZeros() {
        RealNumber n1 = RealNumber.of(0);
        RealNumber n2 = RealNumber.of(0);
        RealNumber weighted = n1.weightedAverage(n2);
        assertThatAlgebraically(weighted).isEqualTo(DoubleElement.of(0));
    }

    @Test
    public void determinant2() {
           DoubleElement[][] realNumbers = new DoubleElement[][] {
            new DoubleElement[]{DoubleElement.of(1), DoubleElement.of(2)},
            new DoubleElement[]{DoubleElement.of(3), DoubleElement.of(4)},
        };

        assertThatAlgebraically(RealField.INSTANCE.determinant(realNumbers)).isEqualTo(DoubleElement.of(-2));
    }

    @Test
    public void weightedAverageOfZero() {
        withLooseEquals(() -> {
            DoubleElement n1 = DoubleElement.of(-906.2970587338823);
            DoubleElement n2 = DoubleElement.of(0);
            RealNumber weighted = n1.weightedAverage(n2);
            assertThatAlgebraically(weighted).isEqualTo(DoubleElement.of(0));
        });
    }

    @Override
    @Provide
    public Arbitrary<RealNumber> elements() {
        return
            Arbitraries.randomValue(
                (random) ->
                    RealNumber.of(2000 * random.nextDouble() - 1000))
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

    @Test
    public void sinPi() {
        RealNumber sin = DoubleElement.of(PI).sin();
        assertThatAlgebraically(sin).isEqTo(INSTANCE.zero());
    }

    @Test
    public void pow() {
        DoubleElement w = new DoubleElement(-1971, 680);
        assertThat(w.pow(-2).doubleUncertainty()).isPositive();
    }



}
