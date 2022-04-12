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

import lombok.extern.java.Log;

import java.time.Clock;
import java.time.Duration;
import java.util.Arrays;
import java.util.LongSummaryStatistics;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

/**
 * {@link LongSummaryStatistics} can be aggregated, and therefore {@link Windowed}.
 * @see WindowedDoubleSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 1.66
 */
@Log
public class WindowedLongSummaryStatistics extends Windowed<LongSummaryStatistics> implements LongConsumer {

    @lombok.Builder
    protected WindowedLongSummaryStatistics(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        BiConsumer<Event, Windowed<LongSummaryStatistics>>[] eventListeners,
        Clock clock
        ) {
        super(LongSummaryStatistics.class, window, bucketDuration, bucketCount, eventListeners, clock);
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
        LongSummaryStatistics[] b = getRelevantBuckets();
        log.info(() -> Arrays.asList(b).toString());
        for (int i = b.length - 1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }

}
