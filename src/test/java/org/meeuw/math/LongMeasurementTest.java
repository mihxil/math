package org.meeuw.math;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
class LongMeasurementTest {

    @Test
    public void instants() {
        Instant now = Instant.now();
        String expected = now.truncatedTo(ChronoUnit.SECONDS).toString();
        LongMeasurement mes = new LongMeasurement(LongMeasurement.Mode.INSTANT);
        mes.enter(now, now.plus(Duration.ofSeconds(1)), now.minus(Duration.ofMillis(500)));
        assertEquals(expected, mes.toString());
    }

}
