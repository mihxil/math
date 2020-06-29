package org.meeuw.math.windowed;

import lombok.extern.java.Log;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.meeuw.math.LongMeasurement;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Log
class WindowedLongMeasurementTest {

    @Test
    public void test() throws InterruptedException {
        WindowedLongMeasurement impl = WindowedLongMeasurement
            .builder()
            .mode(LongMeasurement.Mode.INSTANT)
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
