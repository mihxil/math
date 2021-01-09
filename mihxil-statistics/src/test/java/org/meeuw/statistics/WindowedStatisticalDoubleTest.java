package org.meeuw.statistics;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
class WindowedStatisticalDoubleTest {

    @Test
    public void test() {
        TestClock clock = new TestClock();

        WindowedStatisticalDouble impl = WindowedStatisticalDouble
            .builder()
            .bucketDuration(Duration.ofMillis(4))
            .bucketCount(30)
            .clock(clock)
            .build();

        impl.accept(0.1, 0.2);
        clock.sleep(1);
        impl.accept(0.2, 0.21);
        clock.sleep(1);
        impl.accept(0.19);
        clock.sleep(1);
        impl.accept(0.22, 0.23);
        clock.sleep(1);
        impl.accept(0.24);
        log.info(() -> String.valueOf(impl));
        StatisticalDouble windowValue = impl.getWindowValue();
        UncertainDoubleElement uncertainNumber = windowValue.immutableCopy();
        assertThat(windowValue.toString()).isEqualTo("0.20 ± 0.04");
        assertThat(uncertainNumber.toString()).isEqualTo("0.20 ± 0.04");
        assertThat(windowValue.getCount()).isEqualTo(8);

    }

}
