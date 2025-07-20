package org.meeuw.time.dateparser;

import java.io.StringReader;
import java.time.*;
import java.time.chrono.Chronology;
import java.time.temporal.WeekFields;
import java.util.*;

import lombok.Setter;

public class DynamicDateTime {

    final Random random = new Random();

    @Setter
    private Clock clock = Clock.systemDefaultZone();
    private Locale locale = Locale.getDefault();

    protected DayOfWeek getFirstDayOfWeek() {
        return WeekFields.of(locale).getFirstDayOfWeek();
    }
    protected Chronology getChronology() {
        return Chronology.ofLocale(locale);
    }
    protected ZonedDateTime getInstance() {
        return ZonedDateTime.now(clock);
    }
    protected void setZoneId(ZoneId timeZone) {
        clock = clock.withZone(timeZone);
    }

    protected ZonedDateTime resetToEra() {
        clock = clock.withZone(ZoneId.of("UTC"));
        return Instant.ofEpochMilli(0).atZone(clock.getZone());
    }

    public ZonedDateTime parse(String string) throws ParseException {
        DateParser parser = new DateParser(new StringReader(string));
        parser.setDynamicDateTime(this);
        parser.start();
        return parser.get();
    }

    public static void main(String[] argv) throws ParseException {
        DynamicDateTime parser = new DynamicDateTime();
        System.out.println("" + parser.parse(argv[1]));
    }

}
