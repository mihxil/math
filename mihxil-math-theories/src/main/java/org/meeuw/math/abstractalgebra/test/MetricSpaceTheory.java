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
package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.DoubleUtils;
import org.meeuw.math.abstractalgebra.MetricSpaceElement;
import org.meeuw.math.numbers.Scalar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MetricSpaceTheory<E extends MetricSpaceElement<E, S>, S extends Scalar<S>>
    extends SizeableTheory<E, S> {


    @Property
    default void distanceSymmetry(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        assertThat(a.distanceTo(b)).isEqualTo(b.distanceTo(a));
    }

    @Property
    default void identifyOfIndiscernibles(@ForAll(ELEMENTS) E a) {
        assertThat(a.distanceTo(a).isZero()).isTrue();
    }

    @Property
    default void triangleInequality(
        @ForAll(ELEMENTS) E a,
        @ForAll(ELEMENTS) E b,
        @ForAll(ELEMENTS) E c
        ) {
        double distance = a.distanceTo(c).doubleValue();
        assertThat(distance)
            .isLessThanOrEqualTo(a.distanceTo(b).doubleValue() + b.distanceTo(c).doubleValue() + DoubleUtils.uncertaintyForDouble(distance));
    }
}
