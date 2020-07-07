package org.meeuw.math.windowed;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;

import org.meeuw.math.*;

/**
 * {@link StatisticalDouble} can be aggregated, and therefor {@link Windowed}.
 * @see WindowedDoubleSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class WindowedStatisticalDouble extends Windowed<StatisticalDouble> implements DoubleConsumer {

    private final Units units;

    @lombok.Builder(builderClassName = "Builder")
    protected WindowedStatisticalDouble(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        StatisticalLong.Mode mode,
        Units units,
        BiConsumer<Event, Windowed<StatisticalDouble>>[] eventListeners
    ) {
        super(window, bucketDuration, bucketCount, eventListeners);

        this.units = units;
        init();
    }

    @Override
    protected void _init() {

    }

    @Override
    protected StatisticalDouble[] newBuckets(int bucketCount) {
        return new StatisticalDouble[bucketCount];
    }

    @Override
    protected StatisticalDouble initialValue() {
        return new StatisticalDouble(units);
    }

    @Override
    public void accept(double value) {
        currentBucket().accept(value);
    }

    public void accept(double... value) {
        StatisticalDouble currentBucket = currentBucket();
        currentBucket.enter(value);
    }


    @Override
    public StatisticalDouble getWindowValue() {
        StatisticalDouble result = initialValue();
        StatisticalDouble[] b = getBuckets();
        for (int i = b.length -1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }

    @Deprecated
    public StatisticalDouble getCombined() {
        return getWindowValue();
    }

}
