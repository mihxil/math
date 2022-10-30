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

import org.meeuw.math.abstractalgebra.AlgebraicElement;

/**
 * Like {@link java.util.function.BiPredicate}, but the difference is that this is not itself generic, but only its {@link #test(AlgebraicElement, AlgebraicElement)} method.
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface AlgebraicComparisonOperator extends OperatorInterface {

    <E extends AlgebraicElement<E>>  boolean test(E arg1, E arg2);


    String stringify(String element1, String element2);

    default <E extends AlgebraicElement<E>> String stringify(E element1, E element2) {
        return stringify(element1.toString(), element2.toString());
    }

    default String getSymbol() {
        return stringify("", "").trim();
    }
}
