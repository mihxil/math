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

import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BinaryOperator;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.exceptions.InvalidAlgebraicResult;
import org.meeuw.math.exceptions.NoSuchOperatorException;

/**
 * The basic operations of arithmetic
 *
 * @author Michiel Meeuwissen
 * @since 0.19
 */
 public class SimpleAlgebraicBinaryOperator implements AlgebraicBinaryOperator {

    @Getter
    final Method method;


    @Getter
    final BinaryOperator<CharSequence> stringify;

    @Getter
    final String symbol;

    final int precedence;

    final String name;

    public SimpleAlgebraicBinaryOperator(Method method, String symbol, int precedence, String name) {
        this.method = method;
        this.symbol = symbol;
        this.stringify = (a, b) -> a + " " + symbol + " " + b;
        this.precedence = precedence;
        this.name = name;
    }

    public SimpleAlgebraicBinaryOperator(Method method, AlgebraicBinaryOperator from) {
        this.method = method;
        this.symbol = from.getSymbol();
        this.stringify = (a, b) -> from.stringify(a.toString(), b.toString());
        this.precedence = from.precedence();
        this.name = from.name();
    }


    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E apply(E element1, E element2) {
        try {
            if (!method.getParameterTypes()[0].isInstance(element1)) {
                throw new NoSuchOperatorException(element1.getClass().getSimpleName() + " " + element1 + " has no operator '" + method.getName() + "'");
            }
            E result = (E) method.invoke(element1, element2);
            if (result == null) {
                throw new InvalidAlgebraicResult("" + method + "(" + element1 + ',' + element2 + ") resulted null");
            }
            return result;
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }


    @Override
    public String stringify(String element1, String element2) {
        return stringify.apply(element1, element2).toString();
    }

    @Override
    public int precedence() {
        return precedence;
    }
    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }


}
