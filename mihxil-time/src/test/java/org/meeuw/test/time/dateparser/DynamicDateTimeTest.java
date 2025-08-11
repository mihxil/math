package org.meeuw.test.time.dateparser;

import java.util.Date;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.meeuw.time.TestClock;
import org.meeuw.time.dateparser.DynamicDateTime;
import org.meeuw.time.dateparser.ParseException;

class DynamicDateTimeTest {


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
            "next year \"spring\"",
            "\"easter sunday\""
        };
    }

    @ParameterizedTest
    @MethodSource("getDemo")
    public void tryDemo(String demo) throws ParseException {
        java.text.DateFormat formatter = new java.text.SimpleDateFormat("GGGG yyyy-MM-dd HH:mm:ss.SSS zzz E");

        DynamicDateTime dt = DynamicDateTime.builder()
            .clock( TestClock.twentyTwenty())
            .build();
        System.out.println(formatter.format(Date.from(dt.applyWithException(demo).toInstant())) + "\t" + demo);
    }

}
