package org.meeuw.physics;

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
        Measurement door = new Measurement(2.00, 0.03, SI.DISTANCE);
        assertThat(door.toString()).isEqualTo("2.00 ± 0.03 m");
        Measurement knob = new Measurement(0.88, 0.04, SI.DISTANCE);
        Measurement height = door.minus(knob);
        assertThat(height.toString()).isEqualTo("1.12 ± 0.05 m");
    }


    @Test
    public void area() {
        Measurement height = new Measurement(21, 0.2, SI.LENGTH);
        Measurement width = new Measurement(30, 1, SI.LENGTH);
        Measurement area =  height.times(width);
        assertThat(area.toString()).isEqualTo("630 ± 22 m²"); // or should that be 27?
    }


    @Test
    public void illegalAdd() {
        assertThatThrownBy(() -> {
            Measurement door = new Measurement(2.00, 0.03, SI.DISTANCE);
            Measurement knob = new Measurement(0.88, 0.04, SI.AMOUNT_OF_SUBSTANCE);
            Measurement height = door.minus(knob);
        }).isInstanceOf(IllegalArgumentException.class);

    }
    @Test
    public void divide() {
        Measurement speed = new Measurement(6.0, 0.4, SI.SPEED);
        assertThat(speed.toString()).isEqualTo("6.0 ± 0.4 m·s⁻¹");
        assertThat(speed.getUnits().dimensions().toString()).isEqualTo("LT⁻¹");

        Measurement distance = new Measurement(2.0, 0.05, SI.DISTANCE);
        Measurement duration = distance.dividedBy(speed);
        assertThat(duration.toString()).isEqualTo("0.33 ± 0.02 s");
    }

    @Test
    public void structure() {
        Measurement a = new Measurement(6.0, 0.4, SI.SPEED);
        assertThat(a.plus(a.structure().zero())).isEqualTo(a);
        assertThat(a.times(a.structure().one())).isEqualTo(a);

        assertThat(a.times(2d)).isEqualTo(new Measurement(12.0, 0.8, SI.SPEED));

        // FAILS
        // And btw, repetive addition should handle uncertaintities more like times(double)
//        assertThat(a.repeatedPlus(2)).isEqualTo(new Measurement(12.0, 0.6, SI.SPEED));

    }

}
