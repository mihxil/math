package org.meeuw.math;

import java.time.*;

/**
 * A clock that ticks uniquely. Every call to {@link #instant()} will be a time later than the previous call to it.
 * <p>
 * If no nanosecond has passed since the previous call, an extra nanosecond will be implicititely added.
 *
 * @author Michiel Meeuwissen
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
