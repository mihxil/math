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

import static org.meeuw.math.CollectionUtils.navigableSet;

import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.AlgebraicUnaryOperator;

/**
 * Like a {@link Ring} but without multiplicative identity.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Rng<E extends RngElement<E>> extends AdditiveAbelianGroup<E>, MultiplicativeSemiGroup<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(AdditiveAbelianGroup.OPERATORS, MultiplicativeSemiGroup.OPERATORS);

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(AdditiveAbelianGroup.UNARY_OPERATORS, MultiplicativeSemiGroup.UNARY_OPERATORS, Group.UNARY_OPERATORS);


    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

}
