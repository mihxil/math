/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
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
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

/**
 * {@link IntSummaryStatistics} can be aggregated, and therefore {@link Windowed}.
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
        BiConsumer<Event, Windowed<IntSummaryStatistics>>[] eventListeners,
        Clock clock
        ) {
        super(IntSummaryStatistics.class, window, bucketDuration, bucketCount, eventListeners, clock);
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

}
