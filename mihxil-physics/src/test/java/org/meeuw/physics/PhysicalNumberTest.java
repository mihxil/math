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

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.test.*;
import org.meeuw.math.numbers.test.NumberTheory;
import org.meeuw.math.numbers.test.ScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.Measurement.measurement;
import static org.meeuw.physics.SI.*;
import static org.meeuw.physics.SI.DecimalPrefix.k;
import static org.meeuw.physics.SI.DecimalPrefix.none;
import static org.meeuw.physics.SIUnit.kg;
import static org.meeuw.physics.SIUnit.m;


/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class PhysicalNumberTest implements
    MultiplicativeAbelianGroupTheory<PhysicalNumber>,
    SignedNumberTheory<PhysicalNumber>,
    UncertainDoubleTheory<PhysicalNumber>,
    NumberTheory<PhysicalNumber>,
    ScalarTheory<PhysicalNumber> {

    @Test
    public void add() {
        PhysicalNumber lys = measurement(2, 0.1, ly);
        PhysicalNumber psc = measurement(1, 0.1, pc);
        log.info("{} + {} ({})= {}", lys, psc, psc.toUnits(ly), lys.plus(psc));
        assertThat(lys.plus(psc).toString()).isEqualTo("5.3 ± 0.4 ly");
        assertThat(psc.plus(lys).toString()).isEqualTo("1.61 ± 0.13 pc");
    }

    @Test
    public void toUnits() {
        Units pc = Units.of(SI.pc);
        PhysicalNumber two_pc = measurement(2, 0.1, pc);

        PhysicalNumber inLightYear = two_pc.toUnits(Units.of(ly));
        assertThat(inLightYear.doubleValue()).isEqualTo(6.523127554334867);
        assertThat(inLightYear.toString()).isEqualTo("6.5 ± 0.3 ly");

        assertThat(inLightYear.toUnits(SI.INSTANCE).toString()).isEqualTo("(6.2 ± 0.3)·10¹⁶ m");
        assertThat(inLightYear.toUnits(CGS.INSTANCE).toString()).isEqualTo("(6.2 ± 0.3)·10¹⁸ cm");
        assertThat(inLightYear.toUnits(Planck.INSTANCE).toString()).isEqualTo("(3.82 ± 0.17)·10⁵¹ ℓₚ");
    }

    @Test
    public void prefix() {
        Unit km = m.withPrefix(k);

        Measurement measurementInKm = measurement(1, 0.1, km);

        assertThat(measurementInKm.toString()).isEqualTo("1.00 ± 0.10 km");

        Measurement measurementInM = measurement(1010, 100, m);

        log.info("{} =? {}", measurementInM, measurementInKm);

        assertThat(measurementInM.equals(measurementInKm)).isTrue();

    }

    @Test
    public void kilogramPrefix() {

        Measurement measurementInKg = measurement(1, 0.1, kg);

        assertThat(measurementInKg.toString()).isEqualTo("1.00 ± 0.10 kg");

        Units g = kg.withPrefix(none);
        Measurement measurementInG = measurement(1010, 100, g);

        log.info("{} =? {}", measurementInKg, measurementInG);

        assertThat(measurementInKg.equals(measurementInG)).isTrue();

    }

    @Test
    public void lt() {
        PhysicalNumber two_lightyear = measurement(2, 0.1, ly);
        PhysicalNumber three_km = measurement(3, 0.1, m);
        assertThat(three_km.lt(two_lightyear)).isTrue();
    }

    @Test
    public void equals() {
        PhysicalNumber two_lightyear = measurement(2, 0.1, ly);
        PhysicalNumber inm = measurement(3, 0.1, m);


    }

    /**
     * Returns only velocities for now.
     *
     */
    @Override
    public Arbitrary<PhysicalNumber> elements() {
        Units[] units = new Units[ ] {mPerS, J};
        return
            Arbitraries
                .<PhysicalNumber>randomValue(
                    (random) -> measurement(
                        random.nextDouble() * 200 - 100,
                        Math.abs(random.nextDouble() * 10),
                        units[random.nextInt(units.length)]
                    )
                )
                .injectDuplicates(0.01)
                .dontShrink()
                .edgeCases(config -> {
                    config.add(measurement(0, 0.001, mPerS));
                    config.add(PhysicalConstant.c);
                });

    }

    @Override
    public Arbitrary<PhysicalNumber> numbers() {
        return elements();
    }

}
