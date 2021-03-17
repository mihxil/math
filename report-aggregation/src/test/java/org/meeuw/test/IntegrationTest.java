package org.meeuw.test;

import org.junit.jupiter.api.Test;
import org.meeuw.math.TestClock;
import org.meeuw.math.windowed.WindowedEventRate;
import org.meeuw.physics.*;

/**
 * @author Michiel Meeuwissen
 * @since ...
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
        PhysicalNumber measurement = rate.toPhysicalNumber();
        PhysicalNumber rateInHours = measurement.toUnits(Units.of(SI.hours).reciprocal());
        System.out.println("Rate: " + rateInHours);
    }
}
