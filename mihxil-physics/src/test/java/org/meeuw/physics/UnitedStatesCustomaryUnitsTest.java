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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.uncertainnumbers.field.UncertainDouble.of;
import static org.meeuw.physics.SI.DecimalPrefix.k;
import static org.meeuw.physics.SIUnit.m;

@Log4j2
class UnitedStatesCustomaryUnitsTest {


    @Test
    void inchCmYd() {
        PhysicalNumber oneInch = new Measurement(of(1, 0.01), UnitedStatesCustomaryUnits.in);
        PhysicalNumber inCm = oneInch.toUnits(m.withPrefix(SI.DecimalPrefix.c));
        assertThat(inCm.toString()).isEqualTo("2.54 ± 0.03 cm");

        assertThat(oneInch.eq(inCm)).isTrue();

        PhysicalNumber inYd = inCm.toUnits(UnitedStatesCustomaryUnits.INSTANCE.forDimension(Dimension.L));

        assertThat(inYd.eq(inCm)).isTrue();
        assertThat(inYd.toString()).isEqualTo("0.0278 ± 0.0003 yd");

        assertThat(UnitedStatesCustomaryUnits.US.yd.reciprocal().toString()).isEqualTo("/yd");
    }

    @Test
    void mileKm() {
        PhysicalNumber twoMile = new Measurement(of(2, 0.01), UnitedStatesCustomaryUnits.mi);
        PhysicalNumber inKm = twoMile.toUnits(m.withPrefix(k));
        assertThat(inKm.toString()).isEqualTo("3.219 ± 0.016 km");
    }

    @Test
    void newton() {

    }


}
