package org.meeuw.math.windowed;

import lombok.extern.java.Log;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.meeuw.math.LongStatisticalMeasurement;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Log
class WindowedLongStatisticalMeasurementTest {

    @Test
    public void test() throws InterruptedException {
        WindowedLongStatisticalMeasurement impl = WindowedLongStatisticalMeasurement
            .builder()
            .mode(LongStatisticalMeasurement.Mode.INSTANT)
            .build();

        impl.accept(Instant.now());
        Thread.sleep(1);
        impl.accept(Instant.now());
        Thread.sleep(1);
        impl.accept(Instant.now());
        Thread.sleep(1);
        impl.accept(Instant.now());
        Thread.sleep(1);
        impl.accept(Instant.now());

        log.info(() -> impl.getWindowValue().toString());


    }

}
