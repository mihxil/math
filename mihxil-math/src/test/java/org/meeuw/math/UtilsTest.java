package org.meeuw.math;

import lombok.extern.log4j.Log4j2;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.meeuw.math.text.TextUtils;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
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
        assertThat(TextUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.MINUTES)).isEqualTo("2018-10-16T15:48:15");
        assertThat(TextUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.SECONDS)).isEqualTo("2018-10-16T15:48:15.592");

        assertThat(TextUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.HOURS)).isEqualTo("2018-10-16T15:48:00");
        //assertThat(Utils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.HOURS)).isEqualTo("2018-10-16T15:48"); TODO

        assertThat(TextUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.DAYS)).isEqualTo("2018-10-16");
        assertThat(TextUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.WEEKS)).isEqualTo("2018-10-16");
    }


    @Test
    public void incForStream() {
        int[] counters = new int[] {0, 0};
        Utils.IntState state = new Utils.IntState(0, 1);
        Utils.inc(counters, state);
        assertThat(state.max).isEqualTo(1);
        assertThat(counters).containsExactly(1, 0);
        Utils.inc(counters, state);
        assertThat(counters).containsExactly(-1, 0);
        Utils.inc(counters, state);
        assertThat(counters).containsExactly(0, 1);
        Utils.inc(counters, state);
        assertThat(counters).containsExactly(0, -1);
        Utils.inc(counters, state);
        assertThat(counters).containsExactly(1, 0);
        Utils.inc(counters, state);
        Utils.inc(counters, state);
        assertThat(counters).containsExactly(2, 0);
        Utils.inc(counters, state);
        Utils.inc(counters, state);
        assertThat(counters).containsExactly(0, 1);
        Utils.inc(counters, state);
        Utils.inc(counters, state);
        assertThat(counters).containsExactly(1, 1);
        Utils.inc(counters, state);
        Utils.inc(counters, state);
        assertThat(counters).containsExactly(2, 1);


        log.info(Arrays.stream(counters).mapToObj(String::valueOf).collect(joining(", ")));

    }

    @Test
    public void stream() {
        Utils.stream(3).limit(100).forEach(i -> {
            log.info(() -> Arrays.stream(i).mapToObj(String::valueOf).collect(joining(", ")));
        });
    }
}
