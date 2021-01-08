package org.meeuw.statistics;

import lombok.Getter;

import java.time.*;

/**
 * a {@link Clock} (mainly for testing).
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class TestClock extends Clock {

    @Getter
    final ZoneId zone;

    Instant instant;

    public TestClock(ZoneId zone, Instant instant) {
        this.zone = zone;
        this.instant = instant;
    }

    public TestClock() {
        this(ZoneId.of("Europe/Amsterdam"), Instant.now());
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new TestClock(zone, instant);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    public void tick(Duration duration){
        instant = instant.plus(duration);
    }
    public void tick(long millis) {
        instant = instant.plusMillis(millis);
    }

    public void sleep(long millis) {
        tick(millis);
    }

    public void tick() {
        tick(Duration.ofSeconds(1));
    }
}
