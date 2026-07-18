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
import java.util.function.UnaryOperator;

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.exceptions.InvalidAlgebraicResult;

/**
 * The basic operations of arithmetic
 *
 * @author Michiel Meeuwissen
 * @since 0.20
 */
 public class SimpleAlgebraicUnaryOperator extends AbstractAlgebraicUnaryOperator implements AlgebraicUnaryOperator {

    @Getter
    final Method method;

    @Getter
    final UnaryOperator<CharSequence> stringify;

    @Getter
    final String symbol;


    final String name;

    public SimpleAlgebraicUnaryOperator(Method method, String symbol, String name) {
        this(method, symbol,  (a) -> symbol + a, name);
    }

    public SimpleAlgebraicUnaryOperator(Method method,  UnaryOperator<CharSequence> stringify, String name) {
        this(method, stringify.apply("x").toString(), stringify, name);
    }

    public SimpleAlgebraicUnaryOperator(Method method, String symbol, UnaryOperator<CharSequence> stringify, String name) {
        this.method = method;
        this.symbol = symbol;
        this.stringify = stringify;
        this.name = name;
    }

    public SimpleAlgebraicUnaryOperator(Method method, AlgebraicUnaryOperator from) {
        this.method = method;
        this.symbol = from.getSymbol();
        this.stringify = (a) -> from.stringify(a.toString());
        this.name = from.name();
    }


    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E apply(E element1) {
        try {
            E result = (E) method.invoke(element1);
            if (result == null) {
                throw new InvalidAlgebraicResult(  method + "(" + element1 + ") resulted null");
            }
            return result;
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }


    @Override
    public String stringify(String element1) {
        return stringify.apply(element1).toString();
    }

    @Override
    public String name() {
        return name;
    }

}
