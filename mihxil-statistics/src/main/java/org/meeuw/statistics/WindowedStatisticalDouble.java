package org.meeuw.statistics;

import java.time.Clock;
import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;

/**
 * {@link StatisticalDouble} can be aggregated, and therefore {@link Windowed}.
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
        BiConsumer<Event, Windowed<StatisticalDouble>>[] eventListeners,
        Clock clock
    ) {
        super(StatisticalDouble.class, window, bucketDuration, bucketCount, eventListeners, clock);
    }

    @Override
    protected StatisticalDouble initialValue() {
        return new StatisticalDouble();
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
