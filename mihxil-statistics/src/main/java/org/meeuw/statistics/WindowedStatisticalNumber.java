package org.meeuw.statistics;

import java.time.Duration;
import java.util.function.BiConsumer;

/**
 * {@link StatisticalNumber}s can be aggregated, and therefor {@link Windowed}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class WindowedStatisticalNumber<T extends StatisticalNumber<T>> extends Windowed<T>  {


    protected WindowedStatisticalNumber(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        BiConsumer<Event, Windowed<T>>[] eventListeners
    ) {
        super(window, bucketDuration, bucketCount, eventListeners);
        init();
    }

    @Override
    protected void _init() {

    }

    @Override
    public T getWindowValue() {
        T result = initialValue();
        T[] b = getRelevantBuckets();

        for (int i = b.length -1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }


}
