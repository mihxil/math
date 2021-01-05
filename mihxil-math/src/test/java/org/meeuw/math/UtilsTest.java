package org.meeuw.math;

import lombok.extern.log4j.Log4j2;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.meeuw.math.text.TextUtils;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
class UtilsTest {

    ZoneId id = ZoneId.of("Europe/Amsterdam");

    @Test
    void orderOfMagnitude() {
    }

    @Test
    void roundDuration() {
        assertThat(Utils.round(Duration.ofMillis(12345567L), ChronoUnit.SECONDS).toString()).isEqualTo("PT3H25M45S");
        assertThat(Utils.round(Duration.ofMillis(12345567L), ChronoUnit.MINUTES).toString()).isEqualTo("PT3H26M");
        assertThat(Utils.round(Duration.ofMillis(12345567L), ChronoUnit.MILLIS).toString()).isEqualTo("PT3H25M45.567S");
        assertThat(Utils.round(Duration.ofMillis(1112345567L), ChronoUnit.DAYS).toString()).isEqualTo("PT309H");
        assertThat(Utils.round(Duration.ofMillis(1112345567L), ChronoUnit.HOURS).toString()).isEqualTo("PT309H");

    }

    @Test
    void roundInstant() {
        assertThat(TextUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.MINUTES)).isEqualTo("2018-10-16T15:48:15");
        assertThat(TextUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.SECONDS)).isEqualTo("2018-10-16T15:48:15.592");

        assertThat(TextUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.HOURS)).isEqualTo("2018-10-16T15:48:00");
        //assertThat(Utils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.HOURS)).isEqualTo("2018-10-16T15:48"); TODO

        assertThat(TextUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.DAYS)).isEqualTo("2018-10-16");
        assertThat(TextUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.WEEKS)).isEqualTo("2018-10-16");
    }


    @Test
    public void incForStream() {
        int[] counters = new int[] {0, 0};
        int max = 0;
        max = Utils.inc(counters, max);
        assertThat(max).isEqualTo(2);
        assertThat(counters).containsExactly(1, 0);
        max = Utils.inc(counters, max);
        assertThat(counters).containsExactly(2, 0);
        max = Utils.inc(counters, max);
        assertThat(counters).containsExactly(0, 1);
        max = Utils.inc(counters, max);
        assertThat(counters).containsExactly(1, 1);
        max = Utils.inc(counters, max);
        assertThat(counters).containsExactly(2, 1);
        max = Utils.inc(counters, max);
        assertThat(counters).containsExactly(0, 2);
        max = Utils.inc(counters, max);
        assertThat(counters).containsExactly(1, 2);


        log.info(Arrays.stream(counters).mapToObj(String::valueOf).collect(joining(", ")));

    }

    @Test
    public void stream() {
        Utils.stream(3).limit(100).forEach(i -> {
            log.info(() -> Arrays.stream(i).mapToObj(String::valueOf).collect(joining(", ")));
        });
    }

    @Test
    public void log10() {
        long start = System.currentTimeMillis();
        int d = 0;
        for (int i = 0; i < 1000000L; i++) {
            d = Utils.log10(123456789);
        }
        log.info("{} : {}", d, System.currentTimeMillis() -start);
        assertThat(Utils.log10(10)).isEqualTo(1);
        assertThat(Utils.log10(100)).isEqualTo(2);
        assertThat(Utils.log10(10d)).isEqualTo(1);
        assertThat(Utils.log10(20)).isEqualTo(1);
        assertThat(Utils.log10(20d)).isEqualTo(1);
    }

    @Test
    public void uncertaintityForDouble() {
        assertThat(Utils.uncertaintyForDouble(0)).isEqualTo(4.9E-324);
        assertThat(Utils.uncertaintyForDouble(1e-300)).isEqualTo(3.31561842E-316);
        assertThat(Utils.uncertaintyForDouble(1e-100)).isEqualTo(2.5379418373156492E-116);
        assertThat(Utils.uncertaintyForDouble(1e-16)).isEqualTo(2.465190328815662E-32);
        assertThat(Utils.uncertaintyForDouble(-1)).isEqualTo(4.440892098500626E-16);
        assertThat(Utils.uncertaintyForDouble(1)).isEqualTo(4.440892098500626E-16);
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199})
    public void isPrime(int prime) {
        assertThat(Utils.isPrime(prime)).isTrue();
    }
    @ParameterizedTest
    @ValueSource(ints = {4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 36, 38, 39, 40, 42, 44, 45, 46, 48, 49, 50, 51, 52, 54, 55, 56, 57, 58, 60, 62, 63, 64, 65, 66, 68, 69, 70, 72, 74, 75, 76, 77, 78, 80, 81,82, 84, 85, 86, 87, 88, 90, 91, 92, 93, 94, 95, 96, 98, 99, 100})
    public void isNotPrime(int composite) {
        assertThat(Utils.isPrime(composite)).isFalse();
    }
}
