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
package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.IllegalPowerException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;
import static org.meeuw.math.DoubleUtils.uncertaintyForDouble;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public strictfp interface CompleteScalarFieldTheory<E extends CompleteScalarFieldElement<E>> extends
    CompleteFieldTheory<E> {

    @Property
    default void sqrt(@ForAll(ELEMENTS) E e) {
        Assume.that(! e.isNegative());
        E sqrt = e.sqrt();
        assertThat(sqrt.doubleValue())
            .isCloseTo(
            Math.sqrt(e.doubleValue()), offset(uncertaintyForDouble(sqrt.doubleValue())));
    }

    @Property
    default void sin(@ForAll(ELEMENTS) E e) {
        E sin = e.sin();
        assertThat(sin.doubleValue()).isCloseTo(
            Math.sin(e.doubleValue()), offset(0.01)
        );
    }

    @Property
    default void cos(@ForAll(ELEMENTS) E e) {
        E cos = e.cos();
        assertThat(cos.doubleValue()).isCloseTo(
            Math.cos(e.doubleValue()), offset(0.01)
        );
    }

    @Property
    default void pow(@ForAll(ELEMENTS) E e, @ForAll(ELEMENTS) E  exponent) {
        Assume.that(! e.isNegative());
        if (e.isZero()) {
            if (exponent.isNegative()) {
                assertThatThrownBy(() -> {
                        getLogger().info("{}^{} = {} (expected exception)", e, exponent, e.pow(exponent));
                    }
                ).isInstanceOfAny(IllegalPowerException.class, DivisionByZeroException.class);
                return;
            }
        }

        E pow = e.pow(exponent);
        double expected = Math.pow(e.doubleValue(), exponent.doubleValue());
        assertThat(pow.doubleValue())
            //.withFailMessage("%s ^ %s = %s != %s", e, exponent, pow, expected)
            .isCloseTo(expected, offset(
                1000 * Math.max(
                    uncertaintyForDouble(pow.doubleValue()),
                    uncertaintyForDouble(expected)))
            );
    }
}