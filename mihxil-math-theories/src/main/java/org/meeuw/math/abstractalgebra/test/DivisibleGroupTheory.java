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

import org.meeuw.math.abstractalgebra.DivisibleGroupElement;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisibleGroupTheory<E extends DivisibleGroupElement<E>>
    extends
    MultiplicativeAbelianGroupTheory<E> {

    @Property
    default void dividedByLong(@ForAll(ELEMENTS) E v1, @ForAll("positiveLongs") long divisor) {
        try {
            assertThat(v1.dividedBy(divisor).getStructure()).isEqualTo(v1.getStructure());
            assertThat(v1.dividedBy(divisor).times(divisor).eq(v1)).withFailMessage("(%s / %s) * %s = %s != %s", v1, divisor, divisor, v1.dividedBy(divisor).times(divisor), v1).isTrue();
            assertThat(v1.times(divisor).dividedBy(divisor).eq(v1)).withFailMessage("(%s * %s) / %s = %s != %s", v1, divisor, divisor, v1.dividedBy(divisor).times(divisor), v1).isTrue();

        } catch (DivisionByZeroException divisionByZeroException) {
            getLogger().info("{} / {} -> {}", v1, divisor, divisionByZeroException.getMessage());
            assertThat(BasicAlgebraicBinaryOperator.DIVISION.isAlgebraicFor(v1)).isFalse();
        }
    }

    @Provide
    default Arbitrary<Long> positiveLongs() {
        return Arbitraries.longs().between(1, 1_000_000);
    }

}
