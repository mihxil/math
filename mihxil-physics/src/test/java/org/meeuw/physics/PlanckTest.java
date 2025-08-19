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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlanckTest {

    @Test
    public void c() {
        assertThat(Planck.c.toString()).isEqualTo("1 ℓₚ·tₚ⁻¹");
        Units units = Planck.c.getUnits();
        assertThat(units.toString()).isEqualTo("ℓₚ·tₚ⁻¹");

        PhysicalNumber cSI = Planck.c.toUnits(SI.INSTANCE);
        assertThat(cSI.toString()).isEqualTo("(2.99792 ± 0.00007)·10⁸ m·s⁻¹");

        assertThat(cSI.toUnits(Planck.INSTANCE).toString()).isEqualTo("1.00000 ± 0.00004 ℓₚ·tₚ⁻¹");
    }

    @Test
    public void BoltzmannConstant() {
        assertThat(Planck.kB.toString()).isEqualTo("1 ℓₚ·tₚ⁻¹");
    }

}
