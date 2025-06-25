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

import java.time.Instant;
import java.time.temporal.*;

import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * A {@link Temporal} implementation that is like {@link Instant}, but is backed by a {@link UncertainNumber}. So it might
 * e.g. have a {@link Object#toString() toString} like {@code 2025-02-15T08:40 ± PT5M}
 * @since 0.15
 */
public interface UncertainInstant<N extends Number> extends UncertainNumber<N>, Temporal, TemporalAdjuster {

    /**
     *  @return The backing uncertain value for this instant. It
     * is for this interface still left open whether that is e.g. 'milliseconds since 1970', or something else.
     */
    @Override
    N getValue();
    Instant instantValue();

    @Override
    default boolean isSupported(TemporalUnit unit) {
        return instantValue().isSupported(unit);
    }

    @Override
    default Temporal with(TemporalField field, long newValue) {
        return instantValue().with(field, newValue);
    }

    @Override
    default long until(Temporal endExclusive, TemporalUnit unit) {
        return instantValue().until(endExclusive, unit);
    }

    UncertainDuration<N> until(Temporal endExclusive);

    @Override
    default boolean isSupported(TemporalField field) {
        return instantValue().isSupported(field);
    }

    @Override
    default long getLong(TemporalField field) {
        return instantValue().getLong(field);
    }

    @Override
    default Temporal adjustInto(Temporal temporal) {
        return instantValue().adjustInto(temporal);
    }


}
