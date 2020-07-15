package org.meeuw.math.windowed;

import java.time.Duration;
import java.util.Arrays;
import java.util.LongSummaryStatistics;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

/**
 * {@link LongSummaryStatistics} can be aggregated, and therefor {@link Windowed}.
 * @see WindowedDoubleSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 1.66
 */
public class WindowedLongSummaryStatistics extends Windowed<LongSummaryStatistics> implements LongConsumer {

    @lombok.Builder
    protected WindowedLongSummaryStatistics(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        BiConsumer<Event, Windowed<LongSummaryStatistics>>[] eventListeners
        ) {
        super(window, bucketDuration, bucketCount, eventListeners);
    }

    @Override
    protected LongSummaryStatistics[] newBuckets(int bucketCount) {
        return new LongSummaryStatistics[bucketCount];
    }

    @Override
    protected LongSummaryStatistics initialValue() {
        return new LongSummaryStatistics();
    }

    @Override
    public void accept(long value) {
        currentBucket().accept(value);
    }

    public void accept(long... value) {
        LongSummaryStatistics currentBucket = currentBucket();
        Arrays.stream(value).forEach(currentBucket);
    }

    @Override
    public LongSummaryStatistics getWindowValue() {
        LongSummaryStatistics result = new LongSummaryStatistics();
        LongSummaryStatistics[] b = getBuckets();
        for (int i = b.length -1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }

    @Deprecated
    public LongSummaryStatistics getCombined() {
        return getWindowValue();
    }

}
