package org.meeuw.math;

import lombok.Getter;

import java.time.*;
import java.time.temporal.TemporalAmount;

/**
 * A {@link Clock}, which must be explicitly 'ticked'. This is of course mainly useful for testing
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
    public TestClock withZone(ZoneId zone) {
        return new TestClock(zone, instant);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    public LocalDateTime localDateTime() {
        return LocalDateTime.ofInstant(instant(), zone);
    }

    /**
     * Progresses the clock the given amount of time.
     * @param duration The amount of time to progress this clock
     */
    public void tick(TemporalAmount duration){
        instant = instant.plus(duration);
    }

    /**
     * Progresses the clock with the number of given millis.
     * @param millis The amount of milliseconds to progress this clock
     */
    public void tick(long millis) {
        instant = instant.plusMillis(millis);
    }

    /**
     * Progresses the clock with the number of given millis. (Drop in replacement for {@link Thread#sleep(long)})
     *
     * It's a kind of virtual sleep, because this will not actually take time.
     *
     * @param millis The amount of milliseconds to progress this clock
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
