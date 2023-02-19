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

import jakarta.annotation.PreDestroy;

import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.WithUnits;
import org.meeuw.math.statistics.StatisticalLong;
import org.meeuw.math.uncertainnumbers.CompareConfiguration;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;


/**
 * An implementation of {@link Windowed} with {@link AtomicLong} values.
 * <p>
 * Keeps track of an event rate in a current window of a given duration
 * E.g. If you want to report a certain event rate average for the last 5 minutes.
 * <p>
 * Every 'bucket' of the window is just counter, and the associated {@link #getWindowValue()} is just the sum.
 * <p>
 * Logically this class also provides {@link #getRate(TimeUnit)}.
 *
 * @author Michiel Meeuwissen
 * @since 0.38
 */
public class WindowedEventRate extends Windowed<AtomicLong>
    implements
    AutoCloseable,
    IntConsumer,
    UncertainDouble<UncertainReal>, WithUnits {


    private static final ThreadGroup THREAD_GROUP = new ThreadGroup("mihxil-statistics");
    private static final ScheduledExecutorService backgroundExecutor = Executors
        .newScheduledThreadPool(5, new ThreadFactory() {
            int counter = 0;
            @Override
            public Thread newThread(Runnable r) {
                Thread t =  new Thread(THREAD_GROUP,  r, "WindowedEventRate-" + (counter ++));
                t.setDaemon(true);
                return t;
            }
        })
        ;

    private final ScheduledFuture<?> scheduledReporter;

    /**
     * @param window         The total time window for which events are going to be measured (or <code>null</code> if bucketDuration specified)
     * @param bucketDuration The duration of one bucket (or <code>null</code> if window specified).
     * @param bucketCount    The number of buckets the total window time is to be divided in.
     * @param reporter       A consumer that will be called every {@code bucketDuration}
     */

    @lombok.Builder
    private WindowedEventRate(
        @Nullable Duration window,
        @Nullable Duration bucketDuration,
        @Nullable Integer bucketCount,
        @Nullable Consumer<WindowedEventRate> reporter,
        @NonNull BiConsumer<Event, Windowed<AtomicLong>>@Nullable[] eventListenersArray,
        @Nullable Clock clock
        ) {
        super(AtomicLong.class, window, bucketDuration, bucketCount, eventListenersArray, clock);
        if (reporter != null) {
            scheduledReporter = backgroundExecutor.scheduleAtFixedRate(
                () -> {
                    try {
                        reporter.accept(WindowedEventRate.this);
                    } catch (Throwable t) {
                        Logger.getLogger(WindowedEventRate.class.getName()).log(Level.WARNING, t.getMessage(), t);
                    }
                }, 0, this.bucketDuration, TimeUnit.MILLISECONDS);
        } else {
            scheduledReporter = null;
        }
    }

    /**
     * @return rate in /s (See {@link #getRate()}
     */
    @Override
    public double doubleValue() {
        return getRate();
    }

    /**
     * @return the uncertainty in the rate in /s (See {@link #getRate()}
     */
    @Override
    public double doubleUncertainty() {
        shiftBuckets();
        StatisticalLong statisticalLong = new StatisticalLong();
        for (AtomicLong bucket : getRelevantBuckets()) {
            statisticalLong.enter(bucket.get());
        }
        return 1000 * statisticalLong.doubleUncertainty() * getRelevantBuckets().length
            / getTotalDuration().toMillis();
    }

    public UncertainReal getUncertainRate() {
        return immutableInstanceOfPrimitives(getRate(), doubleUncertainty());
    }

    @Override
    public String getUnitsAsString() {
        return "/s";
    }

    @Override
    public UncertainDoubleElement immutableInstanceOfPrimitives(double value, double uncertainty) {
        return new UncertainDoubleElement(value, uncertainty);
    }

    /**
     * If using a reporter, cancels the associated {@link ScheduledFuture}.
     */
    @Override
    @PreDestroy
    public void close() {
        if (scheduledReporter != null) {
            scheduledReporter.cancel(true);
        }
    }



    @Override
    protected AtomicLong initialValue() {
        return new AtomicLong(0L);
    }

    @Override
    protected boolean resetValue(AtomicLong value) {
        value.set(0);
        return true;
    }

    public WindowedEventRate(int unit, TimeUnit timeUnit, int bucketCount) {
        this(Duration.ofMillis(
            TimeUnit.MILLISECONDS.convert(unit, timeUnit) * bucketCount),
            null, bucketCount, null, null, null);
    }
    public WindowedEventRate(int unit, TimeUnit timeUnit) {
        this(unit, timeUnit, 100);
    }

    public WindowedEventRate(TimeUnit timeUnit) {
        this(1, timeUnit);
    }

    public void newEvent() {
        currentBucket().getAndIncrement();
    }

    /**
     * Registers a number of events at once.
     * @param count The number of events to register
     */
    public void newEvents(int count) {
        currentBucket().getAndAdd(count);
    }

    /**
     * Accepting an integer is equivalent to {@link #newEvents(int)}
     */
    @Override
    public void accept(int value) {
        newEvents(value);
    }

    public long getTotalCount() {
        shiftBuckets();
        long totalCount = 0;
        for (AtomicLong bucket : buckets) {
            totalCount += bucket.get();
        }
        return totalCount;
    }

    @Override
    public AtomicLong getWindowValue() {
        return new AtomicLong(getTotalCount());
    }

    /**
     * The current rate as a number of events per given unit of time.
     * See also {@link #getRate(Duration)} and {@link #getRate()}
     * @param unit The unit of time to express the rate in.
     * @return the rate as a double
     */
    public double getRate(TimeUnit unit) {
        long totalCount = getTotalCount();
        Duration relevantDuration = getRelevantDuration();
        return ((double) totalCount * TimeUnit.NANOSECONDS.convert(1, unit)) / relevantDuration.toNanos();
    }

    /**
     * See also {@link #getRate(TimeUnit)} and {@link #getRate()}
     * @return the rate as a double
     * @param perInterval The reciprocal unit of the rate to report e.g. 1 second or 1 minute.
     */
    public double getRate(final Duration perInterval) {
        long totalCount = getTotalCount();
        Duration relevantDuration = getRelevantDuration();
        return ((double) totalCount * perInterval.toNanos()) / relevantDuration.toNanos();
    }

    /**
     * @return the current rate as a number of events per SI unit of time (the second)
     * @see #getRate(TimeUnit)
     */
    public double getRate() {
        return getRate(TimeUnit.SECONDS);
    }

    @Override
    public boolean strictlyEquals(Object o) {
        if (!(o instanceof WindowedEventRate)) {
            return false;
        }
        WindowedEventRate other = (WindowedEventRate) o;
        return getRate() == other.getRate();
    }

    @Override
    public boolean equals(Object o) {
        if ( ConfigurationService.getConfigurationAspect(CompareConfiguration.class).isEqualsIsStrict()) {
            return strictlyEquals(o);
        } else {
            return eq((WindowedEventRate) o);
        }
    }


    public boolean eq(WindowedEventRate o) {
        return eq(o, 1);
    }

    @Override
    public String toString() {
        return "" + getUncertainRate() + " " + getUnitsAsString() + (isWarmingUp() ? " (warming up)" : "");
    }

    public static class Builder {

        @SafeVarargs
        public final Builder eventListeners(BiConsumer<Event, Windowed<AtomicLong>>... eventListeners) {
            return eventListenersArray(eventListeners);
        }
    }

    @PreDestroy
    public static void shutdown() {
        backgroundExecutor.shutdown();
    }

}
