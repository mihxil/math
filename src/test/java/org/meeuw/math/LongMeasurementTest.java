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

class LongMeasurementTest {

    @Test
    public void instants() {
        Instant now = Instant.ofEpochMilli(1593070087406L);
        String expected = now.truncatedTo(ChronoUnit.SECONDS).toString();
        LongMeasurement mes = new LongMeasurement(LongMeasurement.Mode.INSTANT);

        mes.enter(now, now.plus(Duration.ofMillis(-400)), now.minus(Duration.ofMillis(500)));
        assertThat(mes.getRoundedMean()).isEqualTo(1593070087000L);
        assertThat(mes.toString()).startsWith(expected);
        assertThat(mes.toString()).isEqualTo("2020-06-25T07:28:07Z ± PT0.216S");
    }


    @Test
    public void longs() {
        LongMeasurement mes = new LongMeasurement();
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

    }

    @Test
    public void combine() {
        LongMeasurement mes = new LongMeasurement();
        mes.enter(0, 1, 2, 3);
        LongMeasurement mes2  = new LongMeasurement();
        mes2.enter(4, 5, 6, 7);

        mes.combine(mes2);

        assertThat(mes.getSum()).isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7);
        assertThat(mes.getSumOfSquares()).isEqualTo(1 + 2 * 2 + 3 *3 + 4 * 4 + 5 * 5 + 6 *6 + 7*7);
        assertThat(mes.doubleValue()).isEqualTo(3.5);
        assertThat(mes.getStandardDeviation()).isEqualTo(2.179449471770337);


    }

}
