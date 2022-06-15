package org.meeuw.physics;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.physics.Measurement.measurement;
import static org.meeuw.physics.SI.BinaryPrefix.Ki;
import static org.meeuw.physics.SI.hour;
import static org.meeuw.physics.SI.octet;
import static org.meeuw.physics.SIUnit.s;

public class DigitalInformationTest {


    @Test
    public void kB() {
        assertThat(SI.bit.getSIFactor()).isEqualTo(exactly(1));
        Unit kB = SI.bit.withPrefix(Ki);

        PhysicalNumber thousandKiB = measurement(exactly(1000), kB);

        assertThat(thousandKiB.toString()).isEqualTo("1000 Kibit");

        PhysicalNumber inBytes = thousandKiB.toUnits(octet.withPrefix(Ki));
        assertThat(inBytes.toString()).isEqualTo("125 KiByte");

        PhysicalNumber speed = thousandKiB.dividedBy(measurement(2d, 0.1d, s));

        assertThat(speed.toString()).isEqualTo("500 ± 24 Kibit·s⁻¹");
        assertThat(inBytes.dividedBy(measurement(2d, 0.1d, hour)).toString()).isEqualTo("62 ± 3 KiByte·h⁻¹");
    }
}
