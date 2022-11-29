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

import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.physics.Dimension.L;
import static org.meeuw.physics.Dimension.T;
import static org.meeuw.physics.SI.DecimalPrefix.d;
import static org.meeuw.physics.SI.DecimalPrefix.k;
import static org.meeuw.physics.SI.INSTANCE;
import static org.meeuw.physics.SIUnit.m;
import static org.meeuw.physics.SIUnit.s;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class SITest {

    @Test
    public void ly() {
        assertThat(SI.ly.toString()).isEqualTo("ly");
        assertThat(SI.ly.asSIConstant().toString()).isEqualTo("9.4607304725808·10¹⁵ m");
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void litre() {
        assertThat(SI.litre.toString()).isEqualTo("l");
        assertThat(SI.litre.asSIConstant().toString()).isEqualTo("0.001 m³");

        Units dm3 = m.withPrefix(d).pow(3);
        Measurement alsoOneLitre = new Measurement(exactly(1), dm3);
        assertThat(alsoOneLitre.equals(SI.litre.asSIConstant())).isTrue();

    }

    @Test
    public void prefixes() {
        prefixes(SI.DecimalPrefix.none);
    }

    @Test
    public void binaryPrefixes() {
         prefixes(SI.BinaryPrefix.none);
    }

    public void prefixes(Prefix startPoint) {
        Optional<? extends Prefix> prefix = startPoint.inc();
        while (prefix.isPresent()) {
            log.info("{}: {} ({})", prefix.get(), prefix.get().getAsDouble(), prefix.get().getPrefixName());
            prefix = prefix.get().inc();
        }
        prefix = startPoint.dec();
        while (prefix.isPresent()) {
            log.info("{}: {} ({})", prefix.get(), prefix.get().getAsDouble(), prefix.get().getPrefixName());
            prefix = prefix.get().dec();
        }
    }

    @Test
    public void forDimension() {
        assertThat((Object) INSTANCE.forDimension(T)).isEqualTo(s);
    }

    @Test
    public void forDimensions() {
        assertThat(INSTANCE.forDimensions(L, T.with(-1)).toString()).isEqualTo("m·s⁻¹");
        assertThat(INSTANCE.forQuantity(Quantity.FORCE).toString()).isEqualTo("N");
    }

    @Test
    public void prefix() {
        Units kmPerS = m.withPrefix(k).per(s);
        assertThat(kmPerS.toString()).isEqualTo("km·s⁻¹");
        assertThat(kmPerS.getSIFactor().doubleValue()).isEqualTo(1000d);
        assertThat(SI.km.getSIFactor().doubleValue()).isEqualTo(1000d);
    }

    @Test
    public void getBaseUnits() {
        assertThat(INSTANCE.getBaseUnits().toString()).isEqualTo("[m, kg, s, A, K, mol, cd]");
    }

    @Test
    public void getUnits() {
        assertThat(INSTANCE.getUnits().toString()).isEqualTo(
            "[m, kg, s, A, K, mol, cd, m·s⁻¹, km, l, N, g, Hz, Pa, J, min, h, eV, AU, pc, ly, Da, bit, Byte]"
        );
    }

    @Test
    public  void getForQuantity() {
        assertThat(INSTANCE.forQuantity(Quantity.LUMINOUS_INTENSITY)).isEqualTo(SIUnit.cd);
        assertThat(INSTANCE.forQuantity(Quantity.ENERGY)).isEqualTo(SI.J);
    }


}
