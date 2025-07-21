package org.meeuw.time.dateparser;

import lombok.Getter;
import lombok.With;

import java.io.StringReader;
import java.time.*;
import java.time.chrono.Chronology;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Random;

import org.meeuw.functional.ThrowingFunction;
import org.meeuw.functional.ThrowingSupplier;


public class DynamicDateTime implements ThrowingFunction<String, ZonedDateTime, ParseException> {

    @With
    final Random random;

    @Getter
    @With
    Clock clock;

    @With
    final Locale locale;

    public DynamicDateTime() {
        this(null, null, null);
    }
    @lombok.Builder
    private DynamicDateTime(Random random, Clock clock, Locale locale) {
        this.random = random == null ? new Random() : random;
        this.clock = clock == null ? Clock.systemDefaultZone() : clock;
        this.locale = locale == null ? Locale.getDefault() : locale;
    }

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

    @Override
    public ZonedDateTime applyWithException(String string) throws ParseException {
        DateParser parser = new DateParser(new StringReader(string));
        parser.setDynamicDateTime(this);
        parser.start();
        return parser.get();
    }
    public ThrowingSupplier<ZonedDateTime, ParseException> supply(String string)  {
        return () -> {
            DateParser parser = new DateParser(new StringReader(string));
            parser.setDynamicDateTime(this);
            parser.start();
            return parser.get();
        };
    }

    public static void main(String[] argv) throws ParseException {
        DynamicDateTime parser = new DynamicDateTime();
        System.out.println("" + parser.applyWithException(argv[1]));
    }

}
