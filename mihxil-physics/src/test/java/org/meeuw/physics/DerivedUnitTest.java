package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.SI.DecimalPrefix.k;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
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
        Units km =   SIUnit.m.withPrefix(k);
        assertThat(km.toString()).isEqualTo("km");

        Units kmph = km.dividedBy(SI.hour).withName("km/h");
        assertThat(kmph.toString()).isEqualTo("km/h");
        assertThat(kmph.getDimensions()).isEqualTo(DimensionalAnalysis.SPEED);

        PhysicalNumber n = new Measurement(10d, 1d, kmph);

        PhysicalNumber mps = n.toUnits(SI.INSTANCE);

        log.info("{} -> {}", n, mps);
        assertThat(mps.toString()).isEqualTo("2.8 ± 0.3 m·s⁻¹");

        assertThat(mps).isEqualTo(n.toUnits(SI.INSTANCE));

        assertThat(n.toUnits(Planck.INSTANCE).toString()).isEqualTo("(9.3 ± 0.8)·10⁻⁹ ℓₚ·tₚ⁻¹");
    }

    @Override
    public Arbitrary<Units> elements() {
        return Arbitraries.of(SI.eV, SI.N, SI.eV);
    }
}
