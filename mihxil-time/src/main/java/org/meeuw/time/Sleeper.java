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
package org.meeuw.time;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Abstraction of {@link Thread#sleep(long)}. Code that needs to wait can accept a {@code Sleeper}, so that test cases can
 * replace it, e.g. with a {@link TestClock}, which just advances the clock, so that tests don't actually need to wait.
 *
 * @author Michiel Meeuwissen
 * @since 0.23
 */
@FunctionalInterface
public interface Sleeper {

    /**
     * Sleeps using {@link Thread#sleep(long)}. Zero or negative durations return immediately.
     */
    Sleeper THREAD = duration -> {
        if (duration.isNegative() || duration.isZero()) {
            return;
        }
        TimeUnit.MILLISECONDS.sleep(duration.toMillis());
    };

    /**
     * @param duration how long to sleep
     * @throws InterruptedException if interrupted while sleeping
     */
    void sleep(@NonNull Duration duration) throws InterruptedException;
}
