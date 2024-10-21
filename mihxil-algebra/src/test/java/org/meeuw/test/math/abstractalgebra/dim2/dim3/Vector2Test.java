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
package org.meeuw.test.math.abstractalgebra.dim2.dim3;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.dim2.Vector2;
import org.meeuw.math.abstractalgebra.dim3.Matrix3Group;
import org.meeuw.math.abstractalgebra.dim3.Vector3;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.theories.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.dim3.Vector3.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class Vector2Test implements
    ElementTheory<Vector2>,
    VectorSpaceTheory<Vector2, RealNumber>,
    WithScalarTheory<Vector2, RealNumber> {

    @Override
    public Arbitrary<? extends Vector2> elements() {
        return Arbitraries.randomValue(r ->
            Vector2.of(r.nextDouble(), r.nextDouble())
        )
            .injectDuplicates(0.1)
            .dontShrink()
            .edgeCases(config -> {
                config.add(Vector2.of(0, 0));
                config.add(Vector2.of(0, 1));
            });
    }

    @Override
    public Arbitrary<RealNumber> scalars() {
        return Arbitraries.randomValue(random ->
            RealNumber.of(random.nextDouble())
        ).dontShrink()
            .edgeCases(c  -> {
            c.add(RealNumber.ONE);
            c.add(RealNumber.ZERO);
        });
    }

    @Test
    void timesMatrix() {
        Vector3 example = of(1, 2, 3);
        Assertions.assertThat(example.times(Matrix3Group.INSTANCE.one())).isEqualTo(example);
        assertThat(example.times(Matrix3Group.INSTANCE.one().times(2))).isEqualTo(example.times(2));
    }

    @Test
    void timesDouble() {
        Vector3 example = of(1, 2, 3);
        assertThat(example.times(2)).isEqualTo(of(2, 4, 6));
    }

    @Test
    void dividedByDouble() {
        Vector3 example = of(1, 2, 3);
        assertThat(example.dividedBy(2)).isEqualTo(of(0.5, 1, 1.5));
    }

    @SuppressWarnings({"EqualsWithItself", "EqualsBetweenInconvertibleTypes"})
    @Test
    public void stringEqualsHashCode() {
        Vector3 example = of(1, 2, 3);
        assertThat(example.toString()).isEqualTo("(1, 2, 3)");

        assertThat(example.equals(example)).isTrue();
        assertThat(example.equals("bla")).isFalse();
        assertThat(example.equals(of(3, 2, 1))).isFalse();
        assertThat(example.equals(of(1, 2, 4))).isFalse();
        assertThat(example.equals(of(2, 2, 3))).isFalse();
        assertThat(example.equals(of(1, 2, 3))).isTrue();

        assertThat(example.hashCode()).isEqualTo(of(1, 2, 3).hashCode());
    }
}
