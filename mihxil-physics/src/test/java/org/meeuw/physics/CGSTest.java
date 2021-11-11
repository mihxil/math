package org.meeuw.physics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CGSTest {

    @Test
    void velocity() {
        PhysicalNumber slow = new Measurement(2, 0.1, CGS.INSTANCE.forDimensions(DimensionalAnalysis.SPEED));
        assertThat(slow.toString()).isEqualTo("2.00 ± 0.10 cm·s⁻¹");

        PhysicalNumber inSI = slow.toUnits(SI.INSTANCE);
        assertThat(inSI.toString()).isEqualTo("0.0200 ± 0.0010 m·s⁻¹");
    }

}
