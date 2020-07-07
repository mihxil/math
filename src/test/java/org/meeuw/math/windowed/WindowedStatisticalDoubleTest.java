package org.meeuw.math.windowed;

import lombok.extern.java.Log;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.meeuw.math.StatisticalDouble;
import org.meeuw.math.UncertainNumber;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Log
class WindowedStatisticalDoubleTest {

    @Test
    public void test() throws InterruptedException {
        WindowedStatisticalDouble impl = WindowedStatisticalDouble
            .builder()
            .window(Duration.ofMillis(10))
            .bucketCount(10)
            .build();

        impl.accept(0.1, 0.2);
        Thread.sleep(1);
        impl.accept(0.2, 0.21);
        Thread.sleep(1);
        impl.accept(0.19);
        Thread.sleep(1);
        impl.accept(0.22, 0.23);
        Thread.sleep(1);
        impl.accept(0.24);
        StatisticalDouble windowValue = impl.getWindowValue();
        assertThat(windowValue.getCount()).isEqualTo(8);
        UncertainNumber uncertainNumber = windowValue.measurementCopy();
        assertThat(windowValue.toString()).isEqualTo("0.20 ± 0.04");
        assertThat(uncertainNumber.toString()).isEqualTo("0.20 ± 0.04");
    }

}
