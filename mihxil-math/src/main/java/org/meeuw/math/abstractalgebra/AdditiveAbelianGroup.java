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
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.SUBTRACTION;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.NEGATION;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveAbelianGroup<E extends AdditiveGroupElement<E>>
    extends AdditiveGroup<E>, AdditiveAbelianSemiGroup<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(AdditiveMonoid.OPERATORS, SUBTRACTION);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(AdditiveMonoid.UNARY_OPERATORS, NEGATION);

    @Override
    default boolean additionIsCommutative() {
        return true;
    }
}
