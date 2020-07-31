package org.meeuw.physics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
class DerivedUnitTest {

    @Test
    public void N() {
        assertThat(SI.N.toString()).isEqualTo("N");
        assertThat(SI.N.getDimensions().toString()).isEqualTo("LMT⁻²");
    }

    @Test
    public void eV() {
        assertThat(SI.eV.toString()).isEqualTo("eV");
        assertThat(SI.eV.getDimensions().toString()).isEqualTo("L²MT⁻²");
        assertThat(SI.eV.getSiFactor()).isEqualTo(1.602176634E-19);
    }

    @Test
    public void kmph() {
        assertThat(new DerivedUnit(SI.Prefixes.k, SIUnit.m));
        assertThat(SI.eV.getDimensions().toString()).isEqualTo("L²MT⁻²");
        assertThat(SI.eV.getSiFactor()).isEqualTo(1.602176634E-19);
    }
}
