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
package org.meeuw.math.operators;

import java.lang.reflect.Method;
import java.util.Objects;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.text.TextUtils;

/**
 * Like {@link java.util.function.BinaryOperator BinaryOperator}, but the difference is that this is not itself generic, but only its {@link #apply(AlgebraicElement, AlgebraicElement) apply} method.
 * <p>
 * Besides that, it also requires that the {@link #stringify(String, String)} is implemented.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicBinaryOperator  extends OperatorInterface {

    /**
     * Applies this operator to the given arguments
     */
    <E extends AlgebraicElement<E>> E apply(E arg1, E arg2);

      /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default AlgebraicBinaryOperator andThen(final AlgebraicUnaryOperator after) {
        Objects.requireNonNull(after);
        return new AlgebraicBinaryOperator() {
            @Override
            public <E extends AlgebraicElement<E>> E apply(E arg1, E arg2) {
                return after.apply(AlgebraicBinaryOperator.this.apply(arg1, arg2));
            }

            @Override
            public String stringify(String element1, String element2) {
                return after.stringify(AlgebraicBinaryOperator.this.stringify(element1, element2));
            }

            @Override
            public String name() {
                return AlgebraicBinaryOperator.this.name() + " and then " + after.name();
            }
            @Override
            public int precedence() {
                return 0;
            }
        };
    }

    /**
     * Creates a string representation for this operator working on two (string versions) elements.
     * E.g. for the addition operator this would result {@code "element1 + element2"}
     */
    String stringify(String element1, String element2);

    default <E extends AlgebraicElement<E>> String stringify(E element1, E element2) {
        return stringify(element1.toString(), element2.toString());
    }

    /**
     * @since 0.19
     */
    default <E extends AlgebraicElement<E>> String stringifyEval(E element1, E element2) {
        E eval = apply(element1, element2);
        return stringify(element1.toString(), element2.toString()) + "=" + eval.toString();
    }
    default String getSymbol() {
        return stringify(TextUtils.PLACEHOLDER, TextUtils.PLACEHOLDER);
    }

    @Override
    default <E extends AlgebraicElement<E>> Method getMethodFor(E e) {
        try {
            return e.getClass().getMethod(getMethod().getName(), e.getStructure().getElementClass());
        } catch (NoSuchMethodException nsme) {
            return OperatorInterface.super.getMethodFor(e);
        }
    }

    /**
     * When using infix notation, the precedence of this binary operation
     * @since 0.19
     */
    int precedence();


}
