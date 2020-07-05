package org.meeuw.math.windowed;

import java.time.Duration;
import java.time.Instant;
import java.util.LongSummaryStatistics;
import java.util.function.LongConsumer;

import org.meeuw.math.LongStatisticalMeasurement;

/**
 * {@link LongSummaryStatistics} can be aggregated, and therefor {@link Windowed}.
 * @see WindowedDoubleSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 1.66
 */
public class WindowedLongStatisticalMeasurement extends Windowed<LongStatisticalMeasurement> implements LongConsumer {

    private final LongStatisticalMeasurement.Mode mode;

    @lombok.Builder(builderClassName = "Builder")
    protected WindowedLongStatisticalMeasurement(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        LongStatisticalMeasurement.Mode mode) {
        super(window, bucketDuration, bucketCount);
        this.mode = mode == null ? LongStatisticalMeasurement.Mode.LONG : mode;
        init();
    }

    @Override
    protected void _init() {

    }

    @Override
    protected LongStatisticalMeasurement[] newBuckets(int bucketCount) {
        return new LongStatisticalMeasurement[bucketCount];
    }

    @Override
    protected LongStatisticalMeasurement initialValue() {
        return new LongStatisticalMeasurement(mode);
    }

    @Override
    public void accept(long value) {
        currentBucket().accept(value);
    }

    public void accept(long... value) {
        LongStatisticalMeasurement currentBucket = currentBucket();
        currentBucket.enter(value);
    }
    public void accept(Instant... instant) {
        LongStatisticalMeasurement currentBucket = currentBucket();
        currentBucket.enter(instant);
    }

    @Override
    public LongStatisticalMeasurement getWindowValue() {
        LongStatisticalMeasurement result = initialValue();
        LongStatisticalMeasurement[] b = getBuckets();
        for (int i = b.length -1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }

    @Deprecated
    public LongStatisticalMeasurement getCombined() {
        return getWindowValue();
    }

}
