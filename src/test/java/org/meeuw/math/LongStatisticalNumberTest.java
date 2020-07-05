package org.meeuw.math;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */

class LongStatisticalNumberTest {

    @Test
    public void instants() {
        Instant now = Instant.ofEpochMilli(1593070087406L);
        String expected = now.truncatedTo(ChronoUnit.MILLIS).toString();
        StatisticalLong mes = new StatisticalLong(StatisticalLong.Mode.INSTANT);

        mes.enter(now, now.plus(Duration.ofMillis(-400)), now.minus(Duration.ofMillis(500)));
        assertThat(mes.getRoundedMean()).isEqualTo(1593070087000L);
        //assertThat(mes.toString()).startsWith(expected);
        assertThat(mes.toString()).isEqualTo("2020-06-25T09:28:07.106 ± PT0.216S");
    }


    @Test
    public void longs() {
        StatisticalLong mes = new StatisticalLong();
        mes.enter(0, 1, 2, 3, 4, 5, 6, 7);
        assertThat(mes.getGuessedMean()).isEqualTo(0);
        assertThat(mes.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(mes.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 * 3 + 4 * 4 + 5 * 5  + 6 * 6 + 7 *7);

        assertThat(mes.doubleValue()).isEqualTo(3.5);
        assertThat(mes.getStandardDeviation()).isEqualTo(2.179449471770337);

        mes.reset();

        mes.enter(7, 6, 5, 4, 3, 2, 1, 0);
        assertEquals(mes.getGuessedMean(), 7);
        assertThat(mes.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(mes.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 *3 + 4 * 4 + 5 * 5 + 6 *6 + 7*7);
        assertThat(mes.doubleValue()).isEqualTo(3.5);
        assertThat(mes.getStandardDeviation()).isEqualTo(2.179449471770337);
        assertThat(mes.toString()).isEqualTo("4 ± 2");

    }

    @Test
    public void combine() {
        StatisticalLong stat1 = new StatisticalLong();
        stat1.enter(0, 2, 4, 6);
        StatisticalLong stat2  = new StatisticalLong();
        stat2.enter(1, 3, 5, 7);

        StatisticalLong statCombined = stat1.combine(stat2);

        assertThat(statCombined.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(statCombined.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 * 3 + 4 * 4 + 5 * 5 + 6 * 6 + 7 * 7);
        assertThat(statCombined.doubleValue()).isEqualTo(3.5);
        assertThat(statCombined.getStandardDeviation()).isEqualTo(2.179449471770337);
        assertThat(statCombined.getGuessedMean()).isEqualTo(0);


        statCombined.reguess();
        assertThat(statCombined.getGuessedMean()).isEqualTo(4);

        assertThat(statCombined.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(statCombined.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 * 3 + 4 * 4 + 5 * 5 + 6 * 6 + 7 * 7);
        assertThat(statCombined.doubleValue()).isEqualTo(3.5);
        assertThat(statCombined.getStandardDeviation()).isEqualTo(2.179449471770337);
        assertThat(statCombined.toString()).isEqualTo("4 ± 2");

        UncertainNumber combinedMeasurement = stat1.measurementCopy().combine(stat2.measurementCopy());

        assertThat(combinedMeasurement.doubleValue()).isEqualTo(3.5);

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

}
