package org.meeuw.math.windowed;

import java.time.Duration;
import java.time.Instant;
import java.util.LongSummaryStatistics;
import java.util.function.LongConsumer;

import org.meeuw.math.StatisticalLong;

/**
 * {@link LongSummaryStatistics} can be aggregated, and therefor {@link Windowed}.
 * @see WindowedDoubleSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 1.66
 */
public class WindowedStatisticalLong extends Windowed<StatisticalLong> implements LongConsumer {

    private final StatisticalLong.Mode mode;

    @lombok.Builder(builderClassName = "Builder")
    protected WindowedStatisticalLong(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        StatisticalLong.Mode mode) {
        super(window, bucketDuration, bucketCount);
        this.mode = mode == null ? StatisticalLong.Mode.LONG : mode;
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

    @Override
    public StatisticalLong getWindowValue() {
        StatisticalLong result = initialValue();
        StatisticalLong[] b = getBuckets();
        for (int i = b.length -1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }

    @Deprecated
    public StatisticalLong getCombined() {
        return getWindowValue();
    }

}
