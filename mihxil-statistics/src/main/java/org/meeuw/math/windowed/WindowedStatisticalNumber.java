package org.meeuw.math.windowed;

import java.time.Clock;
import java.time.Duration;
import java.util.function.BiConsumer;

import org.meeuw.math.statistics.StatisticalNumber;

/**
 * {@link StatisticalNumber}s can be aggregated, and therefore {@link Windowed}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class WindowedStatisticalNumber<T extends StatisticalNumber<T>>
    extends Windowed<T>  {


    protected WindowedStatisticalNumber(
        Class<T> bucketClass,
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        BiConsumer<Event, Windowed<T>>[] eventListeners,
        Clock clock
    ) {
        super(bucketClass, window, bucketDuration, bucketCount, eventListeners, clock);
    }

    @Override
    public T getWindowValue() {
        T result = initialValue();
        T[] b = getRelevantBuckets();
        for (int i = b.length - 1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }


}
