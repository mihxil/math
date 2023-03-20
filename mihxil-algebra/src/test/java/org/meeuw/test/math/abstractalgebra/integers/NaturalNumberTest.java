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
package org.meeuw.test.math.abstractalgebra.integers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.integers.NaturalNumber;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.theories.abstractalgebra.*;
import org.meeuw.theories.numbers.ScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.NaturalNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class NaturalNumberTest implements
    MultiplicativeMonoidTheory<NaturalNumber>,
    AdditiveMonoidTheory<NaturalNumber>,
    AdditiveAbelianSemiGroupTheory<NaturalNumber>,
    MultiplicativeAbelianSemiGroupTheory<NaturalNumber>,
    ScalarTheory<NaturalNumber> {

    @BeforeAll
    public static void setup() {
        PositiveIntegerTest.setup();
    }

    @Test
    public void test() {
        assertThatThrownBy(() -> of(1).times(of(-1))).isInstanceOf(InvalidElementCreationException.class);
        assertThat(of(5).plus(of(7))).isEqualTo(of(12));
    }

    @Override
    public Arbitrary<NaturalNumber> elements() {
        return Arbitraries.randomValue(r ->
            NaturalNumber.of(Math.abs(r.nextInt(100_000)))).injectDuplicates(10);
    }
}
