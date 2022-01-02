package org.meeuw.test.math;

import java.time.*;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.meeuw.math.TestClock;

/**
 * @author Michiel Meeuwissen
 */
class TestClockTest {

    @Test
    void withZone() {

        TestClock clock = new TestClock(ZoneId.of("Europe/Amsterdam"), Instant.parse("2021-01-15T21:39:26Z"));
        clock = clock.withZone(ZoneId.of("UTC"));
        clock.tick();
        clock.sleep(2000);
        assertThat(clock.localDateTime()).isEqualTo(LocalDateTime.of(2021, 1, 15, 21, 39, 29));
        assertThat(clock.getZone().toString()).isEqualTo("UTC");

    }
}
