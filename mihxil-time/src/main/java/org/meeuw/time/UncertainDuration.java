/*
 *  Copyright 2025 Michiel Meeuwissen
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
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;

import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * An uncertain 'temporal amount'. Much like a {@link Duration}, but uncertain.
 *
 * @see #durationValue()
 * @since 0.15
 */
public interface UncertainDuration<N extends Number> extends UncertainNumber<N>, TemporalAmount {

    /**
     * Returns the value of this uncertain number as a {@link Duration}.
     */
    Duration durationValue();

    /**
     * Returns the value of this uncertain number as a {@link Duration}.
     */
    Duration durationUncertainty();

    /**
     *  @return The backing uncertain value for this duration. It
     * is for this interface still left open whether that is e.g. 'milliseconds' or 'nanoseconds', or something else.
     */
    @Override
    N getValue();

    @Override
    default long get(TemporalUnit unit) {
        return durationValue().get(unit);
    }

    @Override
    default List<TemporalUnit> getUnits() {
        return durationValue().getUnits();
    }

}
