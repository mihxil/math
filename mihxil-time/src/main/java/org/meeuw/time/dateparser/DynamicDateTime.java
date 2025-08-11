package org.meeuw.time.dateparser;

import lombok.Getter;
import lombok.With;

import java.time.*;
import java.time.chrono.Chronology;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Random;

import org.meeuw.functional.ThrowingFunction;
import org.meeuw.functional.ThrowingSupplier;


/**
 * A parser for dynamic date and time strings, which can be used to parse various date-time formats.
 * It uses a {@link DateParser} internally to handle the parsing logic.
 * <p>
 * This class is designed to be flexible and can be configured with different locales and time zones.
 * It also provides methods to get the current date-time, reset to the epoch, and set the time zone.
 * <p>
 * It is implemented as a {@link java.util.function.Function} (actually a {@link ThrowingFunction}) that takes a string as input and returns a {@link ZonedDateTime} object.
 * @see DateParser
 * @since 0.19
 */
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

    /**
     * Parses the given string into a {@link ZonedDateTime}.
     * @param string The strign to be parsed.
     * @return A ZonedDateTime object representing the parsed date and time
     * @throws ParseException If the string could not be parsed
     */
    @Override
    public ZonedDateTime applyWithException(String string) throws ParseException {
        DateParser parser = new DateParser(string);
        parser.setDynamicDateTime(this);
        parser.start();
        return parser.get();
    }

    /**
     * Return a supplier that can be used to parse a string into a {@link ZonedDateTime}.
     * This is useful for lazy evaluation or when you want to delay the parsing and evaluation of the string until it is actually needed. E.g. the every time the Supplier is called with for `now` a different ZonedDateTime is returned.
     * @param string The string to be parsed into a ZonedDateTime.
     * @return A Supplier that can be used to parse the string into a ZonedDateTime.
     */
    public ThrowingSupplier<ZonedDateTime, ParseException> supply(String string)  {
        return () -> {
            DateParser parser = new DateParser(string);
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
