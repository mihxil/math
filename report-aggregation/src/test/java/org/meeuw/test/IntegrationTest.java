package org.meeuw.test;

import org.junit.jupiter.api.Test;

import org.meeuw.math.TestClock;
import org.meeuw.math.windowed.WindowedEventRate;
import org.meeuw.physics.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
public class IntegrationTest {

    @Test
    public void test() {
        TestClock clock = new TestClock();
        WindowedEventRate rate = WindowedEventRate.builder()
            .clock(clock)
            .build();

        rate.newEvent();
        clock.tick();
        rate.newEvent();
        PhysicalNumber measurement = new Measurement(rate);

        PhysicalNumber rateInHours = measurement.toUnits(Units.of(SI.hour).reciprocal());
        assertThat(rateInHours.toString()).isEqualTo("7200 h⁻¹");
        System.out.println("Rate: " + rateInHours);
    }
}
