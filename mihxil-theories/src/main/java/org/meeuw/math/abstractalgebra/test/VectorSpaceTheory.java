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

import java.util.concurrent.atomic.AtomicInteger;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.abstractalgebra.Vector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.test.WithScalarTheory.SCALARS;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface VectorSpaceTheory<
    V extends Vector<V, S>, S extends ScalarFieldElement<S>
    >
    extends AbelianRingTheory<V> {


    @Property
    default void dimension(@ForAll(ELEMENTS) V v1) {
        int dim = v1.getSpace().getDimension();
        assertThat(dim).isGreaterThan(0);

        for (int i = 0 ; i < dim; i++) {
            v1.get(i);
        }
        AtomicInteger count = new AtomicInteger(0);
        v1.forEach(e -> count.incrementAndGet());
        assertThat(count.intValue()).isEqualTo(dim);

        count.set(0);
        v1.spliterator().forEachRemaining(
            e -> count.incrementAndGet()
        );
        assertThat(count.intValue()).isEqualTo(dim);

        assertThatThrownBy(() ->
            v1.get(dim)
        ).isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Property
    default void vectorToString(@ForAll(ELEMENTS) V v1) {
        String toString = v1.toString();
        v1.forEach(e -> assertThat(toString).contains(e.toString()));
    }

    @Property
    default void space(@ForAll(ELEMENTS) V v1, @ForAll(ELEMENTS) V v2) {
        assertThat(v1.getSpace().getDimension()).isEqualTo(v2.getSpace().getDimension());
        assertThat(v1.getSpace().getField()).isEqualTo(v2.getSpace().getField());
        assertThat(v1.getSpace().zero()).isEqualTo(v2.getSpace().zero());
        assertThat(v1.getSpace().equals(v2.getSpace())).isTrue();
        assertThat(v1.getSpace()).isSameAs(v2.getSpace());
        assertThat(v1.getSpace().equals(new Object())).isFalse();
        //assertThat(v1.getSpace().toString()).contains(v1.getSpace().getField().toString());
    }

    @Property
    default void associativity(@ForAll(ELEMENTS) V v1, @ForAll(ELEMENTS) V v2, @ForAll(ELEMENTS) V v3) {
        withLooseEquals(() -> {

            assertThat(
                v1.plus(v2.plus(v3))
            ).isEqualTo(
                (v1.plus(v2)).plus(v3)
            );
        });
    }

    @Property
    default void commutativity(@ForAll(ELEMENTS) V v1, @ForAll(ELEMENTS) V v2) {
        assertThat(v1.plus(v2)).isEqualTo(v2.plus(v1));
    }

    @Property
    default void negation(@ForAll(ELEMENTS) V v1) {
        assertThat(v1.plus(v1.negation())).isEqualTo(v1.getSpace().zero());
    }

    @Property
    default void compatibility(@ForAll(ELEMENTS) V v, @ForAll(SCALARS) S a, @ForAll(SCALARS) S b) {
        withLooseEquals(() -> {
            assertThat(
                (v.times(a)).times(b)
            ).isEqualTo(
                v.times(a.times(b))
            );
        });
    }

    @Property
    default void scalarIdentity(@ForAll(ELEMENTS) V v) {
        assertThat(v.times(v.getSpace().getField().one())).isEqualTo(v);
    }

    @Property
    default void vectorDistributivity(@ForAll(ELEMENTS) V v1, @ForAll(ELEMENTS) V v2, @ForAll(SCALARS) S e) {
        withLooseEquals(() -> {

            assertThat(
                (v1.plus(v2)).times(e)
            ).isEqualTo(
                (v1.times(e)).plus(v2.times(e))
            );
        });
    }

    @Property
    default void scalarDistributivity(
        @ForAll(ELEMENTS) V v,
        @ForAll(SCALARS) S e1, @ForAll(SCALARS) S e2) {
        withLooseEquals(() -> {

            assertThat(
                v.times(e1.plus(e2))
            ).isEqualTo(
                (v.times(e1)).plus(v.times(e2))
            );
        });
    }

    @Property
    default void dotCommutative(
        @ForAll(ELEMENTS) V a,
        @ForAll(ELEMENTS) V b) {
        assertThat(a.dot(b)).isEqualTo(b.dot(a));
    }

    @Property
    default void dotDistributive(
        @ForAll(ELEMENTS) V a,
        @ForAll(ELEMENTS) V b,
        @ForAll(ELEMENTS) V c
        ) {
        withLooseEquals(() -> {
            assertThat(a.dot(b.plus(c)))
                .isEqualTo((a.dot(b)).plus(a.dot(c)));
        });
    }

    @Provide
    Arbitrary<? extends S> scalars();

}
