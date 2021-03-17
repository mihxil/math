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
        assertThat(SI.eV.getDescription()).isEqualTo("electronvolt");
        assertThat(SI.eV.getDimensions().toString()).isEqualTo("L²MT⁻²");
        assertThat(SI.eV.getSIFactor().getValue()).isEqualTo(1.602176634E-19);
    }

    @Test
    public void kmph() {
        assertThat(new DerivedUnit(SI.Prefixes.k, SIUnit.m).toString()).isEqualTo("km");
        assertThat(SI.eV.getDimensions().toString()).isEqualTo("L²MT⁻²");
        assertThat(SI.eV.getSIFactor().getValue()).isEqualTo(1.602176634E-19);
    }

    @Override
    public Arbitrary<Units> elements() {
        return Arbitraries.of(SI.eV, SI.N, SI.eV);
    }
}
