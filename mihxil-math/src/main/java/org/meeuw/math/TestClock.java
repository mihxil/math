package org.meeuw.math;

import lombok.Getter;

import java.time.*;
import java.time.temporal.TemporalAmount;

/**
 * A {@link Clock}, which must be explicitely 'ticked'. This is of course mainly useful for testing
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class TestClock extends Clock {

    @Getter
    private final ZoneId zone;

    private Instant instant;

    public TestClock(ZoneId zone, Instant instant) {
        this.zone = zone;
        this.instant = instant;
    }

    public TestClock() {
        this(ZoneId.systemDefault(), Instant.now());
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new TestClock(zone, instant);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    /**
     * Progresses the clock the given amount of time.
     */
    public void tick(TemporalAmount duration){
        instant = instant.plus(duration);
    }

    /**
     * Progresses the clock with the number of given millis.
     */
    public void tick(long millis) {
        instant = instant.plusMillis(millis);
    }

    /**
     * Progresses the clock with the number of given millis. (Drop in replacement for {@link Thread#sleep(long)})
     */
    public void sleep(long millis) {
        tick(millis);
    }

    /**
     * Progresses the clock with exactly one second
     */
    public void tick() {
        tick(Duration.ofSeconds(1));
    }
}
