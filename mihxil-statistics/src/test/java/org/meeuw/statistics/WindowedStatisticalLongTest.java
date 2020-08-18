package org.meeuw.statistics;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;
import org.meeuw.statistics.Windowed.Event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.statistics.Windowed.Event.WINDOW_COMPLETED;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
class WindowedStatisticalLongTest {

    @Test
    public void test() throws InterruptedException {
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
            .build();

        assertThat(impl.getTotalDuration()).isEqualTo(Duration.ofMillis(bucketCount * bucketDuration));
        assertThat(impl.getBucketDuration()).isEqualTo(Duration.ofMillis(bucketDuration));
        events.clear();

        impl.accept(Instant.now());
        Thread.sleep(1);
        impl.accept(Instant.now());
        Thread.sleep(1);
        impl.accept(Instant.now(), Instant.now().plus(Duration.ofMillis(1)));
        Thread.sleep(1);
        impl.accept(Instant.now());
        Thread.sleep(1);
        impl.accept(Instant.now());


        assertThat(impl.getWindowValue().getCount()).isEqualTo(6);
        log.info(() -> "toString: " + impl.getWindowValue().toString());
        Thread.sleep(impl.getStart().plus(impl.getTotalDuration()).toEpochMilli() - System.currentTimeMillis());
        impl.shiftBuckets();
        assertThat(events).hasSize(1);

    }


    @Test
    public void testNormal() throws InterruptedException {
        final int bucketCount = 20;
        final int bucketDuration = 10; // ms
        WindowedStatisticalLong impl = WindowedStatisticalLong
            .builder()
            .bucketCount(bucketCount)
            .bucketDuration(Duration.ofMillis(bucketDuration))
            .mode(StatisticalLong.Mode.LONG)
            .build();


        impl.accept(100L);
        Thread.sleep(1);

        impl.accept(101L, 150L);



        assertThat(impl.getWindowValue().getCount()).isEqualTo(3);
        log.info(() -> "toString: " + impl.getWindowValue().toString());
    }

}
