/*
 *  Copyright 2024 Michiel Meeuwissen
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

import java.lang.invoke.MethodHandle;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.lang.reflect.Method;
import java.util.function.BiFunction;

import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * The predefined  basic 'unary operators' of algebra's.
 * @author Michiel Meeuwissen
 * @since 0.15
 */
@Log
public  class AbstractAlgebraicIntOperator implements AlgebraicIntOperator {

    @Getter
    final MethodHandle method;

    final BiFunction<CharSequence, CharSequence, String> stringify;

    final String name;

    public AbstractAlgebraicIntOperator(String name, MethodHandle method, BiFunction<CharSequence, CharSequence, String> stringify) {
        this.name = name;
        this.method = method;
        this.stringify = stringify;
    }

    @Override
    @SneakyThrows
    public <E extends AlgebraicElement<E>> E apply(E e, int i) {
        return AlgebraicIntOperator.apply(this, method, e, i);
    }

    @Override
    public String stringify(String element, String n) {
        return stringify.apply(element, n);
    }

    @Override
    public String name() {
        return name;
    }
}
