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
package org.meeuw.test.time;

import java.time.*;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.time.TimeUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeUtilsTest {


    ZoneId id = ZoneId.of("Europe/Amsterdam");

    @Test
    void orderOfMagnitude() {
        Assertions.assertThat(TimeUtils.orderOfMagnitude(Duration.ofMillis(1))).isEqualTo(ChronoUnit.MILLIS);
        assertThat(TimeUtils.orderOfMagnitude(Duration.ofSeconds(100))).isEqualTo(ChronoUnit.SECONDS);
        assertThat(TimeUtils.orderOfMagnitude(Duration.ofMinutes(100))).isEqualTo(ChronoUnit.MINUTES);
        assertThat(TimeUtils.orderOfMagnitude(Duration.ofHours(20))).isEqualTo(ChronoUnit.HOURS);
        assertThat(TimeUtils.orderOfMagnitude(Duration.ofDays(10))).isEqualTo(ChronoUnit.DAYS);
    }

    @Test
    void roundDuration() {
        assertThat(TimeUtils.round(Duration.ofMillis(12345567L), ChronoUnit.SECONDS).toString()).isEqualTo("PT3H25M45S");
        assertThat(TimeUtils.round(Duration.ofMillis(12345567L), ChronoUnit.MINUTES).toString()).isEqualTo("PT3H26M");
        assertThat(TimeUtils.round(Duration.ofMillis(12345567L), ChronoUnit.MILLIS).toString()).isEqualTo("PT3H25M45.567S");
        assertThat(TimeUtils.round(Duration.ofMillis(1112345567L), ChronoUnit.DAYS).toString()).isEqualTo("PT309H");
        assertThat(TimeUtils.round(Duration.ofMillis(1112345567L), ChronoUnit.HOURS).toString()).isEqualTo("PT309H");
        Assertions.assertThatThrownBy(() -> TimeUtils.round(Duration.ofMillis(1112345567L), ChronoUnit.YEARS)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void roundInstant() {
        assertThat(TimeUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.MINUTES)).isEqualTo("2018-10-16T15:48:15");
        assertThat(TimeUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.SECONDS)).isEqualTo("2018-10-16T15:48:15.592");

        assertThat(TimeUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.HOURS)).isEqualTo("2018-10-16T15:48:00");
        //assertThat(Utils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.HOURS)).isEqualTo("2018-10-16T15:48"); TODO

        assertThat(TimeUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.DAYS)).isEqualTo("2018-10-16");
        assertThat(TimeUtils.format(id, Instant.ofEpochMilli(1539697695592L), ChronoUnit.WEEKS)).isEqualTo("2018-10-16");
    }

    @Test
    public void instant() {
        assertThat(TimeUtils.format(Instant.parse("2021-08-22T20:00:14Z"), ChronoUnit.DAYS)).startsWith("2021-08");

        assertThat(TimeUtils.format(ZoneId.of("Europe/Amsterdam"), Instant.parse("2021-08-22T20:00:14Z"), ChronoUnit.DAYS)).isEqualTo("2021-08-22");
        assertThat(TimeUtils.format(ZoneId.of("Europe/Amsterdam"), Instant.parse("2021-08-22T20:00:14Z"), ChronoUnit.SECONDS)).isEqualTo("2021-08-22T22:00:14");
    }


}
