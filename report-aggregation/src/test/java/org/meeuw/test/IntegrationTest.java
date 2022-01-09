package org.meeuw.test;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

import org.meeuw.math.TestClock;
import org.meeuw.math.statistics.StatisticalDouble;
import org.meeuw.math.windowed.WindowedEventRate;
import org.meeuw.physics.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
public class IntegrationTest {

    @Test
    public void windowedEventRateToPhysics() throws UnsupportedEncodingException {
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
        PrintStream out = new PrintStream(System.out, true, "UTF-8");

        out.println("Rate: " + rateInHours + " h⁻¹ " + Charset.defaultCharset() + " " + System.getProperty("file.encoding"));
    }

    @Test
    public void statisticalDoubleToPhysics() {

        StatisticalDouble statisticalDouble = new StatisticalDouble();
        statisticalDouble.enter(10d, 11d, 9d);

        PhysicalNumber measurement = new Measurement(statisticalDouble, Units.of(SI.min));

        assertThat(measurement.toUnits(Units.of(SIUnit.s)).toString()).isEqualTo("600 ± 45 s");
    }
}
