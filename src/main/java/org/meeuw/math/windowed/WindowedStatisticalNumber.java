package org.meeuw.math.windowed;

import lombok.Getter;

import java.time.Duration;
import java.util.function.BiConsumer;

import org.meeuw.math.*;

/**
 * {@link StatisticalNumber}s can be aggregated, and therefor {@link Windowed}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class WindowedStatisticalNumber<T extends StatisticalNumber<T>> extends Windowed<T>  {

    @Getter
    protected final Units units;

    protected WindowedStatisticalNumber(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        Units units,
        BiConsumer<Event, Windowed<T>>[] eventListeners
    ) {
        super(window, bucketDuration, bucketCount, eventListeners);

        this.units = units;
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
