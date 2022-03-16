package org.meeuw.test.math;

import java.time.Duration;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.TimeUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TimeUtilsTest {


    ZoneId id = ZoneId.of("Europe/Amsterdam");

    @Test
    void orderOfMagnitude() {
        Assertions.assertThat(TimeUtils.orderOfMagnitude(Duration.ofMillis(1))).isEqualTo(ChronoUnit.MILLIS);
        assertThat(TimeUtils.orderOfMagnitude(Duration.ofSeconds(100))).isEqualTo(ChronoUnit.SECONDS);
        assertThat(TimeUtils.orderOfMagnitude(Duration.ofMinutes(100))).isEqualTo(ChronoUnit.MINUTES);
        assertThat(TimeUtils.orderOfMagnitude(Duration.ofHours(20))).isEqualTo(ChronoUnit.HOURS);
        assertThat(TimeUtils.orderOfMagnitude(Duration.ofDays(10))).isEqualTo(ChronoUnit.DAYS);
    }

    @Test
    void roundDuration() {
        assertThat(TimeUtils.round(Duration.ofMillis(12345567L), ChronoUnit.SECONDS).toString()).isEqualTo("PT3H25M45S");
        assertThat(TimeUtils.round(Duration.ofMillis(12345567L), ChronoUnit.MINUTES).toString()).isEqualTo("PT3H26M");
        assertThat(TimeUtils.round(Duration.ofMillis(12345567L), ChronoUnit.MILLIS).toString()).isEqualTo("PT3H25M45.567S");
        assertThat(TimeUtils.round(Duration.ofMillis(1112345567L), ChronoUnit.DAYS).toString()).isEqualTo("PT309H");
        assertThat(TimeUtils.round(Duration.ofMillis(1112345567L), ChronoUnit.HOURS).toString()).isEqualTo("PT309H");
        assertThatThrownBy(() -> TimeUtils.round(Duration.ofMillis(1112345567L), ChronoUnit.YEARS)).isInstanceOf(IllegalArgumentException.class);
    }
}
