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
package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.AlgebraicUnaryOperator;

import static org.meeuw.math.CollectionUtils.navigableSet;

/**
 * <a href="https://en.wikipedia.org/wiki/Field_(mathematics)">Field</a>
 * <p>
 * For simplicity it is both a {@link AdditiveGroup} and a {@link MultiplicativeGroup}, which is not absolutely correct, because it contains one element {@link #zero()} that has no multiplicative {@link MultiplicativeGroupElement#reciprocal()}.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */

public interface Field<E extends FieldElement<E>> extends
    DivisionRing<E>,
    AbelianRing<E>,
    DivisibleGroup<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(DivisionRing.OPERATORS, AbelianRing.OPERATORS);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(DivisionRing.UNARY_OPERATORS, AbelianRing.UNARY_OPERATORS);


    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    // explicit to make proxying possible (DocumentationTest)
    @Override
    E one();


    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }


}
