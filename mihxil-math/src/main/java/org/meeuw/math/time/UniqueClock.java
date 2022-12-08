package org.meeuw.math.time;

import java.time.*;

/**
 * A clock that ticks uniquely. Every call to {@link #instant()} will result an {@link Instant} at least 1 nanosecond after the previous call to it.
 * <p>
 * If no nanosecond has passed since the previous call, an extra nanosecond will be implicitly added, so {@link #instant()} may actually be bit in the future.
 *
 * @author Michiel Meeuwissen
 * @since 0.10
 */
public class UniqueClock extends Clock {

    private final Clock baseClock;
    private Instant previousAnswer = Instant.EPOCH;

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
