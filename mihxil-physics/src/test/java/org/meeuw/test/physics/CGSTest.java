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
package org.meeuw.test.physics;

import org.junit.jupiter.api.Test;

import org.meeuw.physics.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.CGS.INSTANCE;
import static org.meeuw.physics.Quantity.ACCELERATION;
import static org.meeuw.physics.SIUnit.m;
import static org.meeuw.physics.SIUnit.s;

class CGSTest {

    @Test
    void cm() {
        assertThat(CGS.CGSUnit.cm.getSIFactor().doubleValue()).isEqualTo(0.01d);
        assertThat(CGS.cmPerS.getSIFactor().doubleValue()).isEqualTo(0.01d);
    }

    @Test
    void velocity() {
        PhysicalNumber slow = new Measurement(2, 0.1, CGS.cmPerS);
        assertThat(slow.toString()).isEqualTo("2.00 ± 0.10 cm·s⁻¹");

        PhysicalNumber inSI = slow.toUnits(SI.INSTANCE);
        assertThat(inSI.toString()).isEqualTo("0.0200 ± 0.0010 m·s⁻¹");
    }

    @Test
    void acceleration() {
        Units gal = INSTANCE.forQuantity(ACCELERATION);
        assertThat(gal).isSameAs(CGS.Gal);
        assertThat(gal.getSIFactor().doubleValue()).isEqualTo(0.01d);

        PhysicalNumber acc = new Measurement(1, 0.1, gal);
        Units mPerS2 = SI.INSTANCE.forQuantity(ACCELERATION);

        assertThat(mPerS2.equals(gal)).isFalse();

        assertThat(mPerS2).isEqualTo(m.per(s.sqr()));
        assertThat(acc.toUnits(mPerS2).toString()).isEqualTo("0.0100 ± 0.0010 m·s⁻²");

    }

}
