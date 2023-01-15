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

import java.util.*;

import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

import static org.meeuw.math.CollectionUtils.navigableSet;


/**
 * The most simple groupoid. Just defining one generic operation 'operate'.
 *
 * @see AdditiveSemiGroup        Where the operation is 'addition'
 * @see MultiplicativeSemiGroup  Where the operation is 'multiplication'
 * @since 0.8
 * @param <E> Type of the elements ({@link MagmaElement}) of this Magma..
 */
public interface Magma<E extends MagmaElement<E>> extends AlgebraicStructure<E> {

    NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(AlgebraicStructure.OPERATORS, BasicAlgebraicBinaryOperator.OPERATION);

    @Override
    default NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    default boolean operationIsCommutative() {
        return false;
    }

}
