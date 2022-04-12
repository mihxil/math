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
package org.meeuw.math.abstractalgebra;

import java.util.NavigableSet;

import org.meeuw.math.operators.*;

import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.DIVISION;

/**
 * A <a href="https://en.wikipedia.org/wiki/Group_(mathematics)">Group</a> with the binary operation 'multiplication'.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroup<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeMonoid<E>, Group<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(MultiplicativeMonoid.OPERATORS, DIVISION);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(
        MultiplicativeMonoid.UNARY_OPERATORS,
        navigableSet(BasicAlgebraicUnaryOperator.RECIPROCAL),
        Group.UNARY_OPERATORS
    );


    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    default E unity() {
        return one();
    }

}
