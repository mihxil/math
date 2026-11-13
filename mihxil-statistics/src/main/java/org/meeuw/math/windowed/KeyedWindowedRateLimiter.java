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

    private final @Nullable Integer bucketCount;
    private final @Nullable Clock clock;
    private final @Nullable Sleeper sleeper;

    private final KeyedWindowedRateLimiterMXBean mxBean = new MXBean();

    private volatile Duration window;
    private volatile long allowedPerWindow;

    /**
     * See {@link WindowedRateLimiter#builder()} for the parameters.
     */
    @lombok.Builder
    private KeyedWindowedRateLimiter(
        @NonNull Duration window,
        @Nullable Integer bucketCount,
        @Positive long allowedPerWindow,
        @Nullable Clock clock,
        @Nullable Sleeper sleeper) {
        if (allowedPerWindow <= 0) {
            throw new IllegalArgumentException("allowedPerWindow must be positive (" + allowedPerWindow + ")");
        }
        this.window = window;
        this.bucketCount = bucketCount;
        this.allowedPerWindow = allowedPerWindow;
        this.clock = clock;
        this.sleeper = sleeper;
    }

    /**
     * @return the limiter for the given key, which is created if it didn't exist yet
     */
    public @NonNull WindowedRateLimiter get(@NonNull K key) {
        return limiters.computeIfAbsent(key, k -> WindowedRateLimiter.builder()
            .window(window)
            .bucketCount(bucketCount)
            .allowedPerWindow(allowedPerWindow)
            .clock(clock)
            .sleeper(sleeper)
            .build()
        );
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
        return window;
    }

    /**
     * @see WindowedRateLimiter#setWindow(Duration)
     */
    public synchronized void setWindow(@NonNull Duration window) {
        this.window = window;
        limiters.values().forEach(l -> l.setWindow(window));
    }

    public long getAllowedPerWindow() {
        return allowedPerWindow;
    }

    /**
     * @see WindowedRateLimiter#setAllowedPerWindow(long)
     */
    public synchronized void setAllowedPerWindow(long allowedPerWindow) {
        if (allowedPerWindow <= 0) {
            throw new IllegalArgumentException("allowedPerWindow must be positive (" + allowedPerWindow + ")");
        }
        this.allowedPerWindow = allowedPerWindow;
        limiters.values().forEach(l -> l.setAllowedPerWindow(allowedPerWindow));
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
    }

    @Override
    public String toString() {
        return getCounts() + " (max " + allowedPerWindow + " per " + window + ")";
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
