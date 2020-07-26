package org.meeuw.math;

import java.time.*;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
class UtilsTest {
    ZoneId id = ZoneId.of("Europe/Amsterdam");

    @Test
    void orderOfMagnitude() {
    }

    @Test
    void roundDuration() {
        assertThat(Utils.round(Duration.ofMillis(12345567L), ChronoUnit.SECONDS).toString()).isEqualTo("PT3H25M45S");
        assertThat(Utils.round(Duration.ofMillis(12345567L), ChronoUnit.MINUTES).toString()).isEqualTo("PT3H26M");
        assertThat(Utils.round(Duration.ofMillis(12345567L), ChronoUnit.MILLIS).toString()).isEqualTo("PT3H25M45.567S");
        assertThat(Utils.round(Duration.ofMillis(1112345567L), ChronoUnit.DAYS).toString()).isEqualTo("PT309H");
        assertThat(Utils.round(Duration.ofMillis(1112345567L), ChronoUnit.HOURS).toString()).isEqualTo("PT309H");

    }

    @Test
    void roundInstant() {
        assertThat(Utils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.MINUTES)).isEqualTo("2018-10-16T15:48:15");
        assertThat(Utils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.SECONDS)).isEqualTo("2018-10-16T15:48:15.592");

        assertThat(Utils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.HOURS)).isEqualTo("2018-10-16T15:48:00");
        //assertThat(Utils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.HOURS)).isEqualTo("2018-10-16T15:48"); TODO

        assertThat(Utils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.DAYS)).isEqualTo("2018-10-16");
        assertThat(Utils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.WEEKS)).isEqualTo("2018-10-16");
    }
}
