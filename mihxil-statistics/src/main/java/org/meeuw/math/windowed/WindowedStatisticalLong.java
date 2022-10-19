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

import java.time.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

import org.meeuw.math.statistics.StatisticalLong;

/**
 * {@link StatisticalLong} can be aggregated, and therefore {@link Windowed}.
 *
 * @see WindowedLongSummaryStatistics
 * @author Michiel Meeuwissen
 * @since 1.66
 */
public class WindowedStatisticalLong extends WindowedStatisticalNumber<StatisticalLong> implements LongConsumer, AutoCloseable {

    private final StatisticalLong.Mode mode;

    private final AtomicInteger runningDurationIdentifier = new AtomicInteger(0);
    private final Map<Integer, RunningDuration> runningDurations = new ConcurrentHashMap<>();


    /**
     * Representation of a duration that is currently being measured.
     */
    public class RunningDuration implements AutoCloseable {
        final Integer id = runningDurationIdentifier.incrementAndGet();
        final Instant started = clock.instant();

        {
            runningDurations.put(id, this);
                }

        public void complete() {
            accept(currentValue());
            runningDurations.remove(id);
        }

        @Override
        public void close() {
            complete();
        }

        protected Duration currentValue(Instant now) {
            return Duration.between(started, now);
        }
        protected Duration currentValue() {
            return currentValue(clock.instant());
        }

    }

    @lombok.Builder
    protected WindowedStatisticalLong(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        StatisticalLong.Mode mode,
        BiConsumer<Event, Windowed<StatisticalLong>>[] eventListenersArray,
        Clock clock
    ) {
        super(StatisticalLong.class, window, bucketDuration, bucketCount, eventListenersArray, clock);
        this.mode = mode == null ? StatisticalLong.Mode.LONG : mode;
    }

    @Override
    protected StatisticalLong initialValue() {
        return new StatisticalLong(mode);
    }

    @Override
    public void accept(long value) {
        currentBucket().accept(value);
    }

    @Override
    public StatisticalLong getWindowValue() {
        StatisticalLong result = super.getWindowValue();
        for (RunningDuration runningDuration : runningDurations.values()) {
            result.enter(runningDuration.currentValue());
        }
        return result;
    }

    @Override
    public void close()  {
        runningDurations.clear();
    }

    public void accept(long... value) {
        StatisticalLong currentBucket = currentBucket();
        currentBucket.enter(value);
    }

    public void accept(Instant... instant) {
        StatisticalLong currentBucket = currentBucket();
        currentBucket.enter(instant);
    }

    public void accept(Duration... duration) {
        StatisticalLong currentBucket = currentBucket();
        currentBucket.enter(duration);
    }

    /**
     * Add a duration by measuring it. Call this before the interval, and call {@link RunningDuration#complete()} after it.
     * <p>
     * During this time the current duration is entered in {@link #getWindowValue()}
     * This is certainly an underestimate, but entering nothing at all may be even worse.
     */
    public RunningDuration measure() {
        if (mode != StatisticalLong.Mode.DURATION) {
            throw new IllegalStateException();
        }
        return new RunningDuration();
    }

    public Collection<RunningDuration> getRunningDurations() {
        if (mode != StatisticalLong.Mode.DURATION) {
            throw new IllegalStateException();
        }
        return runningDurations.values();
    }

    public static class Builder {
        @SafeVarargs
        public final Builder eventListeners(BiConsumer<Event, Windowed<StatisticalLong>>... eventListeners) {
            return eventListenersArray(eventListeners);
        }

    }
}
