package org.meeuw.physics;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.SIUnit.m;

class PrefixedUnitTest {

    final Units kN = SI.N.withPrefix(SI.DecimalPrefix.k);

    @Test
    void getDimensions() {
        assertThat(kN.getDimensions()).isEqualTo(SI.N.getDimensions());
    }

    @Test
    void iterator() {
        List<UnitExponent> list = new ArrayList<>();
        kN.iterator().forEachRemaining(list::add);
        assertThat(list.toString()).isEqualTo("[kN]");
    }

    @Test
    void getSIFactor() {
        assertThat(kN.getSIFactor()).isEqualTo(UncertainDoubleElement.exactly(1000d));
    }

    @Test
    void reciprocal() {
        Units units = kN.reciprocal();
        assertThat(units.getSIFactor()).isEqualTo(UncertainDoubleElement.exactly(0.001d));
        assertThat(units.toString()).isEqualTo("kN⁻¹");
    }

    @Test
    void times() {
        Units work = kN.times(m);
        assertThat(work.toString()).isEqualTo("kN·m");
    }


}
