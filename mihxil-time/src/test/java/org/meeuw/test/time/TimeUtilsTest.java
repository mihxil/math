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

import lombok.extern.log4j.Log4j2;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.time.TimeUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@Log4j2
class TimeUtilsTest {


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
    void instant() {
        assertThat(TimeUtils.format(Instant.parse("2021-08-22T20:00:14Z"), ChronoUnit.DAYS)).startsWith("2021-08");

        assertThat(TimeUtils.format(ZoneId.of("Europe/Amsterdam"), Instant.parse("2021-08-22T20:00:14Z"), ChronoUnit.DAYS)).isEqualTo("2021-08-22");
        assertThat(TimeUtils.format(ZoneId.of("Europe/Amsterdam"), Instant.parse("2021-08-22T20:00:14Z"), ChronoUnit.SECONDS)).isEqualTo("2021-08-22T22:00:14");
    }


    @Test
    void parseZoned() {
        assertThat(TimeUtils.parseZoned("2000-01-01").get()).isEqualTo(ZonedDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(0, 0), TimeUtils.zoneId()));
    }

    @Test
    void parseZonedYear() {
        assertThat(TimeUtils.parseZoned("2000").get()).isEqualTo(ZonedDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.of(0, 0), TimeUtils.zoneId()));
    }

    @Test
    void parseMillis() {
        assertThat(TimeUtils.parseZoned("1474643244279").get()).isEqualTo(ZonedDateTime.of(LocalDate.of(2016, 9, 23), LocalTime.of(17, 7, 24, 279000000), TimeUtils.zoneId()));
    }

    @Test
    void parse() {
        assertThat(TimeUtils.parse("2000-07-11T14:00:33.556+02:00").get())
            .isEqualTo(ZonedDateTime.of(LocalDate.of(2000, 7, 11), LocalTime.of(14, 0, 33, 556000000), TimeUtils.zoneId()).toInstant());
    }

    @Test
    void parse2() {
        LocalDateTime example = LocalDateTime.of(2018, 2, 13, 9, 0);
        log.info("{}", example);
        assertThat(TimeUtils.parse("2018-02-13T09:00").get())
            .isEqualTo(example.atZone(TimeUtils.zoneId()).toInstant());

    }

    @Test
    void parseDuration() {
        assertThat(TimeUtils.parseDuration("PT5M").get()).isEqualTo(Duration.ofMinutes(5));
        assertThat(TimeUtils.parseDuration("T5M").get()).isEqualTo(Duration.ofMinutes(5));
        assertThat(TimeUtils.parseDuration("5M").get()).isEqualTo(Duration.ofMinutes(5));
        assertThat(TimeUtils.parseDuration("6s").get()).isEqualTo(Duration.ofSeconds(6));
        assertThat(TimeUtils.parseDuration("6 s").get()).isEqualTo(Duration.ofSeconds(6));
        assertThat(TimeUtils.parseDuration("7S").get()).isEqualTo(Duration.ofSeconds(7));
        assertThat(TimeUtils.parseDuration("5000").get()).isEqualTo(Duration.ofMillis(5000));
        assertThat(TimeUtils.parseDuration("PT300s").get()).isEqualTo(Duration.ofSeconds(300));

        assertThat(TimeUtils.parseDuration("PT-300s").get()).isEqualTo(Duration.ofSeconds(-300));
        assertThat(TimeUtils.parseDuration("-300s").get()).isEqualTo(Duration.ofSeconds(-300));
        assertThat(TimeUtils.parseDuration("0.1s").get()).isEqualTo(Duration.ofMillis(100));

        assertThat(TimeUtils.parseDuration("-2M").get()).isEqualTo(Duration.ofSeconds(-120));


        assertThat(TimeUtils.parseDuration("P10D").get()).isEqualTo(Duration.ofHours(240));



        assertThat(TimeUtils.parseDuration("").orElse(null)).isNull();


        assertThatThrownBy(() -> TimeUtils.parseDuration("can'tbeparsed"))
            .isExactlyInstanceOf(DateTimeParseException.class)
            .hasMessage("can'tbeparsed:Text cannot be parsed to a Duration")
            .matches((dtm) -> {
                return ((DateTimeParseException) dtm).getParsedString().equals("can'tbeparsed");
            }, "doest match");
    }

    @Test
    void dontConfuseMonthWithMinutes() {
        assertThat(TimeUtils.parseDuration("PT1M").get()).isEqualTo(Duration.ofMinutes(1));
        ZonedDateTime now = ZonedDateTime.parse("2026-10-11T11:00:00+02:00");
        assertThat(TimeUtils.parseDuration("P1MT1M", now).get()).isEqualTo(Duration.ofHours(744).plusMinutes(1));
        assertThat(TimeUtils.parseDuration("P1M", now).get()).isEqualTo(Duration.ofHours(744));
    }

    @Test
    void parseWeek() {
        assertThat(TimeUtils.parseDuration("P4W").get()).isEqualTo(Duration.ofDays(7 * 4));
    }

    @Test
    void parseMonth() {
        assertThat(TimeUtils.parseTemporalAmount("P1M").get()).isEqualTo(Period.ofMonths(1));
        assertThat(TimeUtils.parseTemporalAmount("1M").get()).isEqualTo(Duration.ofMinutes(1));
        assertThat(TimeUtils.parseTemporalAmount("PT1M").get()).isEqualTo(Duration.ofMinutes(1));
        assertThat(TimeUtils.parseTemporalAmount("T1M").get()).isEqualTo(Duration.ofMinutes(1));
    }

    @Test
    void parseYears() {
        assertThat(TimeUtils.parseDuration("P2Y2M10DT0H0M0.000S")).contains(Duration.ofDays(800));
    }

    @Test
    void durationToString() {
        assertThat(TimeUtils.toParsableString(Duration.ofSeconds(5))).isEqualTo("5S");
        assertThat(TimeUtils.toParsableString(Duration.ofDays(50))).isEqualTo("1200H");
    }


    @Test
    void parseLocalDateTime() {
        assertThat(
            TimeUtils.parseLocalDateTime("2019-02-13T09:16").get()).isEqualTo(LocalDateTime.of(2019,2, 13, 9, 16));

        assertThat(
            TimeUtils.parseLocalDateTime("2019-02-13").get()).isEqualTo(LocalDate.of(2019,2, 13).atStartOfDay());

    }

    @Test
    void testLarger() {
        assertThat(TimeUtils.isLarger(TimeUtils.parseDuration("PT6M").get(), TimeUtils.parseDuration("PT5M").get())).isTrue();
    }

    @Test
    void roundMillis() {
        Duration duration = Duration.ofSeconds(4).plusNanos(600_000); // 4 seconds 0.6 ms
        assertThat(duration.toMillis()).isEqualTo(4000);

        log.info("{}", duration);
        assertThat(TimeUtils.roundToMillis(duration).toString()).isEqualTo("PT4.001S");
        assertThat(TimeUtils.roundToMillis(duration).toMillis()).isEqualTo(4001);

    }


}
