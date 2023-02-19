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

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.uncertainnumbers.UncertainNumber;

/**
 * A 'statistical' number, can receive a number of values, and can calculate the average (the value of this {@link Number} implementation) and standard deviation of those values.
 * <p>
 * The idea is that the '{@link #getUncertainty()}' will simply be determined heuristically, and be given by the {@link #getStandardDeviation()}
 *
 * @author Michiel Meeuwissen
 * @param <N> the type of the number
 * @param <SELF> type of self
 * @param <E> the type of immutable copies of this. If this implements {@link org.meeuw.math.abstractalgebra.AlgebraicStructure}, then this is the same algebra
 */
public interface StatisticalNumber<SELF extends StatisticalNumber<SELF, N, E>, N extends Number, E>
    extends UncertainNumber<N> {

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

    /**
     * Loosely equals for statistical numbers.
     */
    default boolean eq(SELF o) {
        if (getCount() == 0) {
            return o.getCount() == 0;
        }
        return eq(o.immutableCopy());
    }

    /**
     * Loosely equals, coincides with {@link org.meeuw.math.abstractalgebra.AlgebraicElement#eq(AlgebraicElement)}
     */
    boolean eq(E o);
    /**
     * Return copy of this object as {@link E}
     */
    default E immutableCopy() {
        return immutableInstance(getValue(), getUncertainty());
    }

    E immutableInstance(@NonNull N value, @NonNull N uncertainty);


}



