package org.meeuw.statistics;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.meeuw.math.Interval;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

/**
 * @author Michiel Meeuwissen
 * @since 0.38
 */
@Log4j2
public class WindowedEventRateTest {


    @Test
    public void testBuckets() {
        final TestClock clock = new TestClock();
        WindowedEventRate rate = WindowedEventRate.builder()
            .bucketCount(5)
            .clock(clock)
            .bucketDuration(Duration.ofSeconds(1))
            .build();

        assertThat(rate.getBucketDuration().toMillis()).isEqualTo(1000);
        rate.newEvent();
        clock.tick(1001);
        rate.newEvents(2);

        AtomicLong[] buckets = rate.getBuckets();
        assertThat(buckets[buckets.length - 1].get()).isEqualTo(2); // current bucket
        assertThat(buckets[buckets.length - 2].get()).isEqualTo(1); // one bucketDuration ago
        assertThat(buckets[buckets.length - 3].get()).isEqualTo(0); // longer ago

        List<Map.Entry<Interval<Instant>, AtomicLong>> ranges =
            new ArrayList<>(rate.getRanges().entrySet());
        assertThat(ranges.get(buckets.length - 1).getValue().longValue()).isEqualTo(2); // current bucket
        assertThat(ranges.get(buckets.length - 2).getValue().longValue()).isEqualTo(1); // one buckedDuration ago
        assertThat(ranges.get(buckets.length - 3).getValue().longValue()).isEqualTo(0); // longer ago

        assertThat(ranges.get(buckets.length - 2).getKey()
            .lowerEndpoint()).isEqualTo(rate.getStart()); // This bucket was the first one

        for (int i = 0; i < ranges.size() - 1; i++) {
            assertThat(ranges.get(i).getKey().lowerEndpoint())
                .isBeforeOrEqualTo(ranges.get(i + 1).getKey().lowerEndpoint());
            assertThat(ranges.get(i).getKey().upperEndpoint())
                .isEqualTo(ranges.get(i + 1).getKey().lowerEndpoint());
        }


    }


    @Test
    public void test() {
        final List<Double> consumer = new ArrayList<>();
        final List<Windowed.Event> eventListeners = new ArrayList<>();
        final TestClock clock = new TestClock();

        WindowedEventRate rate = WindowedEventRate.builder()
            .bucketCount(5)
            .window(Duration.ofSeconds(5))
            .clock(clock)
            .reporter((we) -> {
                log.info("{}: for window {}", we.getRate(), we.getWindowValue());
                consumer.add(we.getRate(Duration.ofSeconds(1)));
            })
            .eventListeners((event, atomicLongWindowed) -> eventListeners.add(event))

            .build();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            if (i % 100 == 0) {
                log.info(String.format("%d duration: %s", i, (System.currentTimeMillis() - start) + " ms. Measured rate " + rate.getRate(TimeUnit.SECONDS) + " #/s (" + rate.isWarmingUp() + ")"));
            }
            rate.accept(1);

        }

        clock.tick(4800L);

        log.info("duration: " + (System.currentTimeMillis() - start) + " ms. Measured rate " + rate.getRate(TimeUnit.SECONDS) + " #/s (" + rate.isWarmingUp() + ")");

        clock.sleep(201L);

        assertThat(rate.isWarmingUp()).isFalse();
        log.info("ranges: {}", rate.getRanges());

        log.info("events: {}", eventListeners);
        log.info("consumers: {}", consumer);
    }


    @Test
    public void testAccuracyDuringWarmup() {

        final TestClock clock = new TestClock();

        WindowedEventRate rate = WindowedEventRate.builder()
            .window(Duration.ofMillis(1500))
            .bucketCount(50)
            .clock(clock)
            .build();

        assertThat(rate.getBuckets()).hasSize(50);
        assertThat(rate.getTotalDuration()).isEqualByComparingTo(Duration.ofMillis(1500));

        for (int i = 0; i < 10; i++) {
            assertThat(rate.getTotalCount()).isEqualTo(i * 10);
            rate.newEvents(10);
            clock.sleep(100);
        }
        // got 100 events in about 1 second.
        assertThat(rate.isWarmingUp()).isTrue();
        assertThat(rate.getTotalCount()).isEqualTo(100);
        assertThat(rate.getRelevantDuration().toMillis()).isCloseTo(1000, withPercentage(20));

        double rateDuringWarmup = rate.getRate(TimeUnit.SECONDS);
        log.info(rateDuringWarmup + " ~ 100 /s");

        for (int i = 0; i < 10; i++) {
            rate.newEvents(10);
            clock.sleep(100);
        }
        assertThat(rate.isWarmingUp()).isFalse();
        Long relevantDuration = rate.getRelevantDuration().toMillis();
        assertThat(relevantDuration).isLessThanOrEqualTo(1500);
        assertThat(relevantDuration).isGreaterThan(1500 - rate.getBucketDuration().toMillis());

        double rateAfterWarmup = rate.getRate(TimeUnit.SECONDS);
        log.info(rateAfterWarmup + " ~ 100 /s");
        assertThat(rateAfterWarmup).isCloseTo(100.0, withPercentage(20));

        assertThat(rateDuringWarmup).isCloseTo(100.0, withPercentage(20));
    }

    @Test
    public void builder() {

        WindowedEventRate rate = WindowedEventRate.builder()
            .window(Duration.ofMinutes(5))
            .bucketDuration(Duration.ofSeconds(10))
            .build();
        assertThat(rate.getBucketCount()).isEqualTo(30);
    }

    @Test
    public void builder2() {
        WindowedEventRate rate = WindowedEventRate.builder()
            .window(Duration.ofMinutes(5))
            .build();
        assertThat(rate.getBucketCount()).isEqualTo(20);
        assertThat(rate.getBucketDuration()).isEqualTo(Duration.ofSeconds(15));

    }


    @Test
    public void string() {
        TestClock clock = new TestClock();
        WindowedEventRate rate = WindowedEventRate.builder()
            .window(Duration.ofSeconds(10))
            .clock(clock)
            .build();
        for (int i = 0; i< 10; i++) {
            rate.accept(5 + (i % 3));
            clock.tick();
        }
        clock.tick(50);
        assertThat(rate.isWarmingUp()).isFalse();
        assertThat(rate.toString()).isEqualTo("5.654450261780105 /s");

    }

}


