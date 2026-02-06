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

import lombok.extern.java.Log;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.meeuw.math.text.TextUtils;
import org.meeuw.physics.*;
import org.meeuw.physics.SI.DecimalPrefix;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.DoubleElement.exactly;
import static org.meeuw.physics.Dimension.L;
import static org.meeuw.physics.Dimension.T;
import static org.meeuw.physics.Quantity.ENERGY;
import static org.meeuw.physics.Quantity.LUMINOUS_INTENSITY;
import static org.meeuw.physics.SI.BinaryPrefix.Ki;
import static org.meeuw.physics.SI.DecimalPrefix.*;
import static org.meeuw.physics.SI.INSTANCE;
import static org.meeuw.physics.SI.J;
import static org.meeuw.physics.SIUnit.*;
import static org.meeuw.physics.SIUnit.m;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
class SITest {

    @Test
    public void ly() {
        assertThat(SI.ly.toString()).isEqualTo("ly");
        assertThat(SI.ly.asSIConstant().toString()).isEqualTo("9.4607304725808·10¹⁵ m");
    }

    @Test
    public void litre() {
        assertThat(SI.litre.toString()).isEqualTo("l");
        assertThat(SI.litre.asSIConstant().toString()).isEqualTo("0.001 m³");

        Units dm3 = m.withPrefix(d).pow(3);
        Measurement alsoOneLitre = new Measurement(exactly(1), dm3);
        assertThat(alsoOneLitre.eq(SI.litre.asSIConstant())).isTrue();
    }

    @Test
    public void prefixes() {
        prefixes(DecimalPrefix.none);
    }

    @Test
    public void binaryPrefixes() {
         prefixes(SI.BinaryPrefix.none);
    }

    public void prefixes(Prefix startPoint) {
        Optional<? extends Prefix> prefix = startPoint.inc();
        while (prefix.isPresent()) {
            Prefix p = prefix.get();
            log.info("%s: %s (%s)".formatted(p, p.getAsDouble(), p.getPrefixName()));
            log.info("%s . %s = %s".formatted(p,k ,  p.times(k)));
            log.info("%s / %s = %s".formatted( p, k, p.dividedBy(k)));
            log.info(p + " " + TextUtils.superscript(-1) + " = " + p.reciprocal());
            prefix = prefix.get().inc();
        }
        prefix = startPoint.dec();
        while (prefix.isPresent()) {
            Prefix p = prefix.get();
            log.info("%s: %s (%s)".formatted(p, p.getAsDouble(), p.getPrefixName()));
            log.info("%s . %s = %s".formatted(p, k, p.times(k)));
            log.info("%s / %s = %s".formatted( p, k, p.dividedBy(k)));
            log.info(p + " " + TextUtils.superscript(-1) + " = " + p.reciprocal());
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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void prefix() {
        Units kmPerS = m.withPrefix(k).per(s);
        assertThat(kmPerS.toString()).isEqualTo("km·s⁻¹");
        assertThat(kmPerS.getSIFactor().doubleValue()).isEqualTo(1000d);
        assertThat(SI.km.getSIFactor().doubleValue()).isEqualTo(1000d);

        Units MPerS = m.withPrefix(k.times(k).get()).per(s);
        assertThat(MPerS.getSIFactor().doubleValue()).isEqualTo(1_000_000d);

        Units mPerS = m.withPrefix(k.dividedBy(k).get()).per(s);
        assertThat(mPerS.getSIFactor().doubleValue()).isEqualTo(1d);


        assertThat(k.times(Q)).isEmpty();
        assertThat(k.reciprocal().get()).isEqualTo(DecimalPrefix.m);
    }

    @Test
    public void getBaseUnits() {
        assertThat(INSTANCE.getBaseUnits().toString()).isEqualTo("[m, kg, s, A, K, mol, cd]");
    }

    @Test
    public void getUnits() {
        assertThat(INSTANCE.getUnits().stream().map(u -> u.toString() + "\t" + u.getDescription() + "\t" + u.getClass().getSimpleName() )).containsExactly(
            "m	meter	SIUnit",
            "kg	kilogram	SIUnit",
            "s	second	SIUnit",
            "A	ampere	SIUnit",
            "K	kelvin	SIUnit",
            "mol	mole	SIUnit",
            "cd	candela	SIUnit",
            "m·s⁻¹	null	CompositeUnits",
            "km	kilometer	PrefixedUnit",
            "l	litre	DerivedUnit",
            "N	Newton	DerivedUnit",
            "Hz	Hertz	DerivedUnit",
            "Pa	Pascal	DerivedUnit",
            "J	joule	DerivedUnit",
            "eV	electron-volt	DerivedUnit",
            "min	minute	DerivedUnit",
            "h	hour	DerivedUnit",
            "a	(julian) year	DerivedUnit",
            "AU	Astronomical Unit	DerivedUnit",
            "pc	parsec	DerivedUnit",
            "ly	light-year	DerivedUnit",
            "g	gram	DerivedUnit",
            "Da	dalton	DerivedUnit",
            "M☉	Solar mass	DerivedUnit",
            "bit	binary digit	DerivedUnit",
            "Byte	8 bit octet	DerivedUnit"
        );
    }

    @Test
    public  void getForQuantity() {
        assertThat(INSTANCE.forQuantity(LUMINOUS_INTENSITY)).isEqualTo(cd);
        assertThat(INSTANCE.forQuantity(ENERGY)).isEqualTo(J);
    }

    @Test
    public void unitsOf() {
        INSTANCE.getUnits().forEach(i -> {
            String s = i.toString();
            log.info("" + INSTANCE.unitsOf(s));
        });

    }

    @Test
    public void factorsExact(){
        Unit kB = SI.bit.withPrefix(Ki);
        assertThat(kB.getSIFactor().isExact()).isTrue();
        assertThat(kB.getSIFactor().doubleValue()).isEqualTo(1024d);

    }


}
