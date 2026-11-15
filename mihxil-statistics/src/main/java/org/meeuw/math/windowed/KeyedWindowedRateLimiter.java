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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.time.Sleeper;
import org.meeuw.time.TimeUtils;

/**
 * Maintains a {@link WindowedRateLimiter} per key (e.g. per user, or per customer), all with the same configuration.
 * Changes in the configuration are propagated to all existing limiters.
 * <p>
 * It can be managed via JMX, see {@link #getMXBean()}.
 *
 * @param <K> the type of the keys
 * @author Michiel Meeuwissen
 * @since 0.23
 */
public class KeyedWindowedRateLimiter<K> implements AutoCloseable {

    private final Map<K, WindowedRateLimiter> limiters = new ConcurrentHashMap<>();

    /**
     * Holds the configuration. It is never acquired from itself, but copied for every new key.
     */
    private final WindowedRateLimiter template;

    private final KeyedWindowedRateLimiterMXBean mxBean = new MXBean();

    /**
     * See {@link WindowedRateLimiter#builder()} for the parameters.
     */
    @lombok.Builder
    private KeyedWindowedRateLimiter(
        @Nullable Duration window,
        @Nullable Duration bucketDuration,
        @Nullable Integer bucketCount,
        @Positive long allowedPerWindow,
        long maxPerBucket,
        @Nullable Clock clock,
        @Nullable Sleeper sleeper) {
        this.template = WindowedRateLimiter.builder()
            .window(window)
            .bucketDuration(bucketDuration)
            .bucketCount(bucketCount)
            .allowedPerWindow(allowedPerWindow)
            .maxPerBucket(maxPerBucket)
            .clock(clock)
            .sleeper(sleeper)
            .build();
    }

    /**
     * @return the limiter for the given key, which is created if it didn't exist yet
     */
    public @NonNull WindowedRateLimiter get(@NonNull K key) {
        return limiters.computeIfAbsent(key, k -> template.copy());
    }

    /**
     * @see WindowedRateLimiter#tryAcquire()
     */
    public boolean tryAcquire(@NonNull K key) {
        return get(key).tryAcquire();
    }

    /**
     * @see WindowedRateLimiter#tryAcquire(Duration)
     */
    public boolean tryAcquire(@NonNull K key, @NonNull Duration maxWait) throws InterruptedException {
        return get(key).tryAcquire(maxWait);
    }

    /**
     * @see WindowedRateLimiter#acquire()
     */
    public void acquire(@NonNull K key) throws InterruptedException {
        get(key).acquire();
    }

    /**
     * @return an unmodifiable view on the existing limiters
     */
    public @NonNull Map<K, WindowedRateLimiter> getLimiters() {
        return Collections.unmodifiableMap(limiters);
    }

    /**
     * @return per key the number of events in the current window
     */
    public @NonNull Map<K, Long> getCounts() {
        final Map<K, Long> result = new HashMap<>();
        limiters.forEach((k, l) -> result.put(k, l.getCount()));
        return result;
    }

    public @NonNull Duration getWindow() {
        return template.getWindow();
    }

    /**
     * @see WindowedRateLimiter#setWindow(Duration)
     */
    public synchronized void setWindow(@NonNull Duration window) {
        template.setWindow(window);
        limiters.values().forEach(l -> l.setWindow(window));
    }

    public long getAllowedPerWindow() {
        return template.getAllowedPerWindow();
    }

    /**
     * @see WindowedRateLimiter#setAllowedPerWindow(long)
     */
    public synchronized void setAllowedPerWindow(long allowedPerWindow) {
        template.setAllowedPerWindow(allowedPerWindow);
        limiters.values().forEach(l -> l.setAllowedPerWindow(allowedPerWindow));
    }

    public long getMaxPerBucket() {
        return template.getMaxPerBucket();
    }

    /**
     * @see WindowedRateLimiter#setMaxPerBucket(long)
     */
    public synchronized void setMaxPerBucket(long maxPerBucket) {
        template.setMaxPerBucket(maxPerBucket);
        limiters.values().forEach(l -> l.setMaxPerBucket(maxPerBucket));
    }

    /**
     * Forgets about all keys and their events.
     */
    public void clear() {
        final List<WindowedRateLimiter> removed = new ArrayList<>(limiters.values());
        limiters.clear();
        removed.forEach(WindowedRateLimiter::close);
    }

    /**
     * @return An object that can be registered as an MXBean to inspect and configure this limiter via JMX. Always the same instance.
     */
    public @NonNull KeyedWindowedRateLimiterMXBean getMXBean() {
        return mxBean;
    }

    /**
     * Equivalent to {@link #clear()}.
     */
    @Override
    public void close() {
        clear();
        template.close();
    }

    @Override
    public String toString() {
        return getCounts() + " (max " + getAllowedPerWindow() + " per " + getWindow() + ")";
    }

    private class MXBean implements KeyedWindowedRateLimiterMXBean {

        @Override
        public String getWindow() {
            return KeyedWindowedRateLimiter.this.getWindow().toString();
        }

        @Override
        public void setWindow(String window) {
            KeyedWindowedRateLimiter.this.setWindow(TimeUtils.parseDurationOrThrow(window));
        }

        @Override
        public long getAllowedPerWindow() {
            return KeyedWindowedRateLimiter.this.getAllowedPerWindow();
        }

        @Override
        public void setAllowedPerWindow(long allowedPerWindow) {
            KeyedWindowedRateLimiter.this.setAllowedPerWindow(allowedPerWindow);
        }

        @Override
        public long getMaxPerBucket() {
            return KeyedWindowedRateLimiter.this.getMaxPerBucket();
        }

        @Override
        public void setMaxPerBucket(long maxPerBucket) {
            KeyedWindowedRateLimiter.this.setMaxPerBucket(maxPerBucket);
        }

        @Override
        public Map<String, Long> getCounts() {
            final Map<String, Long> result = new TreeMap<>();
            limiters.forEach((k, l) -> result.put(String.valueOf(k), l.getCount()));
            return result;
        }

        @Override
        public void clear() {
            KeyedWindowedRateLimiter.this.clear();
        }
    }
}
