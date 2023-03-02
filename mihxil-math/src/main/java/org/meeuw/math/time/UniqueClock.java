package org.meeuw.math.time;

import java.time.*;
import lombok.Getter;

/**
 * A clock that ticks uniquely. Every call to {@link #instant()} will result an {@link Instant} at least 1 nanosecond after the previous call to it.
 * <p>
 * If the actual current time (according to the {@link #getBaseClock() base clock}) is not at least one nanosecond after the result of the previous call, one nanosecond will be added to this previous call result, and <em>that</em> will be returned. So this final result may actually be a bit in the future.
 * <p>
 * This also means that this clock is not fit to be called more often than a billion (10<sup>9</sup>) times per second or so. because then it would get substantially late. That would be <em>very</em> often though, and for more realistic call rates, it will just now and then be a nanosecond or two late.
 * @author Michiel Meeuwissen
 * @since 0.10
 */
public class UniqueClock extends Clock {

    /**
     * A {@link UniqueClock} based on {@link Clock#systemUTC()}
     * @since 0.11
     */
    public static UniqueClock systemUTC() {
        return new UniqueClock(Clock.systemUTC());
    }

    @Getter
    private final Clock baseClock;
    private Instant previousAnswer = Instant.EPOCH;

    /**
     * Creates a new {@link UniqueClock} based on another {@link Clock}
     *
     * @param baseClock The clock to base this clock on.
     */

    public UniqueClock(Clock baseClock) {
        this.baseClock = baseClock;
    }

    @Override
    public ZoneId getZone() {
        return baseClock.getZone();
    }

    @Override
    public UniqueClock withZone(ZoneId zone) {
        UniqueClock result = new UniqueClock(baseClock.withZone(zone));
        result.previousAnswer = previousAnswer;
        return result;
    }

    /**
     * The current time according to this {@link UniqueClock}. Normally just the same
     * as the instant returned by the wrapped base clock. Unless that would have been returned previously, in which an instant  1 (or more if needed) nanoseconds later will be produced (so, slightly in the future according to the base clock)
     */
    @Override
    public synchronized Instant instant() {
        Instant answer = baseClock.instant();
        if (! answer.isAfter(previousAnswer)) {
            answer = previousAnswer.plusNanos(1);
        }
        previousAnswer = answer;
        return answer;
    }
}
