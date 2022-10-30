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
package org.meeuw.math.abstractalgebra.integers;

import lombok.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.operators.AlgebraicUnaryOperator;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethod;


/**
 *  Integer types that can be positive can also define the factorial operator.
 */
public interface Factoriable<F extends MultiplicativeMonoidElement<F>>  {


    AlgebraicUnaryOperator FACT = new AlgebraicUnaryOperator() {

        final Method method = getDeclaredMethod(Factoriable.class, "factorial");
        @SuppressWarnings("unchecked")
        @SneakyThrows
        @Override
        public <E extends AlgebraicElement<E>> E apply(E e) {
            try {
                return (E) method.invoke(e);
            } catch (InvocationTargetException ita) {
                throw ita.getCause();
            }
        }

        @Override
        public String stringify(String element) {
            return element + "!";
        }

        @Override
        public String name() {
            return "factorial";
        }
    };

    F factorial();

    class Configuration implements ConfigurationAspect {
        @Getter
        @With
        private final Long maxArgument;

        public Configuration() {
            this(50000L);
        }


        @lombok.Builder
        private Configuration(Long maxArgument) {
            this.maxArgument = maxArgument;
        }

        @Override
        public List<Class<?>> associatedWith() {
            return Collections.singletonList(Factoriable.class);
        }
    }

}
