/*
 *  Copyright 2026 Michiel Meeuwissen
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
import java.util.concurrent.atomic.AtomicLong;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.time.Sleeper;

/**
 * A rate limiter based on a {@link WindowedEventRate}: at most {@link #getAllowedPerWindow()} events are permitted in any
 * {@link #getWindow() window} (a sliding window, which slides per {@link WindowedEventRate#getBucketDuration() bucket}).
 * <p>
 * Contrary to a token bucket implementation (like guava's {@code RateLimiter}) this does not smooth the rate. All permits of a window
 * may be consumed at once, and when a bucket expires, all events in it become available again. This makes it suitable to
 * implement quota like 'at most 60 calls per hour'. Finer grained buckets make the window slide more smoothly.
 * <p>
 * To avoid such bursts (e.g. after a quiet period), {@link #getMaxPerBucket() maxPerBucket} can be set. Then also at most that many
 * events are permitted per {@link WindowedEventRate#getBucketDuration() bucket}, which spreads the events more evenly over the window.
 * <p>
 * E.g.:
 * <pre>{@code
 *   WindowedRateLimiter limiter = WindowedRateLimiter.builder()
 *       .window(Duration.ofMinutes(1))
 *       .bucketCount(60)
 *       .allowedPerWindow(300)
 *       .build();
 *
 *   if (limiter.tryAcquire(Duration.ofSeconds(1))) {
 *       // do the call
 *   } else {
 *       // give up, retry after limiter.retryAfter()
 *   }
 * }</pre>
 * <p>
 * Changing the {@link #setAllowedPerWindow(long) allowed number of events} or the {@link #setWindow(Duration) window} keeps
 * track of the events that happened already.
 *
 * @author Michiel Meeuwissen
 * @since 0.23
 */
public class WindowedRateLimiter implements AutoCloseable {

    private final Clock clock;
    private final Sleeper sleeper;
    private final @Nullable Integer bucketCount;

    private volatile long allowedPerWindow;
    private volatile long maxPerBucket;
    private WindowedEventRate rate; // guarded by this

    /**
     * @param window           The window in which at most {@code allowedPerWindow} events are permitted
     * @param bucketCount      The number of buckets the window is divided in. Defaults to 20 (see {@link Windowed}).
     * @param allowedPerWindow The number of events permitted per window. Must be positive.
     * @param maxPerBucket     The number of events permitted per bucket. Zero or negative (the default) means no limit per bucket.
     * @param clock            Defaults to {@link Clock#systemUTC()}
     * @param sleeper          How to wait. Defaults to {@link Sleeper#THREAD}. A {@link org.meeuw.time.TestClock} can be used in tests.
     */
    @lombok.Builder
    private WindowedRateLimiter(
        @NonNull Duration window,
        @Nullable Integer bucketCount,
        long allowedPerWindow,
        long maxPerBucket,
        @Nullable Clock clock,
        @Nullable Sleeper sleeper) {
        checkAllowed(allowedPerWindow);
        this.maxPerBucket = maxPerBucket;
        this.clock = clock == null ? Clock.systemUTC() : clock;
        this.sleeper = sleeper == null ? Sleeper.THREAD : sleeper;
        this.bucketCount = bucketCount;
        this.allowedPerWindow = allowedPerWindow;
        this.rate = createRate(window);
    }

    private WindowedEventRate createRate(Duration window) {
        return WindowedEventRate.builder()
            .window(window)
            .bucketCount(bucketCount)
            .clock(clock)
            .build();
    }

    private static void checkAllowed(long allowedPerWindow) {
        if (allowedPerWindow <= 0) {
            throw new IllegalArgumentException("allowedPerWindow must be positive (" + allowedPerWindow + ")");
        }
    }

    /**
     * Acquires a permit if one is available right now.
     * @return whether the permit was acquired (and accounted for)
     */
    public synchronized boolean tryAcquire() {
        synchronized (rate) {
            if (rate.getTotalCount() >= allowedPerWindow) {
                return false;
            }
            final long max = maxPerBucket;
            if (max > 0 && rate.currentBucket().get() >= max) {
                return false;
            }
            rate.newEvent();
            return true;
        }
    }

    /**
     * Acquires a permit, waiting at most {@code maxWait} for it. If it is clear that no permit will become available in time,
     * this returns {@code false} immediately, without waiting.
     * @param maxWait the maximal duration to wait
     * @return whether the permit was acquired
     * @throws InterruptedException if interrupted while waiting
     */
    public boolean tryAcquire(@NonNull Duration maxWait) throws InterruptedException {
        final long deadline = clock.millis() + maxWait.toMillis();
        while (true) {
            final Duration wait;
            synchronized (this) {
                if (tryAcquire()) {
                    return true;
                }
                wait = retryAfter();
            }
            if (clock.millis() + wait.toMillis() > deadline) {
                return false;
            }
            sleeper.sleep(wait);
        }
    }

    /**
     * Acquires a permit, waiting as long as necessary.
     * @throws InterruptedException if interrupted while waiting
     */
    public void acquire() throws InterruptedException {
        while (true) {
            final Duration wait;
            synchronized (this) {
                if (tryAcquire()) {
                    return;
                }
                wait = retryAfter();
            }
            sleeper.sleep(wait);
        }
    }

    /**
     * @return The duration after which a permit will be available again (if nobody else takes it). {@link Duration#ZERO} if
     * a permit is available now.
     */
    public synchronized @NonNull Duration retryAfter() {
        synchronized (rate) {
            final AtomicLong[] buckets = rate.getBuckets(); // shifts, and orders the oldest first
            final long now = clock.millis();
            final long currentBucketEnd = rate.currentBucketTime + rate.bucketDuration + 1; // first moment the next bucket is current

            long availableAt = now;

            // the window
            long total = 0;
            for (AtomicLong bucket : buckets) {
                total += bucket.get();
            }
            final long toExpire = total - allowedPerWindow + 1;
            if (toExpire > 0) {
                long expiring = 0;
                for (int k = 0; k < buckets.length; k++) {
                    expiring += buckets[k].get();
                    if (expiring >= toExpire) {
                        // the k-th oldest bucket is discarded as soon as more than k + 1 bucket durations have passed since the start of the current bucket
                        availableAt = Math.max(availableAt, currentBucketEnd + k * rate.bucketDuration);
                        break;
                    }
                }
            }

            // the current bucket
            final long max = maxPerBucket;
            if (max > 0 && buckets[buckets.length - 1].get() >= max) {
                availableAt = Math.max(availableAt, currentBucketEnd);
            }
            return Duration.ofMillis(availableAt - now);
        }
    }

    /**
     * @return The number of events in the current window
     */
    public synchronized long getCount() {
        return rate.getTotalCount();
    }

    public long getAllowedPerWindow() {
        return allowedPerWindow;
    }

    /**
     * Changes the number of permitted events per window. The events already registered remain.
     * @param allowedPerWindow new value, must be positive
     */
    public void setAllowedPerWindow(long allowedPerWindow) {
        checkAllowed(allowedPerWindow);
        this.allowedPerWindow = allowedPerWindow;
    }

    /**
     * @return The maximal number of events per bucket. Zero means: no limit per bucket.
     */
    public long getMaxPerBucket() {
        return Math.max(0, maxPerBucket);
    }

    /**
     * Changes the maximal number of events per bucket. Note that the duration of a bucket is {@link #getWindow() window} divided by the number of buckets.
     * @param maxPerBucket new value. Zero or negative means: no limit per bucket.
     */
    public void setMaxPerBucket(long maxPerBucket) {
        this.maxPerBucket = maxPerBucket;
    }

    /**
     * @return the duration of one bucket
     */
    public synchronized @NonNull Duration getBucketDuration() {
        return rate.getBucketDuration();
    }

    public synchronized @NonNull Duration getWindow() {
        return rate.getTotalDuration();
    }

    /**
     * Changes the window. The events already registered in the current window are (conservatively) all accounted to the
     * current bucket of the new window, so they will all expire after the new window has passed.
     * @param window the new window
     */
    public synchronized void setWindow(@NonNull Duration window) {
        if (window.equals(rate.getTotalDuration())) {
            return;
        }
        final long count = rate.getTotalCount();
        final WindowedEventRate newRate = createRate(window);
        if (count > 0) {
            newRate.newEvents((int) Math.min(Integer.MAX_VALUE, count));
        }
        rate.close();
        rate = newRate;
    }

    /**
     * @return The underlying {@link WindowedEventRate}, e.g. for reporting the current rate. Events should not be added to it directly.
     */
    public synchronized @NonNull WindowedEventRate getWindowedEventRate() {
        return rate;
    }

    @Override
    public synchronized void close() {
        rate.close();
    }

    @Override
    public String toString() {
        final long max = getMaxPerBucket();
        return getCount() + "/" + getAllowedPerWindow() + " per " + getWindow() + (max > 0 ? " (max " + max + " per " + getBucketDuration() + ")" : "");
    }
}
