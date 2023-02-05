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

import org.meeuw.math.abstractalgebra.WithScalarOperations;
import org.meeuw.math.exceptions.ReciprocalException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface WithScalarTheory<E extends WithScalarOperations<E, S>,
    S> extends ElementTheory<E> {


    String SCALARS = "scalars";

    @Property
    default void times(@ForAll(ELEMENTS) E e, @ForAll(SCALARS) S scalar) {
        assertThat(e.times(scalar)).isNotNull();

        try {
            assertThat(e.times(scalar).dividedBy(scalar)).isEqualTo(e);
        } catch (ReciprocalException ae) {
            getLogger().debug("{} * {} / {} -> {}", e, scalar, scalar, ae.getMessage());
        }
        //assertThat(e.times(scalar.sqr())).isEqualTo(e.times(scalar).times(scalar));
    }

    @Property
    default void dividedBy(@ForAll(ELEMENTS) E e, @ForAll(SCALARS) S scalar) {
        try {
            assertThat(e.dividedBy(scalar)).isNotNull();
        } catch (ReciprocalException ae) {
            //
        }
    }

    @Provide
    Arbitrary<S> scalars();



}
