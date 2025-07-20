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

import org.meeuw.math.operators.*;

import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.MULTIPLICATION;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.SQR;

/**
 * The algebraic structure that only defines multiplication. There might be no multiplicatie identity {@link MultiplicativeMonoid#one()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroup<E extends MultiplicativeSemiGroupElement<E>>
    extends Magma<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(Magma.OPERATORS, MULTIPLICATION);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(Magma.UNARY_OPERATORS, SQR);

    NavigableSet<AlgebraicIntOperator> INT_OPERATORS = navigableSet(BasicAlgebraicIntOperator.POWER);


    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicIntOperator> getSupportedIntOperators() {
        return INT_OPERATORS;
    }


    /**
     * Whether {@link MultiplicativeSemiGroupElement#times(MultiplicativeSemiGroupElement) multiplication} is commutative.
     *
     * @see MultiplicativeAbelianGroup
     */
    default boolean multiplicationIsCommutative() {
        return false;
    }

    @Override
    default boolean operationIsCommutative() {
        return multiplicationIsCommutative();
    }

    @Override
    default boolean isCommutative(AlgebraicBinaryOperator operator) {
        if (operator.equals(MULTIPLICATION)) {
            return multiplicationIsCommutative();
        }
        return AlgebraicStructure.defaultIsCommutatative(operator, getSupportedOperators());
    }

}
