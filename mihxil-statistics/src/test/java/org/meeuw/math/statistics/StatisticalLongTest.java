package org.meeuw.math.statistics;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.test.CompleteFieldTheory;
import org.meeuw.math.statistics.text.TimeConfiguration;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.meeuw.configuration.ConfigurationService.with;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */

class StatisticalLongTest implements CompleteFieldTheory<UncertainReal> {

    @Test
    public void instants() {
        with(TimeConfiguration.class, tz -> tz.withZoneId(ZoneId.of("Europe/Amsterdam")), () -> {
            Instant now = Instant.ofEpochMilli(1593070087406L);
            String expected = now.truncatedTo(ChronoUnit.MILLIS).toString();
            StatisticalLong mes = new StatisticalLong(StatisticalLong.Mode.INSTANT);

            mes.enter(now, now.plus(Duration.ofMillis(-400)), now.minus(Duration.ofMillis(500)));
            assertThat(mes.getRoundedMean()).isEqualTo(1593070087100L);
            //assertThat(mes.toString()).startsWith(expected);
            assertThat(mes.toString()).isEqualTo("2020-06-25T09:28:07.106 ± PT0.216S");

            assertThatThrownBy(() -> mes.enter(Duration.ofMillis(100))).isInstanceOf(IllegalStateException.class);
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
        assertThat(mes.getStandardDeviation()).isEqualTo(2.179449471770337);

        mes.reset();

        mes.enter(7, 6, 5, 4, 3, 2, 1, 0);
        assertEquals(mes.getGuessedMean(), 7);
        assertThat(mes.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(mes.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 *3 + 4 * 4 + 5 * 5 + 6 *6 + 7*7);
        assertThat(mes.getValue()).isEqualTo(3.5);
        assertThat(mes.getStandardDeviation()).isEqualTo(2.179449471770337);
        assertThat(mes.toString()).isEqualTo("4 ± 2");

        assertThatThrownBy(() -> mes.enter(Duration.ofMillis(100))).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> mes.enter(Instant.now())).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void combine() {
        StatisticalLong stat1 = new StatisticalLong();
        stat1.enter(0, 2, 4, 6);
        StatisticalLong stat2  = new StatisticalLong();
        stat2.enter(1, 3, 5, 7);

        StatisticalLong statCombined = stat1.combined(stat2);

        assertThat(statCombined.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(statCombined.getSum()).isEqualTo(statCombined.getUncorrectedSum());
        assertThat(statCombined.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 * 3 + 4 * 4 + 5 * 5 + 6 * 6 + 7 * 7);
        assertThat(statCombined.getSumOfSquares()).isEqualTo(statCombined.getUncorrectedSumOfSquares());

        assertThat(statCombined.getValue()).isEqualTo(3.5);
        assertThat(statCombined.getStandardDeviation()).isEqualTo(2.179449471770337);
        assertThat(statCombined.getGuessedMean()).isEqualTo(0);


        statCombined.reguess();
        assertThat(statCombined.getGuessedMean()).isEqualTo(3);

        assertThat(statCombined.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(statCombined.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 * 3 + 4 * 4 + 5 * 5 + 6 * 6 + 7 * 7);
        assertThat(statCombined.getValue()).isEqualTo(3.5);
        assertThat(statCombined.getStandardDeviation()).isEqualTo(2.179449471770337);
        assertThat(statCombined.toString()).isEqualTo("4 ± 2");

        UncertainDouble<?> combinedMeasurement = stat1.immutableCopy().combined(stat2.immutableCopy());

        assertThat(combinedMeasurement.getValue()).isEqualTo(3.5);

        assertThat(combinedMeasurement.toString()).isEqualTo("3.5 ± 1.6");
    }

    @Test
    public void timesAndPlus() {
        StatisticalLong mes = new StatisticalLong(StatisticalLong.Mode.DURATION);

        mes.enter(Duration.ofSeconds(100), Duration.ofSeconds(90), Duration.ofSeconds(110));

        StatisticalLong mesTimes3 = mes.times(3.0);

        assertThat(mesTimes3.durationValue()).isEqualTo(Duration.ofMinutes(5));
        assertThat(mesTimes3.toString()).isEqualTo("PT5M ± PT24.494S");

        StatisticalLong mesPlus1 = mesTimes3.plus(Duration.ofMinutes(1));
        assertThat(mesPlus1.toString()).isEqualTo("PT6M ± PT24.494S");
    }

    @Test
    public void dividedOne() {
        StatisticalLong minusOne = new StatisticalLong();
        minusOne.enter(-1, -1);
        UncertainDoubleElement divided = minusOne.dividedBy(26904L);
        UncertainReal multiplied = divided.times(26904L);
        assertThat(multiplied).isEqualTo(minusOne);
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
