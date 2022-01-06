package org.meeuw.physics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.Quantity.ACCELERATION;
import static org.meeuw.physics.Quantity.SPEED;

class CGSTest {

    @Test
    void velocity() {
        PhysicalNumber slow = new Measurement(2, 0.1, CGS.INSTANCE.forQuantity(SPEED));
        assertThat(slow.toString()).isEqualTo("2.00 ± 0.10 cm·s⁻¹");

        PhysicalNumber inSI = slow.toUnits(SI.INSTANCE);
        assertThat(inSI.toString()).isEqualTo("0.0200 ± 0.0010 m·s⁻¹");
    }

    @Test
    void acceleration() {
        Units gal = CGS.INSTANCE.forQuantity(ACCELERATION);
        assertThat(gal).isSameAs(CGS.Gal);

        PhysicalNumber acc = new Measurement(1, 0.1, gal);
        assertThat(acc.toUnits(SI.INSTANCE.forQuantity(ACCELERATION)).toString()).isEqualTo("1.00 ± 0.10 cm.s-2");

    }

}
