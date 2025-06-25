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
package org.meeuw.time;

import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * An uncertain number can be used to represent objects from {@code java.time}, like {@link java.time.Instant}
 * or {@link java.time.Duration}. This requires some special treatment when formatting.
 * <p>
 * Used to be named {@code UncertainTemporal} until {@code 0.15}, but that didn't make sense, because it could also be
 * a {@link java.time.temporal.TemporalAmount}.
 * TODO Implement this too using {@link java.math.BigDecimal} to avoid overflows.
 * @since 0.9
 * @see UncertainDuration
 * @see UncertainInstant
 */
public interface UncertainJavaTime<N extends Number> extends UncertainNumber<N> {

    Mode getMode();


    /**
     * The value contained in a {@link UncertainNumber} can be interpreted in different ways when it
     */
    enum Mode {
        /**
         * Just a some long number.
         */
        LONG,

        /**
         * The long must be interpreted as a point in time. Milliseconds since EPOCH
         */
        INSTANT,

        /**
         * The long must be interpreted as duration. A number of milliseconds. This is probably precise enough for must cases.
         * For very short times, or more accuracy use {@link #DURATION_NS}, and durations will be stored as nanoseconds.
         */
        DURATION,


        /**
         * The long must be interpreted as duration. A number of nanos. Don't use this if durations vary more than a few seconds, since the sum of squares may rapidly go over {@link Long#MAX_VALUE} then.
         */
        DURATION_NS
    }




}
