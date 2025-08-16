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

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.DoubleUtils;
import org.meeuw.math.abstractalgebra.MetricSpaceElement;
import org.meeuw.math.numbers.Scalar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;

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
    default void metric(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        assertThat(a.getStructure().apply(a, b)).isEqualTo(a.distanceTo(b));
    }



    @Property
    default void distancePositive(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        withLooseEquals(() -> {
            if (a.equals(b)) {
                assertThat(a.distanceTo(b).isZero())
                    .withFailMessage(() -> String.format("%s equals %s -> hence distance must be zero (but is %s)", a, b, a.distanceTo(b)))
                    .isTrue();
            } else {
                assertThat(a.distanceTo(b).isPositive())
                    .withFailMessage(() -> String.format("%s !equals %s -> hence distance must be positive (but is %s)", a, b, a.distanceTo(b)))
                    .isTrue();
            }
        });
    }

    @Property
    default void identityOfIndiscernibles(@ForAll(ELEMENTS) E a) {
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
