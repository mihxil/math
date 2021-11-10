package org.meeuw.physics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlanckTest {

    @Test
    public void c() {
        assertThat(Planck.c.toString()).isEqualTo("1 ℓₚ·tₚ⁻¹");
        Units units = Planck.c.getUnits();
        assertThat(units.toString()).isEqualTo("ℓₚ·tₚ⁻¹");

        PhysicalNumber cSI = Planck.c.toUnits(SI.INSTANCE);
        assertThat(cSI.toString()).isEqualTo("(2.99792 ± 0.00005)·10⁸ m·s⁻¹");

        assertThat(cSI.toUnits(Planck.INSTANCE).toString()).isEqualTo("1.00000 ± 0.00002 ℓₚ·tₚ⁻¹");

    }

    @Test
    public void kB() {
        assertThat(Planck.kB.toString()).isEqualTo("1 ℓₚ·tₚ⁻¹");

    }

}
