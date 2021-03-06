package org.meeuw.math.windowed;

import java.time.*;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

import org.meeuw.math.statistics.StatisticalLong;

/**
 * {@link StatisticalLong} can be aggregated, and therefore {@link Windowed}.
 *
 * @see WindowedLongSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 1.66
 */
public class WindowedStatisticalLong extends WindowedStatisticalNumber<StatisticalLong> implements LongConsumer {

    private final StatisticalLong.Mode mode;

    @lombok.Builder
    protected WindowedStatisticalLong(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        StatisticalLong.Mode mode,
        BiConsumer<Event, Windowed<StatisticalLong>>[] eventListenersArray,
        Clock clock
    ) {
        super(StatisticalLong.class, window, bucketDuration, bucketCount, eventListenersArray, clock);
        this.mode = mode == null ? StatisticalLong.Mode.LONG : mode;
    }

    @Override
    protected StatisticalLong initialValue() {
        return new StatisticalLong(mode);
    }

    @Override
    public void accept(long value) {
        currentBucket().accept(value);
    }

    public void accept(long... value) {
        StatisticalLong currentBucket = currentBucket();
        currentBucket.enter(value);
    }

    public void accept(Instant... instant) {
        StatisticalLong currentBucket = currentBucket();
        currentBucket.enter(instant);
    }

    public void accept(Duration... duration) {
        StatisticalLong currentBucket = currentBucket();
        currentBucket.enter(duration);
    }

    public static class Builder {
        @SafeVarargs
        public final Builder eventListeners(BiConsumer<Event, Windowed<StatisticalLong>>... eventListeners) {
            return eventListenersArray(eventListeners);
        }

    }
}
