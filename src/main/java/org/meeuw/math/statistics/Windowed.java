package org.meeuw.math.statistics;

import lombok.extern.java.Log;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.BiConsumer;

import com.google.common.collect.Range;

/**
 * Some metrics can be aggregated per unit of time. A 'Windowed' instance targets to accommodate that.
 *
 * This window is divided up in a certain number of 'buckets', of which the oldest bucket expires every time after {@code <window duration>/<number of buckets>} and is discarded.
 *
 * The idea is that the values in the buckets can be used to calculate averages which are based on sufficiently long times, though sufficiently sensitive for changes. So you actually look at a window in time that slides gradually forward. A 'sliding window'.
 *
 * The most basic implementation is {@link WindowedEventRate} which simply maintains a value 'events per unit of time':
 *
 * e.g.:
 *<pre>
 * {@code
 *   WindowedEventRate rate = WindowedEventRate.builder()
 *             .bucketCount(5)
 *             .bucketDuration(Duration.ofSeconds(1))
 *             .build();
 *   ...
 *   rate.newEvent();
 *
 *   ..
 *   System.out.println("Current rate: " + rate.getRate(TimeUnit.SECONDS) + " #/s);
 * }
 *</pre>
 *
 * @author Michiel Meeuwissen
 * @since 1.66
 */
@Log
public abstract class Windowed<T> {

    protected final T[] buckets;
    protected final long bucketDuration; // ms
    protected final long totalDuration;  // ms
    protected final Instant start = Instant.now(); //.truncatedTo(ChronoUnit.SECONDS);

    private boolean warmingUp = true;
    protected long  currentBucketTime = start.toEpochMilli();
    protected int currentBucketIndex = 0;

    protected BiConsumer<Event, Windowed<T>> eventListeners;


    /**
     * @param window         The total time window for which events are going to be measured (or <code>null</code> if bucketDuration specified)
     * @param bucketDuration The duration of one bucket (or <code>null</code> if window specified).
     * @param bucketCount    The number of buckets the total window time is to be divided in.
     */
    protected Windowed(
        Duration window,
        Duration bucketDuration,
        Integer bucketCount,
        BiConsumer<Event, Windowed<T>>[] eventListeners
        ) {
        if (bucketCount == null) {
            if (window != null && bucketDuration != null) {
                bucketCount = (int) (window.toMillis() / bucketDuration.toMillis());

            }
        }
        int bucketCount1 = bucketCount == null ? 20 : bucketCount;
        buckets = newBuckets(bucketCount1);
        if (window == null && bucketDuration == null) {
            // if both unspecified, take a default window of 5 minutes
            window = Duration.ofMinutes(5);
        }
        if (window != null) {
            long tempTotalDuration = window.toMillis();
            this.bucketDuration = tempTotalDuration / bucketCount1;
            this.totalDuration = this.bucketDuration * bucketCount1;
            // if window _and_ bucket Duration are specified, then at this duration must accord with the calculated one
            if (bucketDuration != null && this.bucketDuration != bucketDuration.toMillis()) {
                throw new IllegalArgumentException("The specified bucked duration " + bucketDuration + " didn't equal the calculated one " + Duration.ofMillis(this.bucketDuration));

            }
        } else {
            assert bucketDuration != null;
            // cannot happen. If it would have been null, then window would _never_ have been null
            this.bucketDuration = bucketDuration.toMillis();
            this.totalDuration = this.bucketDuration * bucketCount1;
        }
        this.eventListeners = (e, w) -> {
            if (eventListeners != null) {
                for (BiConsumer<Event, Windowed<T>> el : eventListeners) {
                    el.accept(e, w);
                }
            }
        };
        _init();
    }

    protected void _init() {
        init();
    }

    protected void init() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = initialValue();
        }
    }


    /**
     * The total duration, or 'window' we are looking at.
     */
    public Duration getTotalDuration() {
        return Duration.ofMillis(totalDuration);
    }

    /**
     * The duration of one bucket
     */
    public Duration getBucketDuration() {
        return Duration.ofMillis(bucketDuration);
    }

    /**
     * The number of buckets this window is divided in
     */
    public int getBucketCount() {
        return buckets.length;
    }

    /**
     * At which instant the measurements started
     */
    public Instant getStart() {
        return start;
    }

    /**
     * We are still warming up, if since {@link #getStart()} not yet {@link #getTotalDuration()} has elapsed
     */
    public boolean isWarmingUp() {
        if (warmingUp) {
            warmingUp = Instant.now().isBefore(start.plus(getTotalDuration()));
        }
        return warmingUp;
    }

    /**
     * Returns the current buckets, ordered by time. This means that the first one is the oldest one, and the last one is
     * the newest (current) one.
     */
    public T[] getBuckets() {
        shiftBuckets();
        T[] result = newBuckets(buckets.length);
        for (int i = 0; i < buckets.length; i++) {
            result[buckets.length - 1 - i] = buckets[(currentBucketIndex - i + buckets.length) % buckets.length];
        }
        return result;
    }

    public T[] getRelevantBuckets() {
        if (! isWarmingUp()){
            return getBuckets();
        } else {
            return Arrays.copyOfRange(getBuckets(), 0, Math.min(buckets.length, (int) (Duration.between(start, Instant.now()).toMillis() / bucketDuration) + 1));
        }
    }

    /**
     * Returns the current buckets, as a map, where the keys are the period to which they apply.
     * @return SortedMap with the oldest buckets first.
     */
    public SortedMap<Range<Instant>, T> getRanges() {
        shiftBuckets();
        SortedMap<Range<Instant>, T> result = new TreeMap<>(Comparator.comparing(Range::lowerEndpoint));
        Instant end = Instant.ofEpochMilli(currentBucketTime)
            .plusMillis(bucketDuration);
        for (int i = 0; i < buckets.length; i++) {
            Instant begin = end.minusMillis(bucketDuration);
            result.put(Range.closedOpen(begin, end), buckets[(currentBucketIndex - i + buckets.length) % buckets.length]);
            end = begin;
        }
        return result;

    }


    abstract T[] newBuckets(int bucketCount);

    abstract T initialValue();

    /**
     * If values can be reset, this method can do it.
     * @param value to reset if possible
     * @return <code>true</code> if the value was reset. <code>false</code> otherwise and a new {@link #initialValue()} will be used
     */
    protected boolean resetValue(T value) {
        return false;
    }

    protected T currentBucket() {
        shiftBuckets();
        return buckets[currentBucketIndex];
    }

    protected void shiftBuckets() {
        long currentTime = System.currentTimeMillis();
        long afterBucketBegin = currentTime - currentBucketTime;
        int i = 0;
        while (afterBucketBegin > bucketDuration && (i++) < buckets.length) {
            eventListeners.accept(Event.SHIFT, this);
            currentBucketIndex++;
            currentBucketIndex %= buckets.length;
            if (currentBucketIndex == 0) {
                eventListeners.accept(Event.WINDOW_COMPLETED, this);
            }
            if (!resetValue(buckets[currentBucketIndex])) {
                buckets[currentBucketIndex] = initialValue();
            }
            afterBucketBegin -= bucketDuration;
            currentBucketTime += bucketDuration;
        }
    }

    /**
     * Returns the current duration of the complete window
     * If we are warming up, then this will be the time since we started.
     * Otherwise only the current bucket is 'warming up', and the
     * relevant duration will be less than the configured 'window', but more than
     * the given window minus the duration of one bucket.
     */
    public Duration getRelevantDuration() {
        shiftBuckets();
        if (isWarmingUp()) {
            return Duration.between(start, Instant.now());
        } else {
            return Duration.ofMillis(
                (buckets.length -1) * bucketDuration // archived buckets (all but one, the current bucket)
                    +
                    System.currentTimeMillis() - currentBucketTime // current bucket is not yet complete
            );
        }
    }

    public abstract T getWindowValue();

    @Override
    public String toString() {
        return getStart() + " - " + getStart().plus(getTotalDuration()) + " (" + getBucketCount() + " buckets) :"  + getWindowValue();
    }


    public enum Event {
        SHIFT,
        WINDOW_COMPLETED
    }

}