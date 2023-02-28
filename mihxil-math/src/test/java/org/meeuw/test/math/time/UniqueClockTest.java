package org.meeuw.test.math.time;

import java.time.*;

import org.junit.jupiter.api.Test;

import org.meeuw.math.time.TestClock;
import org.meeuw.math.time.UniqueClock;

import static org.assertj.core.api.Assertions.assertThat;

class UniqueClockTest {

    @Test
    void systemUtc() {
        Instant i = UniqueClock.systemUTC().instant();

        assertThat(i).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    void instantsAlwaysAfter() {
        TestClock clock = new TestClock();
        UniqueClock uniqueClock = new UniqueClock(clock);
        Instant i1 = uniqueClock.instant();
        Instant i2 = uniqueClock.instant();
        assertThat(i2).isAfter(i1);
        clock.tick(Duration.ofNanos(1));
        Instant i3 = uniqueClock.instant();
        assertThat(i3).isAfter(i2);
        assertThat(i3).isAfter(clock.instant());
    }

    @Test
    void otherFields() {
        TestClock clock = new TestClock();
        UniqueClock uniqueClock = new UniqueClock(clock);
        Instant i1 = uniqueClock.instant();
        Instant i2 = uniqueClock.instant();

        assertThat(uniqueClock.getZone()).isEqualTo(clock.getZone());

        UniqueClock uniqueClock2 = uniqueClock.withZone(ZoneId.of("Europe/Amsterdam"));
        Instant i3 = uniqueClock2.instant();

        assertThat(uniqueClock2.getZone()).isEqualTo(ZoneId.of("Europe/Amsterdam"));

        assertThat(i3).isAfter(i2);
    }
}
