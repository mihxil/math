/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.windowed;

import java.time.Clock;
import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;

import org.meeuw.math.statistics.StatisticalDouble;
import org.meeuw.math.statistics.StatisticalDoubleImpl;

/**
 * {@link StatisticalDouble}s can be aggregated, and therefore {@link Windowed}.
 * @see WindowedDoubleSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class WindowedStatisticalDouble extends
    WindowedStatisticalNumber<Double, StatisticalDoubleImpl> implements
    DoubleConsumer {

    @lombok.Builder
    protected WindowedStatisticalDouble(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        BiConsumer<Event, Windowed<StatisticalDoubleImpl>>[] eventListenersArray,
        Clock clock
    ) {
        super(StatisticalDoubleImpl.class, window, bucketDuration, bucketCount, eventListenersArray, clock);
    }

    @Override
    protected StatisticalDoubleImpl initialValue() {
        return new StatisticalDoubleImpl();
    }

    @Override
    public void accept(double value) {
        currentBucket().accept(value);
    }

    public void accept(double... value) {
        StatisticalDoubleImpl currentBucket = currentBucket();
        for(double d : value) {
            currentBucket.accept(d);
        }
    }

    public static class Builder {
        @SafeVarargs
        public final WindowedStatisticalDouble.Builder eventListeners(BiConsumer<Event, Windowed<StatisticalDoubleImpl>>... eventListeners) {
            return eventListenersArray(eventListeners);
        }
    }

}
