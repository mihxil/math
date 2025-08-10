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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.math.uncertainnumbers.field.UncertainDouble;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.SI.DecimalPrefix.k;
import static org.meeuw.physics.SIUnit.m;

class PrefixedUnitTest {

    final Units kN = SI.N.withPrefix(k);

    @Test
    void getDimensions() {
        assertThat(kN.getDimensions()).isEqualTo(SI.N.getDimensions());
    }

    @Test
    void iterator() {
        List<UnitExponent> list = new ArrayList<>();
        kN.iterator().forEachRemaining(list::add);
        assertThat(list.toString()).isEqualTo("[kN]");
    }

    @Test
    void getSIFactor() {
        assertThat(kN.getSIFactor()).isEqualTo(UncertainDouble.exactly(1000d));
    }

    @Test
    void reciprocal() {
        Units units = kN.reciprocal();
        assertThat(units.getSIFactor()).isEqualTo(UncertainDouble.exactly(0.001d));
        assertThat(units.toString()).isEqualTo("kN⁻¹");
    }

    @Test
    void times() {
        Units work = kN.times(m);
        assertThat(work.toString()).isEqualTo("kN·m");
    }


}
