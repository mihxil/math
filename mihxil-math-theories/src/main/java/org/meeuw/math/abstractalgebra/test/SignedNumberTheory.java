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

import org.meeuw.math.abstractalgebra.AdditiveMonoidElement;
import org.meeuw.math.numbers.SignedNumber;
import org.meeuw.util.test.ElementTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface SignedNumberTheory<E extends SignedNumber<E>> extends ElementTheory<E> {

    @Property
    default void signum(@ForAll(ELEMENT) E e) {
        assertThat(e.signum()).isIn(-1, 0, 1);
        assertThat(e.isZero()).isEqualTo(e.signum() == 0);
        assertThat(e.isPositive()).isEqualTo(e.signum() == 1);
        assertThat(e.isNegative()).isEqualTo(e.signum() == -1);
    }

    @Property
    default void signumOfZero(@ForAll(ELEMENT) E e) {
        if (e instanceof AdditiveMonoidElement){
            assertThat(((SignedNumber) ((AdditiveMonoidElement<?>) e).getStructure().zero()).signum()).isEqualTo(0);
        }
    }

    @Property
    default void compareToConsistentWithSignum(@ForAll(ELEMENTS) E e) {
        if (e instanceof AdditiveMonoidElement) {
            E zero = (E) ((AdditiveMonoidElement<?>) e).getStructure().zero();
            int compareToZero = e.compareTo(zero);

            if (compareToZero == 0) {
                assertThat(e.signum()).isEqualTo(0);
                assertThat(e).isEqualTo(zero);
            } else if (compareToZero < 0) {
                assertThat(e.signum()).isEqualTo(-1);
            } else {
                assertThat(e.signum()).isEqualTo(1);
            }
        }
    }

}
