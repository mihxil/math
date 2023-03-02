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
package org.meeuw.math.time;

import lombok.Getter;

import java.time.*;
import java.time.temporal.TemporalAmount;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A {@link Clock}, which must be explicitly 'ticked'. This is of course mainly useful for testing
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class TestClock extends Clock {

    @Getter
    private final ZoneId zone;

    private Instant instant;

    /**
     * A test clock with given {@link ZoneId}, and initial {@link Instant value}
     */
    public TestClock(@NonNull ZoneId zone, @NonNull Instant instant) {
        this.zone = zone;
        this.instant = instant;
    }

    /**
     * A clock instantiated with {@link Instant#now()} and {@link ZoneId#systemDefault()}.
     */
    public TestClock() {
        this(ZoneId.systemDefault(), Instant.now());
    }

    /**
     * A test clock with fixed instant in 2020 (recent past)
     */
    public static TestClock twentyTwenty() {
        ZoneId id = ZoneId.of("Europe/Amsterdam");
        return new TestClock(id,
            LocalDateTime.of(2020, 2, 20, 20, 20).atZone(id).toInstant()
        );
    }

     /**
      * A test clock with fixed instant in 2200 (far future).
     */
    public static TestClock twentyTwo() {
        ZoneId id = ZoneId.of("Europe/Amsterdam");
        return new TestClock(id,
            LocalDateTime.of(2200, 2, 22, 22, 20).atZone(id).toInstant()
        );
    }

    @Override
    public TestClock withZone(ZoneId zone) {
        return new TestClock(zone, instant);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    /**
     * The current {@link LocalDateTime} for the current test clock.
     * @see LocalDateTime#now(Clock), which would actually be the same.
     */
    public LocalDateTime localDateTime() {
        return LocalDateTime.ofInstant(instant(), zone);
    }

    /**
     * Progresses the clock the given amount of time.
     * @param duration The amount of time to progress this clock
     */
    public void tick(TemporalAmount duration){
        instant = instant.plus(duration);
    }

    /**
     * Progresses the clock with the number of given millis.
     * @param millis The amount of milliseconds to progress this clock
     */
    public void tick(long millis) {
        instant = instant.plusMillis(millis);
    }

    /**
     * Progresses the clock with the number of given millis. (Drop in replacement for {@link Thread#sleep(long)})
     * <p>
     * It's a kind of virtual sleep, because this will not actually take time.
     *
     * @param millis The amount of milliseconds to progress this clock
     */
    public void sleep(long millis) {
        tick(millis);
    }

    /**
     * Progresses the clock with exactly one second
     */
    public void tick() {
        tick(Duration.ofSeconds(1));
    }
}
