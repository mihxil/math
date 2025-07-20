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

import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.MULTIPLICATION;

/**
 * A commutative ring, next to {@link AdditiveSemiGroupElement#plus(AdditiveSemiGroupElement) addition}, also {@link MultiplicativeSemiGroupElement#times(MultiplicativeSemiGroupElement) multiplication} is commutative.
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface AbelianRing<E extends AbelianRingElement<E>>
    extends Ring<E>,
    MultiplicativeAbelianSemiGroup<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = Ring.OPERATORS;

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = Ring.UNARY_OPERATORS;


    @Override
    E one();

    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }
    @Override
    default boolean isCommutative(AlgebraicBinaryOperator operator) {
        if (operator.equals(MULTIPLICATION)) {
            return multiplicationIsCommutative();
        }
        return AlgebraicStructure.defaultIsCommutatative(operator, getSupportedOperators());
    }

}
