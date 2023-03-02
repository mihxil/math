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

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Utilities related to time. Currently, mainly related to <em>rounding</em> times.
 */
public final class TimeUtils {

    private TimeUtils() {}

    /**
     * Given a {@code Duration}, returns an 'order of magnitude' that it.
     * <p>
     * The first match of
     * <ul>
     *     <li>{@link ChronoUnit#DAYS} if the given duration is longer than 2 days</li>
     *     <li>{@link ChronoUnit#HOURS} if the given duration is longer than 2 hours</li>
     *     <li>{@link ChronoUnit#MINUTES} if the given duration is longer than 2 minutes</li>
     *     <li>{@link ChronoUnit#SECONDS} if the given duration is longer than 2 seconds</li>
     *     <li>{@link ChronoUnit#MILLIS} if the given duration is less than 2 seconds</li>
     * </ul>
     */
    public static ChronoUnit orderOfMagnitude(Duration stddev) {
        ChronoUnit order = ChronoUnit.DAYS;
        if (stddev.toDays() < 2) {
            order = ChronoUnit.HOURS;
            if (stddev.toHours() < 2) {
                order = ChronoUnit.MINUTES;
                if (stddev.toMinutes() < 2) {
                    order = ChronoUnit.SECONDS;
                    if (stddev.toMillis() < 2000) {
                        order = ChronoUnit.MILLIS;
                    }
                }
            }
        }
        return order;
    }

    /**
     * Round a duration.
     * @param duration The duration to round
     * @param order    The {@link ChronoUnit} to round to
     */
    public static Duration round(Duration duration, ChronoUnit order) {
        switch(order) {
            case DAYS:
                //return Duration.ofDays(Math.round(duration.getSeconds() / 86400f));
            case HOURS:
                return Duration.ofHours(Math.round(duration.getSeconds() / 3600f));
            case MINUTES:
                return Duration.ofMinutes(Math.round(duration.getSeconds() / 60f));
            case SECONDS:
                return Duration.ofSeconds(duration.toMillis() / 1000);
            case MILLIS:
                return Duration.ofMillis(duration.toMillis());
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Round an instant.
     * @param instant The instant to round
     * @param order    The {@link ChronoUnit} to round to
     */
    public static Instant round(Instant instant, ChronoUnit order) {
        ChronoUnit trunc = ChronoUnit.values()[
             Math.max(ChronoUnit.MILLIS.ordinal(), Math.min(ChronoUnit.DAYS.ordinal(), order.ordinal() - 1))
             ];
         if (trunc == ChronoUnit.HALF_DAYS) {
             trunc = ChronoUnit.HOURS;
         }
         return instant.truncatedTo(trunc);
    }
}
