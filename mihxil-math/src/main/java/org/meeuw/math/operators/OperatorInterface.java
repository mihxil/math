/**
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
package org.meeuw.math.operators;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.abstractalgebra.AlgebraicElement;

public interface OperatorInterface {

    Comparator<OperatorInterface> COMPARATOR = Comparator
        .comparing(OperatorInterface::ordinal)
        .thenComparing(OperatorInterface::name);

    String name();

    default int ordinal() {
        return Integer.MAX_VALUE;
    }

   default Method getMethod() {
        throw new UnsupportedOperationException();
   }

    @SneakyThrows
    default <E extends AlgebraicElement<E>> boolean isAlgebraicFor(E e) {
        Method m = e.getClass().getMethod(getMethod().getName(), getMethod().getParameterTypes());
        return m.getAnnotation(NonAlgebraic.class) == null;
    }
}
