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

import java.util.ArrayList;
import java.util.Collection;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.test.MultiplicativeAbelianGroupTheory;
import org.meeuw.math.text.FormatService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.physics.SI.DecimalPrefix.k;
import static org.meeuw.physics.SI.INSTANCE;
import static org.meeuw.physics.SI.km;
import static org.meeuw.physics.SIUnit.m;
import static org.meeuw.physics.SIUnit.s;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class UnitsTest implements MultiplicativeAbelianGroupTheory<Units> {

    final Units DISTANCE = INSTANCE.forQuantity(Quantity.DISTANCE);
    final Units TIME = INSTANCE.forQuantity(Quantity.TIME);


     @Test
    public void N() {
        assertThat(SI.N.toString()).isEqualTo("N");
        assertThat(SI.N.getDescription()).isEqualTo("Newton");
        assertThat(SI.N.getDimensions().toString()).isEqualTo("LMT⁻²");
    }

    @Test
    public void eV() {
        assertThat(SI.eV.toString()).isEqualTo("eV");
        assertThat(SI.eV.getDescription()).isEqualTo("electron-volt");
        assertThat(SI.eV.getDimensions().toString()).isEqualTo("L²MT⁻²");
        assertThat(SI.eV.getSIFactor().doubleValue()).isEqualTo(1.602176634E-19);
    }

    @Test
    public void kmph() {
        Units km =   SIUnit.m.withPrefix(k);
        assertThat(km.toString()).isEqualTo("km");
        assertThat(km.getSIFactor().doubleValue()).isEqualTo(1000);
        assertThat(SI.hour.getSIFactor().doubleValue()).isEqualTo(3600);
        Units kmph = km.dividedBy(SI.hour).withName("km/h");
        assertThat(kmph.toString()).isEqualTo("km/h");
        assertThat(kmph.getDimensions()).isEqualTo(Quantity.SPEED.getDimensionalAnalysis());
        assertThat(kmph.getSIFactor().doubleValue()).isEqualTo(0.2777777777777778d);


        PhysicalNumber n = new Measurement(10d, 1d, kmph);

        PhysicalNumber mps = n.toUnits(SI.INSTANCE);

        log.info("{} -> {}", n, mps);
        assertThat(mps.toString()).isEqualTo("2.8 ± 0.3 m·s⁻¹");

        assertThat(mps).isEqualTo(n.toUnits(SI.INSTANCE));

        assertThat(n.toUnits(Planck.INSTANCE).toString()).isEqualTo("(9.3 ± 0.8)·10⁻⁹ ℓₚ·tₚ⁻¹");
    }

    @Override
    public Arbitrary<Units> elements() {
        Collection<Units> units = new ArrayList<>();
        units.addAll(INSTANCE.getUnits());
        units.addAll(CGS.INSTANCE.getUnits());
        units.addAll(Planck.INSTANCE.getUnits());
        return Arbitraries.of(units);
    }

    @Test
    public void distanceAndTime() {
        assertThat(DISTANCE).isEqualTo(SIUnit.m);
        assertThat(TIME).isEqualTo(SIUnit.s);
    }


    @Test
    public void reciprocal() {
        Units perKm = UnitsGroup.INSTANCE.one().dividedBy(m.withPrefix(k));
        assertThat(perKm.reciprocal()).isEqualTo(km);
        assertThat(km.pow(-1)).isEqualTo(perKm);
        assertThat(km.reciprocal()).isEqualTo(perKm);

    }

    @Test
    public void forDivision() {
        assertThat(Units.forDivision(DISTANCE, TIME).toString()).isEqualTo("m·s⁻¹");
        assertThat(Units.forDivision(null, TIME)).isNull();
        assertThat(Units.forDivision(DISTANCE, null)).isNull();
    }

    @Test
    public void forMultiplication() {
        assertThat(Units.forMultiplication(DISTANCE, TIME)).isEqualTo(Units.of(m, s));
        assertThat(Units.forMultiplication(null, TIME)).isNull();
        assertThat(Units.forMultiplication(DISTANCE, null)).isNull();
    }

    @Test
    public void forAddition() {
        assertThatThrownBy(() -> Units.forAddition(DISTANCE, TIME)).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Cannot add [L] to [T]");
        assertThatThrownBy(() -> Units.forAddition(null, TIME)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Units.forAddition(DISTANCE, null)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void forExponentiation() {
        assertThat(Units.forExponentiation(DISTANCE, 2)).isEqualTo(Units.of(m, m));
        assertThat(Units.forExponentiation(null, 2)).isNull();
    }

    @Test
    public void forInversion() {
        assertThat(Units.forInversion(DISTANCE).toString()).isEqualTo("m⁻¹");
        assertThat(Units.forInversion(null)).isNull();
    }

    @Test
    public void parse() {
        assertThat(FormatService.fromString("/s", Units.class)).isEqualTo(Units.of(SIUnit.s).reciprocal());
        assertThat(FormatService.fromString("", Units.class)).isEqualTo(Units.DIMENSIONLESS);
    }
}
