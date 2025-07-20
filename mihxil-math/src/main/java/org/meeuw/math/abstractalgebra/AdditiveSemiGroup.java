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

import static org.meeuw.math.CollectionUtils.navigableSet;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.ADDITION;

/**
 * The algebraic structure that only defines addition. There might be no additive identity {@link AdditiveMonoid#zero()}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveSemiGroup<E extends AdditiveSemiGroupElement<E>> extends Magma<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(Magma.OPERATORS, ADDITION);

    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    /**
     * Whether {@link AdditiveSemiGroupElement#plus addition} is <em>commutative</em>.
     */
    default boolean additionIsCommutative() {
        return false;
    }

    @Override
    default boolean operationIsCommutative() {
        return additionIsCommutative();
    }
    @Override
    default boolean isCommutative(AlgebraicBinaryOperator operator) {
        if (operator.equals(ADDITION)) {
            return additionIsCommutative();
        }
        return AlgebraicStructure.defaultIsCommutative(operator, getSupportedOperators());
    }

}
