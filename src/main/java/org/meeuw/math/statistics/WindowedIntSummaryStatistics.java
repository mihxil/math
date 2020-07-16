package org.meeuw.math.statistics;

import java.time.Duration;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

/**
 * {@link LongSummaryStatistics} can be aggregated, and therefor {@link Windowed}.
 * @see WindowedDoubleSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 1.66
 */
public class WindowedIntSummaryStatistics extends Windowed<IntSummaryStatistics> implements IntConsumer {


    @lombok.Builder
    protected WindowedIntSummaryStatistics(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        BiConsumer<Event, Windowed<IntSummaryStatistics>>[] eventListeners

        ) {
        super(window, bucketDuration, bucketCount, eventListeners);
    }

    @Override
    protected IntSummaryStatistics[] newBuckets(int bucketCount) {
        return new IntSummaryStatistics[bucketCount];
    }

    @Override
    protected IntSummaryStatistics initialValue() {
        return new IntSummaryStatistics();
    }

    @Override
    public void accept(int value) {
        currentBucket().accept(value);
    }

    public void accept(int... value) {
        IntSummaryStatistics currentBucket = currentBucket();
        Arrays.stream(value).forEach(currentBucket);
    }

    @Override
    public IntSummaryStatistics getWindowValue() {
        IntSummaryStatistics result = new IntSummaryStatistics();
        IntSummaryStatistics[] b = getBuckets();
        for (int i = b.length -1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }

    @Deprecated
    public IntSummaryStatistics getCombined() {
        return getWindowValue();
    }

}
