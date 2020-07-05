package org.meeuw.math;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
class MeasurementTest {

    @Test
    public void add() {
        UncertainNumber door = new Measurement(2.00, 0.03, Units.DISTANCE);
        assertThat(door.toString()).isEqualTo("2.00 ± 0.03 m");
        UncertainNumber knob = new Measurement(0.88, 0.04, Units.DISTANCE);
        UncertainNumber height = door.minus(knob);
        assertThat(height.toString()).isEqualTo("1.12 ± 0.05 m");
    }


    @Test
    public void area() {
        UncertainNumber height = new Measurement(21, 0.2, Units.LENGTH);
        UncertainNumber width = new Measurement(30, 1, Units.LENGTH);
        UncertainNumber area =  height.times(width);
        assertThat(area.toString()).isEqualTo("630 ± 22 m²"); // or should that be 27?
    }


    @Test
    public void illegalAdd() {
        assertThatThrownBy(() -> {
            UncertainNumber door = new Measurement(2.00, 0.03, Units.DISTANCE);
            UncertainNumber knob = new Measurement(0.88, 0.04, Units.AMOUNT_OF_SUBSTANCE);
            UncertainNumber height = door.minus(knob);
        }).isInstanceOf(IllegalArgumentException.class);

    }
    @Test
    public void divide() {
        UncertainNumber speed = new Measurement(6.0, 0.4, Units.SPEED);
        assertThat(speed.toString()).isEqualTo("6.0 ± 0.4 ms⁻¹");
        assertThat(speed.getUnits().dimensions().toString()).isEqualTo("LT⁻¹");

        UncertainNumber distance = new Measurement(2.0, 0.05, Units.DISTANCE);
        UncertainNumber duration = distance.div(speed);
        assertThat(duration.toString()).isEqualTo("0.33 ± 0.02 s");
    }

}
