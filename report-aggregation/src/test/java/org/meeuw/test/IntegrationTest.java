/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import org.meeuw.math.TestClock;
import org.meeuw.math.statistics.StatisticalDoubleImpl;
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
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.println("Rate: " + rateInHours + " h⁻¹ " + Charset.defaultCharset() + " " + System.getProperty("file.encoding"));
    }

    @Test
    public void statisticalDoubleToPhysics() {

        StatisticalDoubleImpl statisticalDouble = new StatisticalDoubleImpl();
        statisticalDouble.enter(10d, 11d, 9d);

        PhysicalNumber measurement = new Measurement(statisticalDouble, Units.of(SI.min));

        assertThat(measurement.toUnits(Units.of(SIUnit.s)).toString()).isEqualTo("600 ± 45 s");
    }
}
