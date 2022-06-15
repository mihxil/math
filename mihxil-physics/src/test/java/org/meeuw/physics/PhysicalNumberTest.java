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
package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.test.*;
import org.meeuw.math.numbers.test.NumberTheory;

import static org.assertj.core.api.Assertions.assertThat;
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
    NumberTheory<PhysicalNumber> {

    @Test
    public void add() {
        PhysicalNumber lys = new Measurement(2, 0.1, SI.ly);
        PhysicalNumber psc = new Measurement(1, 0.1, SI.pc);
        log.info("{} + {} ({})= {}", lys, psc, psc.toUnits(SI.ly), lys.plus(psc));
        assertThat(lys.plus(psc).toString()).isEqualTo("5.3 ± 0.4 ly");
        assertThat(psc.plus(lys).toString()).isEqualTo("1.61 ± 0.13 pc");
    }

    @Test
    public void toUnits() {
        Units pc = Units.of(SI.pc);
        PhysicalNumber two_pc = new Measurement(2, 0.1, pc);

        PhysicalNumber inLightYear = two_pc.toUnits(Units.of(SI.ly));
        assertThat(inLightYear.getValue()).isEqualTo(6.523127554334867);
        assertThat(inLightYear.toString()).isEqualTo("6.5 ± 0.3 ly");

        assertThat(inLightYear.toUnits(SI.INSTANCE).toString()).isEqualTo("(6.2 ± 0.3)·10¹⁶ m");
        assertThat(inLightYear.toUnits(CGS.INSTANCE).toString()).isEqualTo("(6.2 ± 0.3)·10¹⁸ cm");
        assertThat(inLightYear.toUnits(Planck.INSTANCE).toString()).isEqualTo("(3.82 ± 0.17)·10⁵¹ ℓₚ");
    }

    @Test
    public void prefix() {
        Unit km = m.withPrefix(k);

        Measurement measurementInKm = new Measurement(1, 0.1, km);

        assertThat(measurementInKm.toString()).isEqualTo("1.00 ± 0.10 km");

        Measurement measurementInM = new Measurement(1010, 100, m);

        log.info("{} =? {}", measurementInM, measurementInKm);

        assertThat(measurementInM.equals(measurementInKm)).isTrue();

    }

    @Test
    public void kilogramPrefix() {

        Measurement measurementInKg = new Measurement(1, 0.1, kg);

        assertThat(measurementInKg.toString()).isEqualTo("1.00 ± 0.10 kg");

        Units g = kg.withPrefix(none);
        Measurement measurementInG = new Measurement(1010, 100, g);

        log.info("{} =? {}", measurementInKg, measurementInG);

        assertThat(measurementInKg.equals(measurementInG)).isTrue();

    }

    @Test
    public void lt() {
        PhysicalNumber two_lightyear = new Measurement(2, 0.1, SI.ly);
        PhysicalNumber three_km = new Measurement(3, 0.1, m);
        assertThat(three_km.lt(two_lightyear)).isTrue();
    }

    @Test
    public void equals() {
        PhysicalNumber two_lightyear = new Measurement(2, 0.1, SI.ly);
        PhysicalNumber inm = new Measurement(3, 0.1, m);


    }

    /**
     * Returns only velocities for now.
     *
     */
    @Override
    public Arbitrary<PhysicalNumber> elements() {
        return
            Arbitraries
                .<PhysicalNumber>randomValue(
                    (random) -> new Measurement(
                        random.nextDouble() * 200 - 100,
                        Math.abs(random.nextDouble() * 10),
                        SI.mPerS
                    )
                )
                .injectDuplicates(0.01)
                .dontShrink()
                .edgeCases(config -> {
                    config.add(new Measurement(0, 0.001, SI.mPerS));
                    config.add(PhysicalConstant.c);
                });

    }

    @Override
    public Arbitrary<PhysicalNumber> numbers() {
        return elements();
    }

}
