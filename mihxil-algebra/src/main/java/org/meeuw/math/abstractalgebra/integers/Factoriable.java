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

import lombok.Getter;
import lombok.With;

import java.util.*;

import org.meeuw.configuration.ConfigurationAspect;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.operators.AlgebraicUnaryOperator;
import org.meeuw.math.operators.SimpleAlgebraicUnaryOperator;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethod;
import static org.meeuw.math.CollectionUtils.navigableSet;


/**
 *  Integer types that can be positive can also define the factorial operator.
 */
public interface Factoriable<F extends MultiplicativeMonoidElement<F>>  {

    AlgebraicUnaryOperator FACT = new SimpleAlgebraicUnaryOperator(
        getDeclaredMethod(Factoriable.class, "factorial"),
        element  -> element + "!",
        "factorial"
    );

    AlgebraicUnaryOperator SUB_FACTORIAL = new SimpleAlgebraicUnaryOperator(
        getDeclaredMethod(Factoriable.class, "subfactorial"),
        element -> "!" + element,
        "subfactorial"
    );

    AlgebraicUnaryOperator DOUBLE_FACTORIAL = new SimpleAlgebraicUnaryOperator(
        getDeclaredMethod(Factoriable.class, "doubleFactorial"),
            element -> element + "!!",
        "double factorial"
    );
            ;
    NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(
        FACT,
        SUB_FACTORIAL,
        DOUBLE_FACTORIAL
    );

    /**
     * The number of possible permutations with this many elements. {@code n * n -1 * n -2 ... * 1}
     */
    F factorial();

    /**
     * The number of possible <a href="https://en.wikipedia.org/wiki/Derangement">derangements</a> with this many elements.
     * {@code !n = (n-1)(! (n-1) + ! (n -2 )}
     */
    F subfactorial();

    /***
     * <a href="https://en.wikipedia.org/wiki/Double_factorial">The double factorial</a>. {@code n!! = n * (n - 2) * (n - 4) .. [1 | 2]}
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
