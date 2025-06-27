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
package org.meeuw.theories.numbers;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.numbers.Scalar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Theory testing for {@link Scalar} implementations.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <S> the type of {@link Scalar} to test
 */
public interface ScalarTheory<S extends Scalar<S>>
    extends SizeableScalarTheory<S, S> {


    @Property
    default void implementsScalar(@ForAll(ELEMENTS) S e1) {
        assertThat(e1).isInstanceOf(Scalar.class);
        assertThat(e1.abs()).isInstanceOf(Scalar.class);
    }

    @Property
    default void absSignum(@ForAll(ELEMENTS) S e) {
        assertThat(e.abs().signum()).isIn(0, 1);
    }

    @Property
    default void asNumber(@ForAll(ELEMENTS) S e) {
        assertThat(e.doubleValue()).isEqualTo(e.asNumber().doubleValue());
    }


}
