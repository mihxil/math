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

import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.operators.*;

import static org.meeuw.math.CollectionUtils.navigableSet;

/**
 * A field with {@link ScalarFieldElement}s.
 *
 * Elements of such a fields can be converted to {@link Number}. Two basic operators are in place.
 *
 * <ul>
 * <li>{@link Scalar#bigDecimalValue()}: for which the symbol x₌ ('equal value',  {@link BasicFunction#DECIMAL}) is used, and
 * <li>{@link Scalar#bigIntegerValue()}: for which we use the symbol ⌊x⌉ ('rounding', {@link BasicFunction#INTEGER}
 * </ul>
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <E> The type of the elements of this field
 */
public interface ScalarField<E extends ScalarFieldElement<E>> extends Field<E> {

    NavigableSet<GenericFunction> FUNCTIONS = navigableSet(Field.FUNCTIONS, BasicFunction.ABS, BasicFunction.DECIMAL, BasicFunction.INTEGER);

    @Override
    default NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }

    @Override
    default NavigableSet<AlgebraicComparisonOperator> getSupportedComparisonOperators() {
        return BasicComparisonOperator.ALL;
    }

    @Override
    default boolean multiplicationIsCommutative() {
        return true;
    }

    /**
     * 𝜋, the ratio of the circumference of a circle to its diameter, approximately 3.14159.
     * This probably is an approximation, as the value of π is not known exactly for most field implementations.
     * @throws org.meeuw.math.exceptions.FieldIncompleteException if the field does not support this value, not even approximately.
     */
    E pi();



}
