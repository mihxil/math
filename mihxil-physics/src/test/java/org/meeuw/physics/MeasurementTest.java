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

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.PARENTHESES;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class MeasurementTest  {
    Units DISTANCE = SI.INSTANCE.forQuantity(Quantity.DISTANCE);
    Units LENGTH = SI.INSTANCE.forQuantity(Quantity.LENGTH);

    @Test
    public void add() {
        Measurement door = new Measurement(2.00, 0.03, DISTANCE);
        assertThat(door.toString()).isEqualTo("2.00 ± 0.03 m");
        Measurement knob = new Measurement(0.88, 0.04, DISTANCE);
        PhysicalNumber height = door.minus(knob);
        assertThat(height.toString()).isEqualTo("1.12 ± 0.07 m");
    }

    @Test
    public void area() {
        Measurement height = new Measurement(21, 0.2, LENGTH);
        Measurement width = new Measurement(30, 1, LENGTH);
        PhysicalNumber area =  height.times(width);
        assertThat(area.toString()).isEqualTo("630 ± 26 m²"); // or should that be 27?
        ConfigurationService.withAspect(UncertaintyConfiguration.class, (ub) -> ub.withNotation(PARENTHESES),
            () -> assertThat(area.toString()).isEqualTo("630(26) m²")
        );
    }

    @Test
    public void illegalAdd() {
        assertThatThrownBy(() -> {
            Measurement door = new Measurement(2.00, 0.03, SIUnit.m);
            Measurement knob = new Measurement(0.88, 0.04, SIUnit.mol);
            PhysicalNumber height = door.minus(knob);
        }).isInstanceOf(DimensionsMismatchException.class);
    }

    @Test
    public void divide() {
        Measurement speed = new Measurement(6.0, 0.4, SI.INSTANCE.forQuantity(Quantity.VELOCITY));
        assertThat(speed.toString()).isEqualTo("6.0 ± 0.4 m·s⁻¹");
        assertThat(speed.getUnits().getDimensions().toString()).isEqualTo("LT⁻¹");

        Measurement distance = new Measurement(2.0, 0.05, SI.INSTANCE.forQuantity(Quantity.DISTANCE));
        PhysicalNumber duration = distance.dividedBy(speed);
        assertThat(duration.toString()).isEqualTo("0.33 ± 0.03 s");
    }

    @Test
    public void testStructure() {
        Measurement a = new Measurement(6.0, 0.4, SI.mPerS);
        assertThat(a.plus(a.getUnits().zero())).isEqualTo(a);
        assertThat(a.times(a.getStructure().one())).isEqualTo(a);

        assertThat(a.times(2d))
            .isEqualTo(new Measurement(12.0, 0.8, SI.mPerS));

    }

}
