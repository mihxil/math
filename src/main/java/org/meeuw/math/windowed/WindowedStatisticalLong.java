package org.meeuw.math.windowed;

import java.time.Duration;
import java.time.Instant;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

import org.meeuw.math.StatisticalLong;
import org.meeuw.math.physics.UnitsImpl;

/**
 * {@link StatisticalLong} can be aggregated, and therefor {@link Windowed}.
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
        UnitsImpl units,
        BiConsumer<Event, Windowed<StatisticalLong>>[] eventListeners
    ) {
        super(window, bucketDuration, bucketCount, units, eventListeners);
        this.mode = mode == null ? StatisticalLong.Mode.LONG : mode;;
        init();
    }

    @Override
    protected void _init() {

    }

    @Override
    protected StatisticalLong[] newBuckets(int bucketCount) {
        return new StatisticalLong[bucketCount];
    }

    @Override
    protected StatisticalLong initialValue() {
        return new StatisticalLong(units, mode);
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
}
