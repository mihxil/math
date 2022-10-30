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
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.function.BiConsumer;
import java.util.function.DoubleConsumer;

/**
 * {@link DoubleSummaryStatistics} can be aggregated, and therefore {@link Windowed}.
 * <p>
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
        BiConsumer<Event, Windowed<DoubleSummaryStatistics>>[] eventListeners,
        Clock clock
        ) {
        super(DoubleSummaryStatistics.class, window, bucketDuration, bucketCount, eventListeners, clock);
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
        final DoubleSummaryStatistics result = new DoubleSummaryStatistics();
        final DoubleSummaryStatistics[] b = getBuckets();
        for (int i = b.length -1 ; i >= 0; i--) {
            result.combine(b[i]);
        }
        return result;
    }

}
