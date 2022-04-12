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
package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveSemiGroupTheory<E extends AdditiveSemiGroupElement<E>>
    extends MagmaTheory<E> {

    @Property
    default void additiveSemiGroupOperators(@ForAll(STRUCTURE) AdditiveSemiGroup<E> s) {
        assertThat(s.getSupportedOperators()).contains(BasicAlgebraicBinaryOperator.ADDITION);
    }

    @Property
    default void additiveAssociativity (
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2,
            @ForAll(ELEMENTS) E v3
            ) {
        assertThat((v1.plus(v2)).plus(v3)).isEqualTo(v1.plus((v2.plus(v3))));
    }

}
