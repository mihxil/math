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
package org.meeuw.math.statistics;

import java.util.Optional;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * A 'statistical' number, can receive a number of values, and can calculate the average (the value of this {@link Number} implementation) and standard deviation of those values.
 * <p>
 * The idea is that the '{@link #getUncertainty()}' will simply be determined heuristically, and be given by the {@link #getStandardDeviation()}
 *
 * @author Michiel Meeuwissen
 * @param <N> the type of the number
 */
public interface StatisticalNumber<SELF extends StatisticalNumber<SELF, N>, N extends Number> extends UncertainNumber<N> {

    N getStandardDeviation();

    @Override
    default N getUncertainty() {
        return getStandardDeviation();
    }
    Optional<N> getOptionalMean();

    default N getMean() {
        return getOptionalMean().orElseThrow(() -> new IllegalStateException("no values entered"));
    }

    @Override
    default N getValue() {
        return getMean();
    }

    int getCount();

    void combine(SELF  t);

    boolean eq(SELF o);

}



