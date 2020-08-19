package org.meeuw.statistics;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * An implementation of {@link Windowed} with {@link AtomicLong} values.
 *
 * Keeps track of an event rate in a current window of a given duration
 * E.g. If you want to report a certain event rate average for the last 5 minutes.
 *
 * Every 'bucket' of the window is just counter, and the associated {@link #getWindowValue()} is just the sum.
 *
 * Logically this class also provides {@link #getRate(TimeUnit)}.
 *
 * @author Michiel Meeuwissen
 * @since 0.38
 */
public class WindowedEventRate extends Windowed<AtomicLong> implements IntConsumer {


    private static final ScheduledExecutorService backgroundExecutor = Executors.newScheduledThreadPool(5);

    /**
     * @param window         The total time window for which events are going to be measured (or <code>null</code> if bucketDuration specified)
     * @param bucketDuration The duration of one bucket (or <code>null</code> if window specified).
     * @param bucketCount    The number of buckets the total window time is to be divided in.
     */

    @lombok.Builder
    private WindowedEventRate(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        Consumer<WindowedEventRate> reporter,
        BiConsumer<Event, Windowed<AtomicLong>>[] eventListenersArray

        ) {
        super(AtomicLong.class, window, bucketDuration, bucketCount, eventListenersArray);
        if (reporter != null) {
            backgroundExecutor.scheduleAtFixedRate(
                () -> {
                    try {
                        reporter.accept(WindowedEventRate.this);
                    } catch (Throwable t) {
                        Logger.getLogger(WindowedEventRate.class.getName()).log(Level.WARNING, t.getMessage(), t);
                    }
            }, 0, this.bucketDuration, TimeUnit.MILLISECONDS);
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
            null, bucketCount, null, null);
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

     */
    public double getRate(TimeUnit unit) {
        long totalCount = getTotalCount();
        Duration relevantDuration = getRelevantDuration();
        return ((double) totalCount * TimeUnit.NANOSECONDS.convert(1, unit)) / relevantDuration.toNanos();
    }

    /**
     * See also {@link #getRate(TimeUnit)} and {@link #getRate()}
     */
    public double getRate(Duration perInterval) {
        long totalCount = getTotalCount();
        Duration relevantDuration = getRelevantDuration();
        return ((double) totalCount * perInterval.toNanos()) / relevantDuration.toNanos();
    }

    /**
     * The current rate as a number of events per SI unit of time (the second)
     * See also {@link #getRate(TimeUnit)}
     */
    public double getRate() {
        return getRate(TimeUnit.SECONDS);
    }

    public String toString() {
        return "" + getRate() + " /s" + (isWarmingUp() ? " (warming up)" : "");
    }


    public static class Builder {

        @SafeVarargs
        public final Builder eventListeners(BiConsumer<Event, Windowed<AtomicLong>>... eventListeners) {
            return eventListenersArray(eventListeners);
        }
    }

}
