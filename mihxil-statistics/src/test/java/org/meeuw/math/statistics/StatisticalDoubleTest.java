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
package org.meeuw.math.statistics;

import lombok.extern.java.Log;

import java.util.Random;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.theories.abstractalgebra.CompleteScalarFieldTheory;
import org.meeuw.theories.abstractalgebra.UncertainDoubleTheory;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.uncertainnumbers.CompareConfiguration;
import org.meeuw.math.uncertainnumbers.ConfidenceIntervalConfiguration;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

import static net.jqwik.api.RandomDistribution.uniform;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.configuration.ConfigurationService.getConfigurationAspect;
import static org.meeuw.configuration.ConfigurationService.withAspect;


/**
 * @author Michiel Meeuwissen
 */
@Log
public class StatisticalDoubleTest implements
    UncertainDoubleTheory<RealNumber>,
    CompleteScalarFieldTheory<RealNumber> {


    @Test
    public void test1() {
        assertThat(new StatisticalDoubleImpl().enter(-10, 20).toString()).isEqualTo("5 ± 15");
    }

    @Test
    public void test2() {
        assertThat(new StatisticalDoubleImpl().enter(0, 0).toString()).isEqualTo("0");
    }

    @Test
    public void test3() {
        assertThat(new StatisticalDoubleImpl().enter(0, 1).toString()).isEqualTo("0.5 ± 0.5");
    }

    @Test
    public void test4() {
        assertThat(new StatisticalDoubleImpl().enter(-0.001, -0.002).toString()).isEqualTo("-0.0015 ± 0.0005");
    }

    @Test
    public void test5() {
        assertThat(new StatisticalDoubleImpl().enter(0.5, 1.5).toString()).isEqualTo("1.0 ± 0.5");
    }

    @Test
    public void test6() {
        assertThat(new StatisticalDoubleImpl().enter(20000, 20001, 20002, 20003).toString()).isEqualTo("20001.5 ± 1.1");
    }

    @Test
    public void test7() {
        assertThat(new StatisticalDoubleImpl().enter(20000, 20010, 20020, 20030).toString()).isEqualTo("20015 ± 11");
    }

    @Test
    public void test8() {
        assertThat(new StatisticalDoubleImpl().enter(20000, 20100, 20200, 20300).toString()).isEqualTo("(2.015 ± 0.011)·10⁴");
    }
    @Test
    public void test9 () {
        assertThat(new StatisticalDoubleImpl().enter(0.000002, 0.0000021, 0.0000022, 0.0000023).toString()).isEqualTo("(2.15 ± 0.11)·10⁻⁶");
    }

    @Test
    public void test10() {
        StatisticalDoubleImpl mes = new StatisticalDoubleImpl().enter(0.0000002, 0.000000201, 0.000000202, 0.000000203);
        assertThat(mes.toString()).isEqualTo("(2.015 ± 0.011)·10⁻⁷");
        assertThat(mes.plus(1d).toString()).isEqualTo("1.0000002015"); // error disappear due to rounding

        assertThat(mes.dividedBy(2).toString()).isEqualTo("(1.008 ± 0.006)·10⁻⁷"); // error disappear due to rounding

    }

    @Test
    public void testEqualsWhenNoValues() {
        StatisticalDoubleImpl d1 = new StatisticalDoubleImpl();
        StatisticalDoubleImpl d2 = new StatisticalDoubleImpl();
        assertThat(d1).isEqualTo(d2);

        assertThat(d1.eq(d2)).isTrue();

    }

    @Test
    public void testEqualsWhenOnlyOne() {
        StatisticalDoubleImpl d1 = new StatisticalDoubleImpl();
        StatisticalDoubleImpl d2 = new StatisticalDoubleImpl();
        d1.enter(0.5);
        d2.enter(0.5);
        assertThat(d1).isEqualTo(d2);
        assertThat(d1.eq(d2)).isTrue();
    }

    @Test
    public void testWhenNoValues() {
        StatisticalDouble d1 = new StatisticalDoubleImpl();
        assertThatThrownBy(d1::getMean).isInstanceOf(DivisionByZeroException.class);
    }

    @Test
    public void reset() {
        StatisticalDoubleImpl d = new StatisticalDoubleImpl();
        d.enter(1, 2, 3);
        d.reset();
        d.enter(3, 4, 5);
        assertThat(d.doubleValue()).isEqualTo(4);
        assertThat(d.doubleValue()).isEqualTo(4);
        assertThat(d.getMin()).isEqualTo(3);
        assertThat(d.getMax()).isEqualTo(5);
    }

    @Property
    public void testString(@ForAll(ELEMENTS) AlgebraicElement<?> e) {
        StatisticalDoubleImpl casted = (StatisticalDoubleImpl) e;
        log.info(casted.getCount() + " " + e);
    }

    @Test
    public void intransitiveEq() {

        log.info("SDS: " + getConfigurationAspect(ConfidenceIntervalConfiguration.class).getSds());
        StatisticalDoubleImpl d1 = new StatisticalDoubleImpl();
        d1.enter(0, 5, 10);
        StatisticalDoubleImpl d2 = new StatisticalDoubleImpl();
        d2.enter(5, 10, 15);
        StatisticalDoubleImpl d3 = new StatisticalDoubleImpl();
        d3.enter(10, 15, 20);
        assertThat(d1.eq(d2)).isTrue();
        assertThat(d2.eq(d3)).isTrue();
        assertThat(d1.eq(d3)).isFalse();

        withAspect(CompareConfiguration.class, compareConfiguration -> compareConfiguration.withEqualsIsStrict(false), () -> {

            assertThat(d1.equals(d2)).isTrue();
            assertThat(d2.equals(d3)).isTrue();
            assertThat(d1.equals(d3)).isFalse();
        });
        withAspect(CompareConfiguration.class, compareConfiguration -> compareConfiguration.withEqualsIsStrict(true), () -> {

            assertThat(d1.equals(d2)).isFalse();
            assertThat(d2.equals(d3)).isFalse();
            assertThat(d1.equals(d3)).isFalse();
        });
    }

    @Test
    public void anotherZero () {
        StatisticalDoubleImpl zero = new StatisticalDoubleImpl();
        zero.enter(0d, 0d, 0d, 0d, 0d, 0d, 0d);
        assertThat(zero.getStructure().zero().eq(zero)).isTrue();
        assertThat(zero.isZero()).isTrue();

        zero.enter(5e-324);

        assertThat(zero.isExactlyZero()).isFalse(); // it _is_ not zero
        assertThat(zero.getStructure().zero().eq(zero)).isTrue();
        assertThat(zero.isZero()).isTrue();
// but it is close enough
    }

    @Test
    public void multipleEq() {
        StatisticalDoubleImpl instance = new StatisticalDoubleImpl();
        instance.enter(1d, 2d, 1d, 2d, 1d, 1d, 1d);
        instance.eq(instance);
        RealNumber uncertainDoubleElement = instance.immutableCopy();
        instance.eq(uncertainDoubleElement);



    }

    @Override
    public Arbitrary<RealNumber> elements() {
        Arbitrary<Integer> amounts = Arbitraries.integers()
            .between(1, 100)
            .shrinkTowards(2)
            .withDistribution(uniform());
        Arbitrary<Double> averages = Arbitraries
            .doubles()
            .between(-1000d, 1000d);
        Arbitrary<Random> randoms = Arbitraries.randoms();
        return Combinators.combine(amounts, averages, randoms)
            .flatAs((am, av, r) -> {
                StatisticalDoubleImpl sd = new StatisticalDoubleImpl();
                r.doubles(am).forEach(d ->
                    sd.accept(av + d * av / 3)
                );
                return Arbitraries.of(sd);
            });
    }
}
