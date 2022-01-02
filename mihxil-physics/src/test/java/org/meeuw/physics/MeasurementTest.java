package org.meeuw.physics;

import org.junit.jupiter.api.Test;

import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.configuration.ConfigurationService.with;
import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.PARENTHESES;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class MeasurementTest  {
    Units DISTANCE = SI.INSTANCE.forDimensions(DimensionalAnalysis.DISTANCE);
    Units LENGTH = SI.INSTANCE.forDimensions(DimensionalAnalysis.LENGTH);
    @Test
    public void add() {
        Measurement door = new Measurement(2.00, 0.03, DISTANCE);
        assertThat(door.toString()).isEqualTo("2.00 ± 0.03 m");
        Measurement knob = new Measurement(0.88, 0.04, DISTANCE);
        PhysicalNumber height = door.minus(knob);
        assertThat(height.toString()).isEqualTo("1.12 ± 0.07 m");
    }

    @Test
    public void area() {
        Measurement height = new Measurement(21, 0.2, LENGTH);
        Measurement width = new Measurement(30, 1, LENGTH);
        PhysicalNumber area =  height.times(width);
        assertThat(area.toString()).isEqualTo("630 ± 21 m²"); // or should that be 27?
        with(UncertaintyConfiguration.class, (ub) -> ub.withNotation(PARENTHESES),
            () -> assertThat(area.toString()).isEqualTo("630(21) m²")
        );
    }

    @Test
    public void illegalAdd() {
        assertThatThrownBy(() -> {
            Measurement door = new Measurement(2.00, 0.03, SI.DISTANCE);
            Measurement knob = new Measurement(0.88, 0.04, SI.AMOUNT_OF_SUBSTANCE);
            PhysicalNumber height = door.minus(knob);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void divide() {
        Measurement speed = new Measurement(6.0, 0.4, SI.VELOCITY);
        assertThat(speed.toString()).isEqualTo("6.0 ± 0.4 m·s⁻¹");
        assertThat(speed.getUnits().getDimensions().toString()).isEqualTo("LT⁻¹");

        Measurement distance = new Measurement(2.0, 0.05, SI.DISTANCE);
        PhysicalNumber duration = distance.dividedBy(speed);
        assertThat(duration.toString()).isEqualTo("0.33 ± 0.02 s");
    }

    @Test
    public void testStructure() {
        Measurement a = new Measurement(6.0, 0.4, SI.VELOCITY);
        assertThat(a.plus(a.getUnits().zero())).isEqualTo(a);
        assertThat(a.times(a.getStructure().one())).isEqualTo(a);

        assertThat(a.times(2d)).isEqualTo(new Measurement(12.0, 0.8, SI.VELOCITY));

        // FAILS
        // And btw, repetive addition should handle uncertaintities more like times(double)
//        assertThat(a.repeatedPlus(2)).isEqualTo(new Measurement(12.0, 0.6, SI.SPEED));
    }

}
