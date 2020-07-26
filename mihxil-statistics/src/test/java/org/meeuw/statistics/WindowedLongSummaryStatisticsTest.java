package org.meeuw.statistics;

import lombok.extern.java.Log;

import java.time.Duration;
import java.util.LongSummaryStatistics;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 1.66
 */
@Log
public class WindowedLongSummaryStatisticsTest {

    @Test
    public void test() throws InterruptedException {
        WindowedLongSummaryStatistics instance =
            WindowedLongSummaryStatistics.builder()
                .bucketCount(10)
                .bucketDuration(Duration.ofSeconds(1))
                .build();

        instance.accept(100L, 200L);
        Thread.sleep(1000L);
        instance.accept(200L, 300L);
        LongSummaryStatistics[] buckets = instance.getBuckets();
        LongSummaryStatistics combined = instance.getWindowValue();
        assertThat(buckets[buckets.length - 1].getAverage()).isCloseTo(250, Offset.offset(0.001));
        assertThat(buckets[buckets.length - 2].getAverage()).isCloseTo(150, Offset.offset(0.001));

        assertThat(combined.getAverage()).isCloseTo(200, Offset.offset(0.001));
        System.out.println(instance.getRanges());
    }

}
