package org.meeuw.statistics;

import java.time.Clock;
import java.time.Duration;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;

/**
 * {@link DoubleSummaryStatistics} can be aggregated, and therefore {@link Windowed}.
 *
 * Every 'bucket' of the window is one '{@link DoubleSummaryStatistics}, and the {@link #getWindowValue()} is just all bucket values
 * {@link DoubleSummaryStatistics#combine(DoubleSummaryStatistics)}d.
 * @author Michiel Meeuwissen
 * @since 2.2
 */
public class WindowedDoubleSummaryStatistics extends Windowed<DoubleSummaryStatistics> implements DoubleConsumer {

    @lombok.Builder
    protected WindowedDoubleSummaryStatistics(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        BiConsumer<Event, Windowed<DoubleSummaryStatistics>>[] eventListeners,
        Clock clock
        ) {
        super(DoubleSummaryStatistics.class, window, bucketDuration, bucketCount, eventListeners, clock);
    }

    @Override
    protected DoubleSummaryStatistics initialValue() {
        return new DoubleSummaryStatistics();
    }

    @Override
    public void accept(double value) {
        currentBucket().accept(value);
    }

    public void accept(double... value) {
        DoubleSummaryStatistics currentBucket = currentBucket();
        Arrays.stream(value).forEach(currentBucket);
    }

    @Override
    public DoubleSummaryStatistics getWindowValue() {
        DoubleSummaryStatistics result = new DoubleSummaryStatistics();
        DoubleSummaryStatistics[] b = getBuckets();
        for (int i = b.length -1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }

}
