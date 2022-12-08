package org.meeuw.math.time;

import java.time.*;

/**
 * A clock that ticks uniquely. Every call to {@link #instant()} will be a time after the previous call to it.
 * <p>
 * If no nanosecond has passed since the previous call, an extra nanosecond will be implicitly added.
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
