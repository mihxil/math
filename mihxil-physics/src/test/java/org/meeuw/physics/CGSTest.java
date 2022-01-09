package org.meeuw.physics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.physics.CGS.INSTANCE;
import static org.meeuw.physics.Quantity.ACCELERATION;
import static org.meeuw.physics.SIUnit.m;
import static org.meeuw.physics.SIUnit.s;

class CGSTest {

    @Test
    void cm() {
        assertThat(CGS.CGSUnit.cm.getSIFactor().getValue()).isEqualTo(0.01d);
        assertThat(CGS.cmPerS.getSIFactor().getValue()).isEqualTo(0.01d);
    }

    @Test
    void velocity() {
        PhysicalNumber slow = new Measurement(2, 0.1, CGS.cmPerS);
        assertThat(slow.toString()).isEqualTo("2.00 ± 0.10 cm·s⁻¹");

        PhysicalNumber inSI = slow.toUnits(SI.INSTANCE);
        assertThat(inSI.toString()).isEqualTo("0.0200 ± 0.0010 m·s⁻¹");
    }

    @Test
    void acceleration() {
        Units gal = INSTANCE.forQuantity(ACCELERATION);
        assertThat(gal).isSameAs(CGS.Gal);
        assertThat(gal.getSIFactor().getValue()).isEqualTo(0.01d);

        PhysicalNumber acc = new Measurement(1, 0.1, gal);
        Units mPerS2 = SI.INSTANCE.forQuantity(ACCELERATION);

        assertThat(mPerS2.equals(gal)).isFalse();

        assertThat(mPerS2).isEqualTo(m.per(s.sqr()));
        assertThat(acc.toUnits(mPerS2).toString()).isEqualTo("0.0100 ± 0.0009 m·s⁻²");

    }

}
