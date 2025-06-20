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
import java.util.*;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.operators.AlgebraicUnaryOperator;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethod;
import static org.meeuw.math.CollectionUtils.navigableSet;


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

    AlgebraicUnaryOperator SUB_FACTORIAL = new AlgebraicUnaryOperator() {

        final Method method = getDeclaredMethod(Factoriable.class, "subfactorial");
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
            return "!" + element;
        }

        @Override
        public String name() {
            return "subfactorial";
        }
    }
    ;

    AlgebraicUnaryOperator DOUBLE_FACTORIAL = new AlgebraicUnaryOperator() {

        final Method method = getDeclaredMethod(Factoriable.class, "doubleFactorial");
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
            return element + "!!";
        }

        @Override
        public String name() {
            return "double factorial";
        }
    }
    ;

    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(
        FACT,
        SUB_FACTORIAL,
        DOUBLE_FACTORIAL
    );

    /**
     * The number of possible permutations with this many elements.
     */
    F factorial();

    /**
     * The number of possible <a href="https://en.wikipedia.org/wiki/Derangement">derangements</a> with this many elements.
     */
    F subfactorial();

    /***
     * https://en.wikipedia.org/wiki/Double_factorial
     */
    F doubleFactorial();


    @Getter
    class Configuration implements ConfigurationAspect {
        @With
        private final Long maxArgument;

        @With
        private final Long maxSubArgument;

        @With
        private final Long maxDoubleArgument;

        public Configuration() {
            this(50000L, 2000L, null);
        }

        @lombok.Builder
        private Configuration(Long maxArgument, Long maxSubArgument, Long maxDoubleArgument) {
            this.maxArgument = maxArgument;
            this.maxSubArgument = maxSubArgument != null ? maxSubArgument : maxArgument;
            this.maxDoubleArgument = maxDoubleArgument != null ? maxDoubleArgument : maxArgument;
        }

        @Override
        public List<Class<?>> associatedWith() {
            return Collections.singletonList(Factoriable.class);
        }
    }

}
