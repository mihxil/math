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

import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.FieldElement;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface FieldTheory<E extends FieldElement<E>> extends
    DivisibleGroupTheory<E>,
    DivisionRingTheory<E>,
    GroupTheory<E> {

    @Property
    default void fieldOperators(@ForAll(STRUCTURE) Field<E> s) {
        assertThat(s.getSupportedOperators()).contains(BasicAlgebraicBinaryOperator.OPERATION);
        assertThat(s.getSupportedOperators()).contains(BasicAlgebraicBinaryOperator.MULTIPLICATION);
        assertThat(s.getSupportedOperators()).contains(BasicAlgebraicBinaryOperator.ADDITION);
        log().info(s + " supports " + s.getSupportedOperators());
        log().info(s + " group operator  " + s.groupOperator() + " with " + s.unity());
    }


}
