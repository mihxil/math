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

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;

import org.meeuw.math.TestClock;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.statistics.StatisticalLong;
import org.meeuw.math.temporal.UncertainTemporal;
import org.meeuw.math.windowed.Windowed.Event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.windowed.Windowed.Event.WINDOW_COMPLETED;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
class WindowedStatisticalLongTest {

    @Test
    public void test() {
        TestClock clock = new TestClock(ZoneId.of("Europe/Amsterdam"), Instant.parse("2022-10-23T09:00:00Z"));
        final List<Event> events = new ArrayList<>();
        BiConsumer<Event, Windowed<StatisticalLong>> listener = (e, l) -> {
            if (e == WINDOW_COMPLETED) {
                log.info("Event on: {} {}", e, l);
                synchronized (events) {
                    events.add(e);
                    events.notifyAll();
                }
            }
        };
        final int bucketCount = 20;
        final int bucketDuration = 10; // ms
        WindowedStatisticalLong impl = WindowedStatisticalLong
            .builder()
            .bucketCount(bucketCount)
            .bucketDuration(Duration.ofMillis(bucketDuration))
            .mode(UncertainTemporal.Mode.INSTANT)
            .eventListeners(listener)
            .clock(clock)
            .build();

        assertThat(impl.getTotalDuration()).isEqualTo(Duration.ofMillis(bucketCount * bucketDuration));
        assertThat(impl.getBucketDuration()).isEqualTo(Duration.ofMillis(bucketDuration));
        events.clear();

        impl.accept(clock.instant());
        clock.sleep(1);
        impl.accept(clock.instant());
        clock.sleep(1);
        impl.accept(clock.instant(), clock.instant().plus(Duration.ofMillis(1)));
        clock.sleep(1);
        impl.accept(clock.instant());
        clock.sleep(1);
        impl.accept(clock.instant());
        clock.tick(Duration.ofNanos(1_000L));
        impl.accept(clock.instant());
        assertThat(impl.getWindowValue().instantValue()).isEqualTo("2022-10-23T09:00:00.002428570Z");
        assertThat(impl.getWindowValue().getCount()).isEqualTo(7);
        log.info(() -> "toString: " + impl.getWindowValue().toString());
        clock.sleep(impl.getStart().plus(impl.getTotalDuration()).toEpochMilli() - clock.millis() + 1);
        impl.shiftBuckets();
        assertThat(events).hasSize(1);

    }


    @Test
    public void testNormal() {
        final int bucketCount = 20;
        final int bucketDuration = 10; // ms
        TestClock clock = new TestClock();

        WindowedStatisticalLong impl = WindowedStatisticalLong
            .builder()
            .bucketCount(bucketCount)
            .bucketDuration(Duration.ofMillis(bucketDuration))
            .clock(clock)
            .build();


        impl.accept(100L);
        clock.sleep(1);

        impl.accept(101L, 150L);

        assertThat(impl.getWindowValue().getCount()).isEqualTo(3);
        log.info(() -> "toString: " + impl.getWindowValue().toString());
    }

    @Test
    public void testDuration() {
        TestClock clock = new TestClock();

        try (WindowedStatisticalLong impl = WindowedStatisticalLong
            .builder()
            .bucketDuration(Duration.ofMillis(4))
            .bucketCount(30)
            .clock(clock)
            .mode(UncertainTemporal.Mode.DURATION)
            .build()) {

            assertThatThrownBy(() ->
                impl.getWindowValue().durationValue()
            ).isInstanceOf(DivisionByZeroException.class);

            try (WindowedStatisticalLong.RunningDuration measure = impl.measure()) {


                clock.tick(10);

                assertThat(impl.getWindowValue().durationValue()).isEqualTo(Duration.ofMillis(10));

                clock.tick(10);
            }
            assertThat(impl.getWindowValue().durationValue()).isEqualTo(Duration.ofMillis(20));
            assertThat(impl.getRunningDurations()).hasSize(0);
        }

    }
}
