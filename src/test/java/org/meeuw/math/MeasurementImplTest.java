package org.meeuw.math;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
class MeasurementImplTest {

    @Test
    public void add() {
        Measurement door = new MeasurementImpl(2.00, 0.03, Units.DISTANCE);
        assertThat(door.toString()).isEqualTo("2.00 ± 0.03 m");
        Measurement knob = new MeasurementImpl(0.88, 0.04, Units.DISTANCE);
        Measurement height = door.minus(knob);
        assertThat(height.toString()).isEqualTo("1.12 ± 0.05 m");
    }


    @Test
    public void illegalAdd() {
        assertThatThrownBy(() -> {
            Measurement door = new MeasurementImpl(2.00, 0.03, Units.DISTANCE);
            Measurement knob = new MeasurementImpl(0.88, 0.04, Units.AMOUNT_OF_SUBSTANCE);
            Measurement height = door.minus(knob);
        }).isInstanceOf(IllegalArgumentException.class);

    }
    @Test
    public void divide() {
        Measurement speed = new MeasurementImpl(6.0, 0.4, Units.SPEED);
        assertThat(speed.toString()).isEqualTo("6.0 ± 0.4 ms⁻¹");

        Measurement distance = new MeasurementImpl(2.0, 0.05, Units.DISTANCE);
        Measurement duration = distance.dividedBy(speed);
        assertThat(duration.toString()).isEqualTo("0.33 ± 0.07 s");
    }

}
