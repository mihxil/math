package org.meeuw.physics;

import lombok.extern.log4j.Log4j2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.of;
import static org.meeuw.physics.SI.DecimalPrefix.k;
import static org.meeuw.physics.SIUnit.m;

@Log4j2
class UnitedStatesCustomaryUnitsTest {


    @Test
    void inchCmYd() {
        PhysicalNumber oneInch = new Measurement(of(1, 0.01), UnitedStatesCustomaryUnits.in);
        PhysicalNumber inCm = oneInch.toUnits(m.withPrefix(SI.DecimalPrefix.c));
        assertThat(inCm.toString()).isEqualTo("2.54 ± 0.03 cm");

        assertThat(oneInch.equals(inCm)).isTrue();

        PhysicalNumber inYd = inCm.toUnits(UnitedStatesCustomaryUnits.INSTANCE.forDimension(Dimension.L));

        assertThat(inYd.equals(inCm)).isTrue();
        assertThat(inYd.toString()).isEqualTo("0.0278 ± 0.0003 yd");

        assertThat(UnitedStatesCustomaryUnits.US.yd.reciprocal().toString()).isEqualTo("/yd");
    }

    @Test
    void mileKm() {
        PhysicalNumber twoMile = new Measurement(of(2, 0.01), UnitedStatesCustomaryUnits.mi);
        PhysicalNumber inKm = twoMile.toUnits(m.withPrefix(k));
        assertThat(inKm.toString()).isEqualTo("3.219 ± 0.016 km");
    }

    @Test
    void newton() {

    }


}
