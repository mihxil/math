package org.meeuw.math.windowed;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.DoubleSummaryStatistics;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.meeuw.math.TestClock;
import org.meeuw.math.windowed.WindowedDoubleSummaryStatistics;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class WindowedDoubleSummaryStatisticsTest {

    @Test
    public void test() {
        TestClock clock = new TestClock();
        WindowedDoubleSummaryStatistics instance =
            WindowedDoubleSummaryStatistics.builder()
                .bucketCount(10)
                .bucketDuration(Duration.ofSeconds(1))
                .clock(clock)
                .build();

        instance.accept(100d);
        instance.accept(200d);
        clock.sleep(1001L);
        instance.accept(200d, 300d);
        DoubleSummaryStatistics[] buckets = instance.getBuckets();
        DoubleSummaryStatistics combined = instance.getWindowValue();

        assertThat(buckets[buckets.length - 1].getAverage())
            .isCloseTo(250, Offset.offset(0.001));
        assertThat(buckets[buckets.length - 2].getAverage()).isCloseTo(150, Offset.offset(0.001));

        assertThat(combined.getAverage()).isCloseTo(200, Offset.offset(0.001));


        log.info(instance.getRanges());
    }

}
