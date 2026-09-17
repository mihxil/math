package org.meeuw.test.physics;

import org.junit.jupiter.api.Test;

import org.meeuw.jupiter.WithUncertaintyConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.physics.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.assertj.Assertions.assertThatAlgebraically;
import static org.meeuw.math.abstractalgebra.reals.DoubleElement.exactly;
import static org.meeuw.physics.Measurement.measurement;
import static org.meeuw.physics.SI.BinaryPrefix.Ki;
import static org.meeuw.physics.SI.hour;
import static org.meeuw.physics.SI.octet;
import static org.meeuw.physics.SIUnit.s;

@WithUncertaintyConfiguration(notation = UncertaintyConfiguration.Notation.PLUS_MINUS, stripZeros = true)
public class DigitalInformationTest {


    @Test
    void kB() {
        assertThat(SI.bit.getSIFactor()).isEqualTo(exactly(1));
        Unit kB = SI.bit.withPrefix(Ki);

        PhysicalNumber thousandKiB = measurement(exactly(1000), kB);
        assertThat(thousandKiB.isExact()).isTrue();
        assertThat(octet.getSIFactor().isExact()).isTrue();

        assertThat(thousandKiB.toString()).isEqualTo("1000 Kibit");

        PhysicalNumber inBytes = thousandKiB.toUnits(octet.withPrefix(Ki));
        assertThatAlgebraically(inBytes).isExact();
        assertThat(inBytes.toString()).isEqualTo("125 KiByte");

        PhysicalNumber speed = thousandKiB.dividedBy(measurement(2d, 0.1d, s));

        assertThat(speed.toString()).isEqualTo("500 ± 25 Kibit·s⁻¹");
        assertThat(inBytes.dividedBy(measurement(2d, 0.1d, hour)).toString()).isEqualTo("62 ± 3 KiByte·h⁻¹");
    }
}
