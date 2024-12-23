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
package org.meeuw.math.temporal;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.statistics.StatisticalLong;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * An uncertain number can be used to represent objects from {@code java.time}, like {@link java.time.Instant}
 * or {@link java.time.Duration}. This requires some special treatment when formatting.
 * <p>
 * TODO Implement this too using {@link java.math.BigDecimal} to avoid overflows.
 * @since 0.9
 */
public interface UncertainTemporal<N extends Number> extends UncertainNumber<N> {

    Mode getMode();

    /**
     * The long value contained in a {@link StatisticalLong} can be interpreted in different ways.
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

    default  Duration durationValue() {
        return optionalDurationValue()
            .orElseThrow(() -> new DivisionByZeroException("no values entered", this + ".duration"));
    }

    Optional<Duration> optionalDurationValue();

    default  Instant instantValue() {
        return optionalInstantValue()
            .orElseThrow(() -> new DivisionByZeroException("no values entered", this + ".instant"));
    }

    Optional<Instant> optionalInstantValue();


}
