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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.meeuw.time.Sleeper;

/**
 * Maintains a {@link WindowedRateLimiter} per key (e.g. per user, or per customer), all with the same configuration.
 * Changes in the configuration are propagated to all existing limiters.
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

    private volatile Duration window;
    private volatile long allowedPerWindow;

    /**
     * See {@link WindowedRateLimiter#builder()} for the parameters.
     */
    @lombok.Builder
    private KeyedWindowedRateLimiter(
        @NonNull Duration window,
        @Nullable Integer bucketCount,
        long allowedPerWindow,
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

    @Override
    public void close() {
        limiters.values().forEach(WindowedRateLimiter::close);
        limiters.clear();
    }

    @Override
    public String toString() {
        return getCounts() + " (max " + allowedPerWindow + " per " + window + ")";
    }
}
