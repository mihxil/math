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

import org.meeuw.math.abstractalgebra.AlgebraicElement;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.exceptions.DivisionByZeroException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.TextUtils.superscript;
import static org.meeuw.math.uncertainnumbers.CompareConfiguration.withLooseEquals;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoidTheory<E extends MultiplicativeMonoidElement<E>>
    extends MultiplicativeSemiGroupTheory<E> {

    @Property
    default void one(
        @ForAll(ELEMENTS) E v) {
        assertThat(v.times(v.getStructure().one()).eq(v)).isTrue();
    }

    @Override
    @Property
    default void pow0(
         @ForAll(ELEMENTS) E v1
    )  {

        try {
            assertThat(v1.pow(0))
                .usingComparator(AlgebraicElement.eqComparator())
                .isEqualTo(v1.getStructure().one());
        } catch (DivisionByZeroException ae){
            log().warn("" + v1 + superscript(0) + ": " + ae.getMessage());
        }
    }

    @Property
    default void isOne(
         @ForAll(ELEMENTS) E v1,
         @ForAll(ELEMENTS) E v2
    )  {
        withLooseEquals(() -> {
            if (v1.isOne()) {
                assertThat(v2.times(v1)).isEqualTo(v2);
                assertThat(v1.times(v2)).isEqualTo(v2);
            }
            if (v2.isOne()) {
                assertThat(v1.times(v2)).isEqualTo(v1);
                assertThat(v2.times(v1)).isEqualTo(v1);
            }
        });
    }
}
