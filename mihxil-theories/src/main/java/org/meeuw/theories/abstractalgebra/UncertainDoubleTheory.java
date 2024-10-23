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
package org.meeuw.theories.abstractalgebra;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.DoubleArbitrary;
import org.assertj.core.data.Percentage;

import org.meeuw.math.exceptions.NotCombinableException;
import org.meeuw.math.exceptions.NotComparableException;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.opentest4j.TestAbortedException;

import static org.meeuw.assertj.Assertions.assertThat;


public interface UncertainDoubleTheory<E extends UncertainDouble<E>>
    extends ElementTheory<E> {

    @Property
    default void timesDouble(
        @ForAll(ELEMENTS) E e,
        @ForAll("doubles") Double multiplier) {
        assertThat(e.times(multiplier).doubleValue()).isCloseTo(e.doubleValue() * multiplier, Percentage.withPercentage(0.001));
    }

    @Property
    default void plus(
        @ForAll(ELEMENTS) E e1,
        @ForAll(ELEMENTS) E e2)  {
        try {
            E sum = e1.plus(e2);
            getLogger().info("{} + {} = {}", e1, e2, sum);
            assertThat(sum.doubleValue()).isEqualTo(e1.doubleValue() + e2.doubleValue());

        } catch(NotComparableException notComparableException) {
            Assume.that(false);
        }
    }

    @Property
    default void weightedAverage(
        @ForAll(ELEMENTS) E e1,
        @ForAll(ELEMENTS) E e2)  {
        try {
            E combined = e1.weightedAverage(e2);
            getLogger().info("{} combined with {} = {} ({})", e1, e2, combined, combined.doubleValue());
            if (e1.doubleValue() < e2.doubleValue()) {
                assertThat(combined.doubleValue()).isBetween(e1.doubleValue() - e1.getOptionalUncertainty().orElse(0d), e2.doubleValue() + e2.getOptionalUncertainty().orElse(0));
            } else {
                assertThat(combined.doubleValue()).isBetween(
                    e2.doubleValue() - e2.getOptionalUncertainty().orElse(0), e1.doubleValue() + e1.getOptionalUncertainty().orElse(0)
                );
            }
        } catch(NotComparableException | NotCombinableException notComparableException) {
            throw new TestAbortedException(e1 + " and " + e2 + ":" + notComparableException.getMessage());
        }
    }

    @Provide
    default DoubleArbitrary doubles() {
        return Arbitraries
            .doubles()
            .lessThan(1e10)
            .greaterThan(-1e10);
    }

}
