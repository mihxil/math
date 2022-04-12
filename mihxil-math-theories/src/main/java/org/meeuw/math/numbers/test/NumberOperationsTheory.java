/*
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
package org.meeuw.math.numbers.test;

import java.math.BigDecimal;

import net.jqwik.api.*;

import org.meeuw.math.numbers.NumberOperations;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
public interface NumberOperationsTheory<N extends Number> {

    String NUMBERS = "numbers";

    @Provide
    Arbitrary<? extends N> numbers();

    NumberOperations<N> operations();

    @Property
    default void  getFractionalUncertainty(N value, N unc) {


    }

    @Property
    default void sqr(@ForAll(NUMBERS) N v) {
        assertThat(operations().sqr(v).doubleValue()).isEqualTo(v.doubleValue() * v.doubleValue());
    }

    @Property
    default void sqrt(N radicand) {
        assertThat(operations().sqrt(radicand).getValue().doubleValue()).isEqualTo(Math.sqrt(radicand.doubleValue()));

    }

    N abs(N v);

    N reciprocal(N v);

    N negate(N v);

    N multiply(N n1, N n2);

    default N multiply(int n1, N n2) {
        N result = n2;
        for (int i = n1; i > 1; i--) {
            result = add(result, n2);
        }
        return result;
    }

    N divide(N n1, N n2);

    N add(N n1, N n2);

    default N minus(N n1, N n2) {
        return add(n1, negate(n2));
    }

    N pow(N n1, int exponent);

    N pow(N n1, N exponent);

    boolean lt(N n1, N n2);

    boolean lte(N n1, N n2);

    default boolean gt(N n1, N n2) {
        return ! lte(n1, n2);
    }

    default boolean gte(N n1, N n2) {
        return ! lt(n1, n2);
    }

    boolean isFinite(N n1);

    boolean isNaN(N n1);

    int signum(N n);

    BigDecimal bigDecimalValue(N n);

    N sin(N n);

    N cos(N n);
}
