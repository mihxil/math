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
package org.meeuw.test.math.abstractalgebra.integers;

import java.util.stream.Collectors;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.abstractalgebra.integers.*;
import org.meeuw.math.abstractalgebra.test.RngTheory;
import org.meeuw.math.abstractalgebra.test.SignedNumberTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.numbers.test.SizeableScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.EvenInteger.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class EvenIntegerTest implements
    RngTheory<EvenInteger>,
    SizeableScalarTheory<EvenInteger, EvenInteger>,
    SignedNumberTheory<EvenInteger> {

    @BeforeAll
    public static void setup() {
        PositiveIntegerTest.setup();
    }

    @Test
    public void test() {
        assertThatThrownBy(() -> of(1)).isInstanceOf(InvalidElementCreationException.class);

        assertThat(of(2).times(of(4))).isEqualTo(of(8));
        assertThat(of(2).plus(of(4))).isEqualTo(of(6));
        assertThat(of(2).plus(of(4).negation())).isEqualTo(of(-2));
        assertThat(of(2).plus(of(2).getStructure().zero())).isEqualTo(of(2));

        Assertions.assertThat(of(2).plus(OddInteger.of(5).negation())).isEqualTo(OddInteger.of(-3));

    }

    @Test
    public void compareTo() {
        //assertThat(new EvenIntegerElement(-2019178024101599495L).compareTo(
        //new BigDecimal(-2019178024101599496L))).isGreaterThan(0);
    }

    @Override
    public Arbitrary<EvenInteger> elements() {
        return Arbitraries.randomValue((random) -> EvenInteger.of(2 * (random.nextLong() / 2)));
    }

    @Test
    void stream() {
        Assertions.assertThat(EvenIntegers.INSTANCE.stream().limit(11).map(EvenInteger::longValue)
            .collect(Collectors.toList())).containsExactly(0L, 2L, -2L, 4L, -4L, 6L, -6L, 8L, -8L, 10L, -10L);
    }

}
