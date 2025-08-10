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

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.uncertainnumbers.field.UncertainDouble;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;
import org.meeuw.theories.abstractalgebra.CompleteScalarFieldTheory;
import org.meeuw.time.UncertainJavaTime;
import org.meeuw.time.text.TimeConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;
import static org.meeuw.time.UncertainJavaTime.Mode.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Log4j2
class StatisticalLongTest implements CompleteScalarFieldTheory<UncertainReal> {

    @Test
    public void instants() {
        ConfigurationService.withAspect(TimeConfiguration.class, tz -> tz.withZoneId(ZoneId.of("Europe/Amsterdam")), () -> {
            Instant now = Instant.ofEpochMilli(1593070087406L);

            StatisticalLong mes = new StatisticalLong(INSTANT);

            mes.enter(now, now.plus(Duration.ofMillis(-400)), now.minus(Duration.ofMillis(500)));
            assertThat(mes.getRoundedMean()).isEqualTo(1593070087100L);
            //assertThat(mes.toString()).startsWith(expected);
            assertThat(mes.toString()).isEqualTo("2020-06-25T09:28:07.106 ± PT0.216S");

            assertThat(mes.getOptionalBigMean()).contains(new BigDecimal("1593070087106"));

            assertThat(mes.with(ChronoField.MILLI_OF_SECOND, 0).toString()).isEqualTo("2020-06-25T07:28:07Z");
            assertThat(mes.until(Instant.parse("2025-06-24T23:15:00Z"), ChronoUnit.DAYS)).isEqualTo(1825L);
            assertThat(mes.until(Instant.parse("2025-06-24T23:15:00Z"), ChronoUnit.HOURS)).isEqualTo(43815L);
            assertThat(mes.until(Instant.parse("2025-06-24T23:15:00Z")).toString()).isEqualTo("PT43815H46M52.894S ± PT0.216S");



            assertThatThrownBy(() -> mes.enter(Duration.ofMillis(100))).isInstanceOf(IllegalStateException.class);

            assertThatThrownBy(() -> mes.add(100L)).isInstanceOf(IllegalStateException.class);
        });
    }

    @Test
    public void longs() {
        StatisticalLong mes = new StatisticalLong();
        mes.enter(0, 1, 2, 3, 4, 5, 6, 7);
        assertThat(mes.getGuessedMean()).isEqualTo(0);
        assertThat(mes.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(mes.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 * 3 + 4 * 4 + 5 * 5  + 6 * 6 + 7 *7);

        assertThat(mes.getValue()).isEqualTo(3.5);
        assertThat(mes.getStandardDeviation()).isEqualTo(2.29128784747792);

        mes.reset();

        mes.enter(7, 6, 5, 4, 3, 2, 1, 0);
        assertThat(mes.getGuessedMean()).isEqualTo(7);
        assertThat(mes.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(mes.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 *3 + 4 * 4 + 5 * 5 + 6 *6 + 7*7);
        assertThat(mes.doubleValue()).isEqualTo(3.5);
        assertThat(mes.getStandardDeviation()).isEqualTo(2.29128784747792);
        assertThat(mes.toString()).isEqualTo("4 ± 2");

        assertThatThrownBy(() -> mes.enter(Duration.ofMillis(100))).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> mes.enter(Instant.now())).isInstanceOf(IllegalStateException.class);

        assertThatThrownBy(() -> mes.add(Duration.ofMillis(100))).isInstanceOf(IllegalStateException.class);

    }

    @Test
    public void combine() {
        StatisticalLong stat1 = new StatisticalLong(LONG);
        stat1.enter(0, 2, 4, 6);
        StatisticalLong stat2  = new StatisticalLong();
        stat2.enter(1, 3, 5, 7);

        StatisticalLong statCombined = stat1.combined(stat2);

        assertThat(statCombined.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(statCombined.getSum()).isEqualTo(statCombined.getUncorrectedSum());
        assertThat(statCombined.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 * 3 + 4 * 4 + 5 * 5 + 6 * 6 + 7 * 7);
        assertThat(statCombined.getSumOfSquares()).isEqualTo(statCombined.getUncorrectedSumOfSquares());

        assertThat(statCombined.doubleValue()).isEqualTo(3.5);
        assertThat(statCombined.getStandardDeviation()).isEqualTo(2.29128784747792);
        assertThat(statCombined.getGuessedMean()).isEqualTo(0);


        statCombined.reguess();
        assertThat(statCombined.getGuessedMean()).isEqualTo(4); // 3.5 is rounded up

        assertThat(statCombined.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(statCombined.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 * 3 + 4 * 4 + 5 * 5 + 6 * 6 + 7 * 7);
        assertThat(statCombined.doubleValue()).isEqualTo(3.5);
        assertThat(statCombined.getStandardDeviation()).isEqualTo(2.29128784747792);
        assertThat(statCombined.toString()).isEqualTo("4 ± 2");

        org.meeuw.math.uncertainnumbers.UncertainDouble<?> combinedMeasurement = stat1.immutableCopy().weightedAverage(stat2.immutableCopy());

        assertThat(combinedMeasurement.getValue()).isEqualTo(3.5);

        assertThat(combinedMeasurement.toString()).isEqualTo("3.5 ± 1.6");
    }

    @Test
    public void timesAndPlus() {
         StatisticalLong mes = new StatisticalLong(DURATION);

        assertThat(mes.optionalDurationValue()).isNotPresent();
        assertThat(mes.getStandardDeviation()).isNaN();

        mes.enter(Duration.ofSeconds(100), Duration.ofSeconds(90), Duration.ofSeconds(110));

        StatisticalLong mesTimes3 = mes.times(3.0);

        assertThat(mesTimes3.durationValue()).isEqualTo(Duration.ofMinutes(5));
        assertThat(mesTimes3.optionalDurationValue()).contains(Duration.ofMinutes(5));
        assertThat(mesTimes3.toString()).isEqualTo("PT5M ± PT24S");

        StatisticalLong mesPlus1 = mesTimes3.plus(Duration.ofMinutes(1));
        assertThat(mesPlus1.toString()).isEqualTo("PT6M ± PT24S");
    }


    @Test
    public void timesAndPlusMs() {
        StatisticalLong mes = new StatisticalLong(DURATION_NS);

        assertThat(mes.optionalDurationValue()).isNotPresent();
        assertThat(mes.getStandardDeviation()).isNaN();

        mes.enter(Duration.ofSeconds(90).plus(Duration.ofMillis(1)), Duration.ofSeconds(90), Duration.ofSeconds(90).minus(Duration.ofMillis(1)));

        StatisticalLong mesTimes3 = mes.times(3.0);

        assertThat(mesTimes3.durationValue()).isEqualTo(Duration.ofSeconds(90 * 3));
        assertThat(mesTimes3.optionalDurationValue()).contains(Duration.ofSeconds(90 * 3));
        assertThat(mesTimes3.toString()).isEqualTo("PT4M30S ± PT0.002S");

        UncertainJavaTime<Double> mesPlus1 = mesTimes3.plus(Duration.ofSeconds(1));
        assertThat(mesPlus1.toString()).isEqualTo("PT4M31S ± PT0.002S");
    }

    @Test
    public void nanoDurations() {
        StatisticalLong mes = new StatisticalLong(DURATION_NS);
        mes.enter(Duration.ofNanos(10), Duration.ofNanos(20));
        assertThat(mes.durationValue()).isEqualTo(Duration.ofNanos(15));
        assertThat(mes.optionalDurationValue()).contains(Duration.ofNanos(15));

        assertThat(mes.getStandardDeviation()).isEqualTo(5.0d); // 5 ns

        mes.enter(Duration.ofMinutes(5));

        assertThat(mes.optionalDurationValue()).contains(Duration.parse("PT1M40.00000001S"));

        assertThatThrownBy(mes::getStandardDeviation).isInstanceOf(OverflowException.class);
    }

    @Test
    public void dividedOne() {
        withLooseEquals(() -> {
            StatisticalLong minusOne = new StatisticalLong();
            minusOne.accept(-1);
            minusOne.accept(-1L);
            UncertainDouble divided = minusOne.dividedBy(26904L);
            UncertainReal multiplied = divided.times(26904L);
            assertThat(multiplied).isEqualTo(minusOne);
        });
    }

    @Test
    public void plusDouble() {
        StatisticalLong mes = new StatisticalLong();
        mes.enter(1, 2);
        StatisticalLong offsetted = mes.plus(3.1);
        assertThat(offsetted.getMean()).isEqualTo(4.6);
    }

    @Test
    public void negativePowerOfZero() {
        StatisticalLong mes = new StatisticalLong();
        mes.enter(-1, 1);
        assertThatThrownBy(() -> {
            UncertainReal ln = mes.ln();
        }).isInstanceOf(IllegalLogarithmException.class);
        assertThatThrownBy(() -> {
            UncertainReal pow = mes.pow(-1);
            log.info("{} ^ {} -> {}", mes, -1, pow);
        }).isInstanceOf(IllegalPowerException.class);
    }

    @Test
    public void zero() {
        StatisticalLong statisticalLong = new StatisticalLong();
        statisticalLong.enter(0L, 0L, 0L, 0L);
        assertThat(statisticalLong.isZero()).isTrue();
        UncertainReal zero = statisticalLong.getStructure().zero();
        assertThat(statisticalLong.eq(zero)).isTrue();
    }

    @Override
    public Arbitrary<UncertainReal> elements() {

        Arbitrary<Integer> amounts = Arbitraries.integers()
            .between(2, 100)
            .shrinkTowards(2)
            .withDistribution(RandomDistribution.uniform());
        Arbitrary<Long> averages = Arbitraries.longs().between(-1000, 1000);
        Arbitrary<Random> random = Arbitraries.randoms();
        return Combinators.combine(amounts, averages, random)
            .flatAs((am, av, r) -> {
                StatisticalLong sd = new StatisticalLong();
                r.doubles(am).forEach(d ->
                    sd.accept((long) (av + d * av / 3))
                );
                return Arbitraries.of(sd);
            });
    }
}
