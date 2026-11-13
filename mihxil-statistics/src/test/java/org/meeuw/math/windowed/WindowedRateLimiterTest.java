package org.meeuw.math.windowed;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.meeuw.time.TestClock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 */
@Timeout(value = 1, unit = TimeUnit.MINUTES)
class WindowedRateLimiterTest {

    final TestClock clock = TestClock.twentyTwenty();

    WindowedRateLimiter limiter(int allowed) {
        return WindowedRateLimiter.builder()
            .window(Duration.ofMinutes(1))
            .bucketCount(6) // buckets of 10 s
            .allowedPerWindow(allowed)
            .clock(clock)
            .sleeper(clock)
            .build();
    }

    @Test
    void tryAcquire() {
        WindowedRateLimiter limiter = limiter(3);
        assertThat(limiter.retryAfter()).isEqualTo(Duration.ZERO);
        assertThat(limiter.tryAcquire()).isTrue();
        assertThat(limiter.tryAcquire()).isTrue();
        assertThat(limiter.tryAcquire()).isTrue();
        assertThat(limiter.tryAcquire()).isFalse();
        assertThat(limiter.getCount()).isEqualTo(3);
        assertThat(limiter.toString()).isEqualTo("3/3 per PT1M");
    }

    @Test
    void retryAfter() {
        WindowedRateLimiter limiter = limiter(3);
        limiter.tryAcquire();             // bucket 1
        clock.tick(Duration.ofSeconds(25));
        limiter.tryAcquire();             // bucket 3
        limiter.tryAcquire();
        assertThat(limiter.tryAcquire()).isFalse();

        // the first bucket is discarded after one window
        assertThat(limiter.retryAfter()).isEqualTo(Duration.ofSeconds(35).plusMillis(1));
        clock.tick(Duration.ofSeconds(35));
        assertThat(limiter.tryAcquire()).isFalse();
        clock.tick(1);
        assertThat(limiter.retryAfter()).isEqualTo(Duration.ZERO);
        assertThat(limiter.tryAcquire()).isTrue();

        // now the oldest bucket contains 2 events
        assertThat(limiter.retryAfter()).isEqualTo(Duration.ofSeconds(20));
    }

    @Test
    void tryAcquireWithMaxWait() throws InterruptedException {
        WindowedRateLimiter limiter = limiter(2);
        assertThat(limiter.tryAcquire(Duration.ZERO)).isTrue();
        assertThat(limiter.tryAcquire(Duration.ZERO)).isTrue();

        // would need to wait a minute, so doesn't wait at all
        assertThat(limiter.tryAcquire(Duration.ofSeconds(10))).isFalse();
        assertThat(clock.millis()).isEqualTo(TestClock.twentyTwenty().millis());

        assertThat(limiter.tryAcquire(Duration.ofMinutes(2))).isTrue();
        assertThat(Duration.ofMillis(clock.millis() - TestClock.twentyTwenty().millis())).isEqualTo(Duration.ofMinutes(1).plusMillis(1));
    }

    @Test
    void acquire() throws InterruptedException {
        WindowedRateLimiter limiter = limiter(10);
        for (int i = 0; i < 100; i++) {
            limiter.acquire();
            assertThat(limiter.getCount()).isLessThanOrEqualTo(10);
        }
        // 100 events, 10 per minute
        assertThat(Duration.ofMillis(clock.millis() - TestClock.twentyTwenty().millis())).isBetween(Duration.ofMinutes(9), Duration.ofMinutes(10));
    }

    @Test
    void maxPerBucket() throws InterruptedException {
        WindowedRateLimiter limiter = WindowedRateLimiter.builder()
            .window(Duration.ofMinutes(1))
            .bucketCount(6) // buckets of 10 s
            .allowedPerWindow(6)
            .maxPerBucket(2)
            .clock(clock)
            .sleeper(clock)
            .build();
        assertThat(limiter.tryAcquire()).isTrue();
        assertThat(limiter.tryAcquire()).isTrue();
        // the window is not full, but the bucket is
        assertThat(limiter.tryAcquire()).isFalse();
        assertThat(limiter.retryAfter()).isEqualTo(Duration.ofSeconds(10).plusMillis(1));
        assertThat(limiter.toString()).isEqualTo("2/6 per PT1M (max 2 per PT10S)");

        clock.tick(Duration.ofSeconds(10).plusMillis(1));
        assertThat(limiter.tryAcquire()).isTrue();
        limiter.acquire();
        limiter.acquire(); // needs to wait for the next bucket
        limiter.acquire();
        assertThat(limiter.getCount()).isEqualTo(6);
        assertThat(Duration.ofMillis(clock.millis() - TestClock.twentyTwenty().millis())).isEqualTo(Duration.ofSeconds(20).plusMillis(1));

        // now the window is full, which takes longer than the bucket
        assertThat(limiter.retryAfter()).isEqualTo(Duration.ofSeconds(40));

        limiter.setMaxPerBucket(0);
        assertThat(limiter.getMaxPerBucket()).isEqualTo(0);
        assertThat(limiter.toString()).isEqualTo("6/6 per PT1M");
    }

    @Test
    void setAllowedPerWindowKeepsCount() {
        WindowedRateLimiter limiter = limiter(3);
        limiter.tryAcquire();
        limiter.tryAcquire();
        limiter.setAllowedPerWindow(2);
        assertThat(limiter.getCount()).isEqualTo(2);
        assertThat(limiter.tryAcquire()).isFalse();
        limiter.setAllowedPerWindow(5);
        assertThat(limiter.tryAcquire()).isTrue();
        assertThatThrownBy(() -> limiter.setAllowedPerWindow(0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void setWindowKeepsCount() {
        try (WindowedRateLimiter limiter = limiter(2)) {
            limiter.tryAcquire();
            limiter.tryAcquire();
            limiter.setWindow(Duration.ofMinutes(2));
            assertThat(limiter.getWindow()).isEqualTo(Duration.ofMinutes(2));
            assertThat(limiter.getCount()).isEqualTo(2);
            assertThat(limiter.tryAcquire()).isFalse();
            assertThat(limiter.retryAfter()).isEqualTo(Duration.ofMinutes(2).plusMillis(1));
        }
    }

    @Test
    void notPositive() {
        assertThatThrownBy(() -> limiter(0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void concurrent() throws Exception {
        WindowedRateLimiter limiter = WindowedRateLimiter.builder()
            .window(Duration.ofHours(1))
            .allowedPerWindow(100)
            .build();
        AtomicInteger acquired = new AtomicInteger();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                futures.add(executor.submit(() -> {
                    if (limiter.tryAcquire()) {
                        acquired.incrementAndGet();
                    }
                }));
            }
            for (Future<?> f : futures) {
                f.get();
            }
        } finally {
            executor.shutdown();
        }
        assertThat(acquired.get()).isEqualTo(100);
        assertThat(limiter.getCount()).isEqualTo(100);
    }

    @Test
    void keyed() throws InterruptedException {
        KeyedWindowedRateLimiter<String> keyed = KeyedWindowedRateLimiter.<String>builder()
            .window(Duration.ofMinutes(1))
            .bucketCount(6)
            .allowedPerWindow(1)
            .clock(clock)
            .sleeper(clock)
            .build();
        assertThat(keyed.tryAcquire("a")).isTrue();
        assertThat(keyed.tryAcquire("a")).isFalse();
        assertThat(keyed.tryAcquire("b", Duration.ZERO)).isTrue();
        assertThat(keyed.getCounts()).containsEntry("a", 1L).containsEntry("b", 1L);

        keyed.setAllowedPerWindow(2);
        assertThat(keyed.get("a").getAllowedPerWindow()).isEqualTo(2);
        keyed.acquire("a");
        assertThat(keyed.getCounts()).containsEntry("a", 2L);

        keyed.setWindow(Duration.ofMinutes(2));
        assertThat(keyed.get("b").getWindow()).isEqualTo(Duration.ofMinutes(2));

        keyed.setMaxPerBucket(1);
        assertThat(keyed.get("b").getMaxPerBucket()).isEqualTo(1);
        assertThat(keyed.get("c").getMaxPerBucket()).isEqualTo(1);
        keyed.close();
        assertThat(keyed.getLimiters()).isEmpty();
    }

    @Test
    void mxBean() throws Exception {
        KeyedWindowedRateLimiter<String> keyed = KeyedWindowedRateLimiter.<String>builder()
            .window(Duration.ofMinutes(1))
            .bucketCount(6)
            .allowedPerWindow(5)
            .clock(clock)
            .build();
        keyed.tryAcquire("a");
        keyed.tryAcquire("a");
        keyed.tryAcquire("b");

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("org.meeuw.math.windowed:type=KeyedWindowedRateLimiter,name=test");
        server.registerMBean(keyed.getMXBean(), name);
        try {
            assertThat(server.getAttribute(name, "Window")).isEqualTo("PT1M");
            assertThat(server.getAttribute(name, "AllowedPerWindow")).isEqualTo(5L);
            TabularData counts = (TabularData) server.getAttribute(name, "Counts");
            assertThat(counts.size()).isEqualTo(2);
            CompositeData a = counts.get(new Object[] {"a"});
            assertThat(a.get("value")).isEqualTo(2L);

            assertThat(server.getAttribute(name, "MaxPerBucket")).isEqualTo(0L);
            server.setAttribute(name, new Attribute("MaxPerBucket", 3L));
            assertThat(keyed.get("a").getMaxPerBucket()).isEqualTo(3);

            server.setAttribute(name, new Attribute("Window", "2m"));
            server.setAttribute(name, new Attribute("AllowedPerWindow", 2L));
            assertThat(keyed.getWindow()).isEqualTo(Duration.ofMinutes(2));
            assertThat(keyed.get("a").getAllowedPerWindow()).isEqualTo(2);
            assertThat(keyed.tryAcquire("a")).isFalse();

            server.invoke(name, "clear", new Object[0], new String[0]);
            assertThat(keyed.getLimiters()).isEmpty();
            assertThat(keyed.tryAcquire("a")).isTrue();
        } finally {
            server.unregisterMBean(name);
        }
    }
}
