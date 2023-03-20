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

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.integers.*;
import org.meeuw.theories.abstractalgebra.AdditiveAbelianSemiGroupTheory;
import org.meeuw.theories.abstractalgebra.MultiplicativeMonoidTheory;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.InvalidFactorial;
import org.meeuw.theories.numbers.ScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.PositiveInteger.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Log4j2
class PositiveIntegerTest implements
    MultiplicativeMonoidTheory<PositiveInteger>,
    AdditiveAbelianSemiGroupTheory<PositiveInteger>,
    ScalarTheory<PositiveInteger> {

    @BeforeAll
    public static void setup() {
        ConfigurationService.defaultConfiguration((con) ->
            con.configure(Factoriable.Configuration.class, c -> c.withMaxArgument(1000L))
        );
    }


    @Test
    public void test() {
        assertThatThrownBy(() -> of(1).times(of(-1))).isInstanceOf(InvalidElementCreationException.class);
        assertThat(of(5).plus(of(7))).isEqualTo(of(12));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 2000, 10000, 50000})
    public void fact(int value) {
        ConfigurationService.withAspect(Factoriable.Configuration.class, c -> c.withMaxArgument(2001L), () -> {
            try {
                log.info("{}! = {}", value, of(value).factorial());
            } catch (InvalidFactorial invalidFactorial) {
                log.info("{}! => {}", value, invalidFactorial.getMessage());
            }
        });
    }
    @Override
    public Arbitrary<PositiveInteger> elements() {
        return Arbitraries.randomValue(
            PositiveIntegers.INSTANCE::nextRandom
            ).injectDuplicates(10);
    }
}
