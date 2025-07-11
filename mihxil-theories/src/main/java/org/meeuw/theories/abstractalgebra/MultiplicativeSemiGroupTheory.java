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

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.IllegalPowerException;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.AlgebraicElement.eqComparator;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupTheory<E extends MultiplicativeSemiGroupElement<E>>
    extends MagmaTheory<E> {

    @Property
    default void multiplicativeSemiGroupOperators(@ForAll(STRUCTURE) MultiplicativeSemiGroup<E> s) {
        assertThat(s.getSupportedOperators()).contains(BasicAlgebraicBinaryOperator.MULTIPLICATION);
    }

    @Property
    default void multiplicationCommutativity(@ForAll(STRUCTURE) MultiplicativeSemiGroup<E> s) {
        if (s.multiplicationIsCommutative()) {
            assertThat(s).isInstanceOf(MultiplicativeAbelianSemiGroup.class);
        }
    }

    @Property
    default void multiplicativeAssociativity (
        @ForAll(ELEMENTS) E v1,
        @ForAll(ELEMENTS) E v2,
        @ForAll(ELEMENTS) E v3
    ) {
        assertThat((v1.times(v2)).times(v3))
            .usingComparator(eqComparator())
            .isEqualTo(v1.times((v2.times(v3))));
    }

    @Property
    default void pow1(
        @ForAll(ELEMENTS) E v1
    )  {
        assertThat(v1.pow(1))
            .usingComparator(eqComparator())
            .isEqualTo(v1);
    }

    @Property
    default void pow2(
        @ForAll(ELEMENTS) E v1
    )  {
        assertThat(v1.pow(2))
            .usingComparator(eqComparator())
            .isEqualTo(v1.times(v1));
    }

    @Property
    default void pow3(
        @ForAll(ELEMENTS) E v1
    )  {
        assertThat(v1.pow(3))
            .usingComparator(eqComparator())
            .isEqualTo(v1.times(v1).times(v1));
    }


    @Property
    default void
    pow0(
        @ForAll(ELEMENTS) E v1
    )  {
        assertThatThrownBy(() -> v1.pow(0)).isInstanceOf(IllegalPowerException.class);
    }

    @Property
    @Label("powNegative1 semigroup")
    default void powNegative1(
        @ForAll(ELEMENTS) E v1
    )  {
        assertThatThrownBy(() -> v1.pow(-1)).isInstanceOf(IllegalPowerException.class);
    }

    @Property
    @Label("powNegative2 semigroup")
    default void powNegative2(
        @ForAll(ELEMENTS) E v1
    )  {
        assertThatThrownBy(() ->
            v1.pow(-2))
            .isInstanceOf(IllegalPowerException.class);
    }

    @Property
    @Label("powNegative3 semigroup")
    default void powNegative3(
        @ForAll(ELEMENTS) E v1
    )  {
        assertThatThrownBy(() -> v1.pow(-3)).isInstanceOf(IllegalPowerException.class);
    }

    @Property
    default void sqr(@ForAll(ELEMENTS) E v) {
        assertThat(v.sqr()).isNotNull()
            .usingComparator(eqComparator())
            .isEqualTo(v.times(v));
    }

}
