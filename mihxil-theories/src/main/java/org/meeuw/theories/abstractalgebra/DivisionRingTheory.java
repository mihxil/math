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

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.AlgebraicElement.eqComparator;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.NEGATION;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.SQR;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisionRingTheory<E extends DivisionRingElement<E>> extends
    RingTheory<E>,
    MultiplicativeGroupTheory<E>,
    AdditiveGroupTheory<E>  {

    @Property
    default void fieldOperators(@ForAll(STRUCTURE) DivisionRing<E> s) {
        assertThat(s.getSupportedOperators()).contains(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION);
    }

    @Property
    default void operatorEnums(
        @ForAll(ELEMENTS) E e1,
        @ForAll(ELEMENTS) E e2) {
        assertThat(MULTIPLICATION.andThen(NEGATION).apply(e1, e2))
            .usingComparator(eqComparator())
            .isEqualTo(e1.times(e2).negation());
        assertThat(ADDITION.andThen(SQR.compose(NEGATION)).apply(e1, e2))
            .usingComparator(eqComparator())
            .isEqualTo((e1.plus(e2).negation()).sqr());
        assertThat(ADDITION.andThen(SQR.andThen(NEGATION)).apply(e1, e2))
            .usingComparator(eqComparator())
            .isEqualTo((e1.plus(e2).sqr()).negation());
    }

}
