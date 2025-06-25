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
package org.meeuw.math.statistics.time;

import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

import org.meeuw.math.statistics.NoValues;
import org.meeuw.math.statistics.StatisticalNumber;
import org.meeuw.time.UncertainInstant;


/**
 * A {@link UncertainInstant}, but the {@link #instantValue()} may throw {@link NoValues} if no values are entered.
 * @since 0.15
 *
 */
public interface StatisticalInstant<
    SELF extends StatisticalInstant<SELF, N, E>,
    N extends Number,
    E>
    extends
    UncertainInstant<N>, StatisticalNumber<SELF, N, E>, LongConsumer, IntConsumer, Temporal {

    @Override
    default Instant instantValue() {
        return optionalInstantValue()
            .orElseThrow(() -> new NoValues("no values entered", this + ".instant"));
    }

    Optional<Instant> optionalInstantValue();

}
