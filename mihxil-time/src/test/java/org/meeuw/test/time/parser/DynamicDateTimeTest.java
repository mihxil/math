package org.meeuw.test.time.parser;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.meeuw.functional.ThrowingSupplier;
import org.meeuw.time.TestClock;
import org.meeuw.time.parser.DynamicDateTime;
import org.meeuw.time.parser.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

class DynamicDateTimeTest {

    TestClock clock = TestClock.twentyTwenty();
    DynamicDateTime dt = DynamicDateTime.builder()
            .clock(clock)
            .build();

    public static String[] getDemo() {
        return new String[] {
            "0", "10000", "-10000", "+1000", // just numbers a bit after 1970, a bit before
            "1973-05-03", "2006-05-09", "-3-12-25", // absolute dates
            "2000-01-01 16:00", "TZUTC 2001-01-01 16:00","today 12:34:56.789",
            "now", "today", "tomorrow", "now + 10 minute", "today + 5 day",
            "now this year", "next august", "today + 6 month next august", "tomonth", "borreltijd", "today + 5 dayish", "yesteryear", "mondayish",
            "duration + 5 minute", "duration + 100 year",
            "TZUTC today noon", "TZEurope/Amsterdam today noon", "TZUTC today", "TZEurope/Amsterdam today",
            "TZ UTC today noon", "TZ Europe/Amsterdam today noon", "TZ UTC today", "TZ Europe/Amsterdam today",
            "TZ Europe/Amsterdam -1000",
            "today 6 oclock", "today 23 oclock", "today 43 oclock",
            "tosecond", "tominute", "tohour", "today", "previous monday", "tomonth", "toyear", "tocentury", "tocentury_pedantic", "toera", "toweek",
            "now this second", "now this minute", "now this hour", "now this day", "today previous monday", "now this month", "now this year", "now this century", "now this era",
            "now - 15 year this century", "now - 20 year this century_pedantic", "today + 2 century", "toera - 1 minute",
            "this july", "previous july", "next july", "this sunday", "previous sunday", "next sunday",
            "2009-W01-01", "2009-W53-7", "2006-123",
            "2005-01-01 this monday",
            "next year",
            "\"spring\"",
            "2005-01-01 this \"spring\"",
            "\"easter sunday\""
        };
    }

    @ParameterizedTest
    @MethodSource("getDemo")
    public void tryDemo(String demo) throws ParseException {
        java.text.DateFormat formatter = new java.text.SimpleDateFormat("GGGG yyyy-MM-dd HH:mm:ss.SSS zzz E");


        System.out.println(formatter.format(Date.from(dt.applyWithException(demo).toInstant())) + "\t" + demo);
    }

    @Test
    public  void now() {
        assertThat(dt.apply("now").toString()).isEqualTo("2020-02-20T20:20+01:00[Europe/Amsterdam]");
        assertThat(dt.apply("").toString()).isEqualTo("2020-02-20T20:20+01:00[Europe/Amsterdam]");
    }

    @Test
    public  void absolute() {
        assertThat(dt.apply("1973-03-05").toString()).isEqualTo("1973-03-05T00:00+01:00[Europe/Amsterdam]");

        assertThat(dt.apply("1973-03-05T06:00").toString()).isEqualTo("1973-03-05T06:00+01:00[Europe/Amsterdam]");
    }

    @Test
    public void offset() {

        assertThat(dt.apply("now + 5 minute").toString()).isEqualTo("2020-02-20T20:25+01:00[Europe/Amsterdam]");
        assertThat(dt.apply("now + 5 minute").toString()).isEqualTo("2020-02-20T20:25+01:00[Europe/Amsterdam]");

        assertThat(dt.apply("now + 5 minutes").toString()).isEqualTo("2020-02-20T20:25+01:00[Europe/Amsterdam]");

        assertThat(dt.apply("2021-08-05T20:00 + 1 minute - 2 days").toString()).isEqualTo("2021-08-03T20:01+02:00[Europe/Amsterdam]");
         assertThat(dt.apply("now next week").toString()).isEqualTo("2020-02-27T20:20+01:00[Europe/Amsterdam]");
    }

    @Test
    public void special_offset() {
        assertThat(dt.apply("2025-08-30T12:00 teatime").toString()).isEqualTo("");
    }



    @Test
    public void rounding() {

        assertThat(dt.apply("now this day").toString()).isEqualTo("2020-02-20T00:00+01:00[Europe/Amsterdam]");

        assertThat(dt.apply("this day").toString()).isEqualTo("2020-02-20T00:00+01:00[Europe/Amsterdam]");

        assertThat(dt.apply("this month").toString()).isEqualTo("2020-02-01T00:00+01:00[Europe/Amsterdam]");

        assertThat(dt.apply("next month this month").toString()).isEqualTo("2020-03-01T00:00+01:00[Europe/Amsterdam]");

    }




    @Test

    public void spring2005() {

        assertThat(dt.apply("2005-01-01 \"spring\"").toString()).isEqualTo("");
    }


    @Test
    public void supplier() {



        ThrowingSupplier<ZonedDateTime, ParseException> supplier = dt.supplier("now + 10 minute");


        assertThat(supplier.get().toString()).isEqualTo("2020-02-20T20:30+01:00[Europe/Amsterdam]");

        clock.tick(Duration.ofMinutes(5));

        assertThat(supplier.get().toString()).isEqualTo("2020-02-20T20:35+01:00[Europe/Amsterdam]");

    }

}
