package org.meeuw.math.windowed;

import lombok.extern.java.Log;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.meeuw.math.StatisticalLong;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Log
class WindowedStatisticalLongTest {

    @Test
    public void test() throws InterruptedException {
        WindowedStatisticalLong impl = WindowedStatisticalLong
            .builder()
            .mode(StatisticalLong.Mode.INSTANT)
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
