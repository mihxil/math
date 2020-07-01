package org.meeuw.math.windowed;

import java.time.Duration;
import java.time.Instant;
import java.util.LongSummaryStatistics;
import java.util.function.LongConsumer;

import org.meeuw.math.LongMeasurement;

/**
 * {@link LongSummaryStatistics} can be aggregated, and therefor {@link Windowed}.
 * @see WindowedDoubleSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 1.66
 */
public class WindowedLongMeasurement extends Windowed<LongMeasurement> implements LongConsumer {

    private final LongMeasurement.Mode mode;

    @lombok.Builder(builderClassName = "Builder")
    protected WindowedLongMeasurement(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        LongMeasurement.Mode mode) {
        super(window, bucketDuration, bucketCount);
        this.mode = mode == null ? LongMeasurement.Mode.LONG : mode;
        init();
    }

    @Override
    protected void _init() {

    }

    @Override
    protected LongMeasurement[] newBuckets(int bucketCount) {
        return new LongMeasurement[bucketCount];
    }

    @Override
    protected LongMeasurement initialValue() {
        return new LongMeasurement(mode);
    }

    @Override
    public void accept(long value) {
        currentBucket().accept(value);
    }

    public void accept(long... value) {
        LongMeasurement currentBucket = currentBucket();
        currentBucket.enter(value);
    }
    public void accept(Instant... instant) {
        LongMeasurement currentBucket = currentBucket();
        currentBucket.enter(instant);
    }

    @Override
    public LongMeasurement getWindowValue() {
        LongMeasurement result = initialValue();
        LongMeasurement[] b = getBuckets();
        for (int i = b.length -1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }

    @Deprecated
    public LongMeasurement getCombined() {
        return getWindowValue();
    }

}
