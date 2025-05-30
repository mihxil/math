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
package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.IntegerArbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.theories.ComparableTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Log4j2
class UnitExponentTest implements ComparableTheory<UnitExponent> {

    @Override
    public Arbitrary<UnitExponent> datapoints() {
        IntegerArbitrary unit = Arbitraries.integers()
            .between(0, SIUnit.values().length - 1);
        IntegerArbitrary exponent = Arbitraries.integers()
            .between(-5, 5);
        return Combinators.combine(
            unit, exponent
            ).as((u, e) -> new UnitExponent(SIUnit.values()[u], e))
            .injectDuplicates(0.2);
    }

    @Test
    void times() {
        UnitExponent g = UnitExponent.of(SI.g, 1);
        assertThat(g.times(g).getExponent()).isEqualTo(2);

        assertThatThrownBy(() -> g.times(UnitExponent.of(SI.pc, 2))).isInstanceOf(IllegalArgumentException.class);


    }

    @Test
    void testEquals() {
    }
}
