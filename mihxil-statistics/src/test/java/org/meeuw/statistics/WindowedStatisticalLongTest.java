package org.meeuw.statistics;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;
import org.meeuw.math.TestClock;
import org.meeuw.statistics.Windowed.Event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.statistics.Windowed.Event.WINDOW_COMPLETED;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
class WindowedStatisticalLongTest {

    @Test
    public void test() {
        TestClock clock = new TestClock();
        final List<Event> events = new ArrayList<>();
        BiConsumer<Event , Windowed<StatisticalLong>> listener = (e, l) -> {
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
            .mode(StatisticalLong.Mode.INSTANT)
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


        assertThat(impl.getWindowValue().getCount()).isEqualTo(6);
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

}
