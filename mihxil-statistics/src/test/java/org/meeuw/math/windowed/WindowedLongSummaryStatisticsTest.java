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
package org.meeuw.math.windowed;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.LongSummaryStatistics;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.meeuw.time.TestClock;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 1.66
 */
@Log4j2
public class WindowedLongSummaryStatisticsTest {

    @Test
    public void test() {
        TestClock clock = new TestClock();

        WindowedLongSummaryStatistics instance =
            WindowedLongSummaryStatistics.builder()
                .bucketCount(10)
                .bucketDuration(Duration.ofSeconds(1))
                .clock(clock)
                .build();

        instance.accept(100L);
        instance.accept(200L);
        clock.sleep(1001L);
        instance.accept(200L, 300L);
        LongSummaryStatistics[] buckets = instance.getBuckets();
        LongSummaryStatistics combined = instance.getWindowValue();
        assertThat(buckets[buckets.length - 1].getAverage()).isCloseTo(250, Offset.offset(0.001));
        assertThat(buckets[buckets.length - 2].getAverage()).isCloseTo(150, Offset.offset(0.001));

        assertThat(combined.getAverage()).isCloseTo(200, Offset.offset(0.001));
        log.info(instance.getRanges());
    }

}
