package org.meeuw.physics;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class DerivedUnitTest implements MultiplicativeGroupTheory<Units> {

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
        assertThat(SI.eV.getSIFactor().getValue()).isEqualTo(1.602176634E-19);
    }

    @Test
    public void kmph() {
        Units km =  new DerivedUnit(SI.DecimalPrefix.k, SIUnit.m);
        assertThat(km.toString()).isEqualTo("km");

        Units kmph = km.dividedBy(SI.hour);
        assertThat(kmph.toString()).isEqualTo("km");
    }

    @Override
    public Arbitrary<Units> elements() {
        return Arbitraries.of(SI.eV, SI.N, SI.eV);
    }
}
