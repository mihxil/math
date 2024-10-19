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

import org.meeuw.math.abstractalgebra.AdditiveGroupElement;
import org.meeuw.math.abstractalgebra.AlgebraicStructure;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.AlgebraicElement.eqComparator;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupTheory<E extends AdditiveGroupElement<E>>
    extends AdditiveMonoidTheory<E> {

    @Property
    default void additiveGroupOperators(@ForAll(STRUCTURE) AlgebraicStructure<?> s) {
        assertThat(s.getSupportedOperators()).contains(BasicAlgebraicBinaryOperator.ADDITION, BasicAlgebraicBinaryOperator.SUBTRACTION);
    }

    @Property
    default void minus(
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2) {
        assertThat(v1.minus(v2))
            .usingComparator(eqComparator())
            .isEqualTo(v1.plus(v2.negation()));
    }


    @Property
    default void repeatedPlusZeroTimes(
            @ForAll(ELEMENTS) E v1
            ) {
        assertThat((v1.repeatedPlus(0)))
            .usingComparator(eqComparator())
            .isEqualTo(v1.getStructure().zero());
    }

    @Property
    default void repeatedPlusOneTimes(
            @ForAll(ELEMENTS) E v1
            ) {
        assertThat((v1.repeatedPlus(1))).isEqualTo(v1);
    }

    @Property
    default void repeatedPlusPositiveTimes(
            @ForAll(ELEMENTS) E v1
            ) {
        assertThat((v1.repeatedPlus(5)))
            .usingComparator(eqComparator())
            .isEqualTo(v1.repeatedPlus(3).plus(v1.repeatedPlus(2)));
    }

    @Property
    default void repeatedPlusNegativeTimes(
            @ForAll(ELEMENTS) E v1
            ) {
        assertThat((v1.repeatedPlus(-5)))
            .usingComparator(eqComparator())
            .isEqualTo(v1.repeatedPlus(5).negation());
    }

}
