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

/**
 * An element of a  {@link Magma}, a group where it is not defined whether the operation is addition, multiplication or even some
 * other one (like e.g. division)
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface MagmaElement<E extends MagmaElement<E>> extends
    AlgebraicElement<E> {

    @Override
    Magma<E> getStructure();

    /**
     * A generic algebraic operator {@link org.meeuw.math.operators.BasicAlgebraicBinaryOperator#OPERATION}, associating a third element with 2 others.
     * May in many cases be viewed as either {@link org.meeuw.math.operators.BasicAlgebraicBinaryOperator#MULTIPLICATION}
     * or {@link org.meeuw.math.operators.BasicAlgebraicBinaryOperator#ADDITION}
     */
    E operate(E operand);

}
