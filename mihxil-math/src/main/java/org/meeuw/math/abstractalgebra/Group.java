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

import org.meeuw.math.abstractalgebra.categoryofgroups.Element;
import org.meeuw.math.operators.AlgebraicUnaryOperator;
import org.meeuw.math.operators.BasicAlgebraicUnaryOperator;

import static org.meeuw.math.CollectionUtils.navigableSet;

/**
 * A general group , with one operation, and a 'unity' element, for this operation.
 *
 * @see MultiplicativeGroup For a group where the operation is explicitely called 'multiplication'
 * @see AdditiveGroup       For a group where the operation is 'addition'.
 * @since 0.8
 */
public interface Group<E extends GroupElement<E>> extends Magma<E>, Element {

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(Magma.UNARY_OPERATORS, BasicAlgebraicUnaryOperator.INVERSION);

    @Override
    default NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    /**
     * @return The unity element, for which the {@link GroupElement#operate(MagmaElement)} returns just the other value.
     */
    E unity();



}
