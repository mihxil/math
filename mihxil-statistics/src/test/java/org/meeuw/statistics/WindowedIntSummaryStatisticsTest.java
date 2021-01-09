package org.meeuw.statistics;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.IntSummaryStatistics;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.meeuw.math.TestClock;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class WindowedIntSummaryStatisticsTest {

    @Test
    public void test() {
        TestClock clock = new TestClock();
        WindowedIntSummaryStatistics instance =
            WindowedIntSummaryStatistics.builder()
                .bucketCount(10)
                .bucketDuration(Duration.ofSeconds(1))
                .clock(clock)
                .build();

        instance.accept(100);
        instance.accept(200);
        clock.sleep(1001L);
        instance.accept(200, 300);
        IntSummaryStatistics[] buckets = instance.getBuckets();
        IntSummaryStatistics combined = instance.getWindowValue();
        assertThat(buckets[buckets.length - 1].getAverage()).isCloseTo(250, Offset.offset(0.001));
        assertThat(buckets[buckets.length - 2].getAverage()).isCloseTo(150, Offset.offset(0.001));

        assertThat(combined.getAverage()).isCloseTo(200, Offset.offset(0.001));
        log.info("ranged: {}", instance.getRanges());

    }
}
