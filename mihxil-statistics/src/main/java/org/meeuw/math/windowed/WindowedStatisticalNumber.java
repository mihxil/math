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
import java.util.function.Supplier;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.math.statistics.StatisticalNumber;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

/**
 * {@link StatisticalNumber}s can be aggregated, and therefore be {@link Windowed}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class WindowedStatisticalNumber<N extends Number, T extends StatisticalNumber<T, N, RealNumber>>

    extends Windowed<T>
    implements Supplier<RealNumber> {


    protected WindowedStatisticalNumber(
        @NonNull Class<T> bucketClass,
        @Nullable Duration window,
        @Nullable Duration bucketDuration,
        @Nullable Integer bucketCount,
        @NonNull BiConsumer<Event, Windowed<T>>@Nullable[] eventListeners,
        @Nullable Clock clock
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

    @Override
    public RealNumber get() {
        return getWindowValue().immutableCopy();
    }


}
