/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.DoubleArbitrary;
import org.assertj.core.data.Percentage;

import org.meeuw.math.exceptions.NotComparableException;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.util.test.ElementTheory;

import static org.assertj.core.api.Assertions.assertThat;

public interface UncertainDoubleTheory<E extends UncertainDouble<E>>
    extends ElementTheory<E> {

    @Property
    default void timesDouble(
        @ForAll(ELEMENTS) E e,
        @ForAll("doubles") Double multiplier) {
        assertThat(e.times(multiplier).getValue()).isCloseTo(e.getValue() * multiplier, Percentage.withPercentage(0.001));
    }

    @Property
    default void plus(
        @ForAll(ELEMENTS) E e1,
        @ForAll(ELEMENTS) E e2) throws NotComparableException {

        E sum = e1.plus(e2);
        getLogger().info("{} + {} = {}", e1, e2, sum);
        assertThat(sum.getValue()).isEqualTo(e1.getValue() + e2.getValue());

    }

    @Property
    default void weightedAverage(
        @ForAll(ELEMENTS) E e1,
        @ForAll(ELEMENTS) E e2) throws NotComparableException {
        E combined = e1.weightedAverage(e2);
        getLogger().info("{} combined with {} = {} ({})", e1, e2, combined, combined.getValue());
        if (e1.getValue() < e2.getValue()) {
            assertThat(combined.getValue()).isBetween(e1.getValue(), e2.getValue());
        } else {
            assertThat(combined.getValue()).isBetween(e2.getValue(), e1.getValue());
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
