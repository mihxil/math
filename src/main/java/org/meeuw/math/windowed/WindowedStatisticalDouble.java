package org.meeuw.math.windowed;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;

import org.meeuw.math.*;
import org.meeuw.math.physics.UnitsImpl;

/**
 * {@link StatisticalDouble} can be aggregated, and therefor {@link Windowed}.
 * @see WindowedDoubleSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class WindowedStatisticalDouble extends WindowedStatisticalNumber<StatisticalDouble> implements DoubleConsumer {

    @lombok.Builder
    protected WindowedStatisticalDouble(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        UnitsImpl units,
        BiConsumer<Event, Windowed<StatisticalDouble>>[] eventListeners
    ) {
        super(window, bucketDuration, bucketCount, units, eventListeners);
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

}
