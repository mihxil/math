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
package org.meeuw.theories.abstractalgebra;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeAbelianSemiGroupTheory<E extends MultiplicativeSemiGroupElement<E>>
    extends MultiplicativeSemiGroupTheory<E> {

    @Property
    default void multiplicativeCommutativity (
        @ForAll(ELEMENTS) E v1,
        @ForAll(ELEMENTS) E v2) {
        E v1Timesv2 = v1.times(v2);
        E v2Timesv1 = v2.times(v1);
        assertThat(v1Timesv2.eq(v2Timesv1))
            .withFailMessage(() -> String.format("%s . %s = %s !=  %s . %s = %s",
                v1, v2, v1Timesv2, v2, v1, v2Timesv1
            ))
            .isTrue();
    }

    @Property
    default void multiplicativeCommutativityProperty(
        @ForAll(STRUCTURE) AlgebraicStructure<?> group) {
        MultiplicativeSemiGroup<E> casted = (MultiplicativeSemiGroup<E>) group;
        assertThat(casted.multiplicationIsCommutative()).isTrue();
    }

}
