package org.meeuw.math.windowed;

import java.time.Duration;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;

/**
 * {@link DoubleSummaryStatistics} can be aggregated, and there for {@link Windowed}.
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
        BiConsumer<Event, Windowed<DoubleSummaryStatistics>>[] eventListeners
        ) {
        super(window, bucketDuration, bucketCount, eventListeners);
    }



    @Override
    protected DoubleSummaryStatistics[] newBuckets(int bucketCount) {
        return new DoubleSummaryStatistics[bucketCount];

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

    @Deprecated
    public DoubleSummaryStatistics getCombined() {
        return getWindowValue();
    }

}
