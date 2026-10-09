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
package org.meeuw.time;

import lombok.extern.java.Log;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.meeuw.configuration.ConfigurationService;
import org.meeuw.time.text.TimeConfiguration;

import static org.meeuw.math.text.TextUtils.isBlank;

/**
 * Utilities related to time. Currently, mainly related to <em>rounding</em> times.
 */
@Log
public final class TimeUtils {

    private TimeUtils() {}

    /**
     * Given a {@code Duration}, returns an 'order of magnitude' that it has.
     * <p>
     * This is the first match of the following::
     * <ul>
     *     <li>{@link ChronoUnit#DAYS} if the given duration is longer than 2 days</li>
     *     <li>{@link ChronoUnit#HOURS} if the given duration is longer than 2 hours</li>
     *     <li>{@link ChronoUnit#MINUTES} if the given duration is longer than 2 minutes</li>
     *     <li>{@link ChronoUnit#SECONDS} if the given duration is longer than 2 seconds</li>
     *     <li>{@link ChronoUnit#MILLIS} if the given duration is less than 2 seconds</li>
     * </ul>
     */
    public static ChronoUnit orderOfMagnitude(Duration stddev) {
        ChronoUnit order = ChronoUnit.DAYS;
        if (stddev.toDays() < 2) {
            order = ChronoUnit.HOURS;
            if (stddev.toHours() < 2) {
                order = ChronoUnit.MINUTES;
                if (stddev.toMinutes() < 2) {
                    order = ChronoUnit.SECONDS;
                    if (stddev.toMillis() < 2000) {
                        order = ChronoUnit.MILLIS;
                    }
                }
            }
        }
        return order;
    }

    /**
     * Round a duration.
     * @param duration The duration to round
     * @param order    The {@link ChronoUnit} to round to
     */
    public static Duration round(Duration duration, ChronoUnit order) {
        return switch (order) {
            //return Duration.ofDays(Math.round(duration.getSeconds() / 86400f));
            case DAYS, HOURS -> Duration.ofHours(Math.round(duration.getSeconds() / 3600f));
            case MINUTES -> Duration.ofMinutes(Math.round(duration.getSeconds() / 60f));
            case SECONDS -> Duration.ofSeconds(duration.toMillis() / 1000);
            case MILLIS -> Duration.ofMillis(duration.toMillis());
            default -> throw new IllegalArgumentException();
        };
    }

    /**
     * Round a duration to the order of magnitude of the given duration. This is used when the duration is a standard deviation
     * (e.g. in a duration or in an {@link UncertainInstant instant}).
     * @param stddev The duration to round
     */
    public static Duration roundStddev(Duration stddev) {
        return round(stddev, orderOfMagnitude(stddev));
    }
    /**
     * Round an instant.
     * @param instant The instant to round
     * @param order    The {@link ChronoUnit} to round to
     */
    public static Instant round(Instant instant, ChronoUnit order) {
        ChronoUnit trunc = ChronoUnit.values()[
             Math.max(ChronoUnit.MILLIS.ordinal(), Math.min(ChronoUnit.DAYS.ordinal(), order.ordinal() - 1))
             ];
         if (trunc == ChronoUnit.HALF_DAYS) {
             trunc = ChronoUnit.HOURS;
         }
         return instant.truncatedTo(trunc);
    }



    public static String format(Instant instant, ChronoUnit order) {
        return format(ZoneId.systemDefault(), instant, order);
    }

    public static String format(ZoneId zoneId, Instant instant, ChronoUnit order) {
        Instant toFormat = round(instant, order);
        if (order.ordinal() < ChronoUnit.DAYS.ordinal()) {
            return DateTimeFormatter.ISO_DATE_TIME.format(toFormat.atZone(zoneId).toLocalDateTime());
        } else {
            return DateTimeFormatter.ISO_DATE.format(toFormat.atZone(zoneId).toLocalDate());
        }
    }


    public static final Duration MAX_DURATION = Duration.ofSeconds(Long.MAX_VALUE, 999_999_999);


    /**
     * @since 0.22
     */
    public static Optional<ZonedDateTime> parseZoned(@Nullable CharSequence parse) {
        if (isBlank(parse)) {
            return Optional.empty();
        }
        try {
            return Optional.of(ZonedDateTime.parse(parse));
        } catch (DateTimeParseException ignored) {

        }
        Instant instant = parse(parse).orElse(null);
        if (instant == null) {
            return Optional.empty();
        }
        return Optional.of(instant.atZone(zoneId()));
    }

    public static ZoneId zoneId() {
        return ConfigurationService.getConfigurationAspect(TimeConfiguration.class).getZoneId();
    }
    static ZonedDateTime localEpoch() {
        return Instant.EPOCH.atZone(zoneId());
    }




    /**
     *
     * @since 0.22
     */
    public static Optional<Instant> parse(@Nullable CharSequence dateValue) {
        if (isBlank(dateValue)) {
            return Optional.empty();
        }
        try {
            // this is the proper XML representation to try that first
            return Optional.of(OffsetDateTime.parse(dateValue).toInstant());
        } catch (DateTimeParseException ignored) {

        }
        try {
            return Optional.of(LocalDate.parse(dateValue).atStartOfDay().atZone(zoneId()).toInstant());
        } catch (DateTimeParseException ignored) {

        }
        try {
            return Optional.of(LocalDateTime.parse(dateValue).atZone(zoneId()).toInstant());
        } catch (DateTimeParseException ignored) {

        }
        //return Instant.parse(dateValue);

        try {
            return Optional.of(ZonedDateTime.parse(dateValue).toInstant());
        } catch (DateTimeParseException ignored) {
        }
        DateTimeParseException dtp;
        try {
            return Optional.of(Instant.parse(dateValue));
        } catch (DateTimeParseException e) {
            dtp = e;
        }

        try {
            long longValue = Long.parseLong(dateValue.toString());
            if (longValue >= 1000 && longValue <= 9999) {
                return Optional.of(LocalDate.of((int) longValue, 1, 1).atStartOfDay().atZone(zoneId()).toInstant());
            } else {
                return Optional.of(Instant.ofEpochMilli(Long.parseLong(dateValue.toString())));
            }
        } catch (NumberFormatException nfe) {
            throw dtp;
        }

    }

    /**
     * Parses a {@link CharSequence} to a duration. This begins for checking emptyness (and returns then an empty {@link Optional} .
     * Then it basically calls {@link Duration#parse(CharSequence)}, but if that fails it has several fall backs:
     * <ul>
     *     <li>Also a week notation like <code>PT2W</code> as defined by ISO-8601 is supported</li>
     *     <li>Possible white space breaking the parsing will be ignored</li>
     *     <li>If the string looks like an unresolved (spring EL) variable, we'll parse to the empty optional</li>
     *     <li>If the string parses as a {@link Long}, it will be interpreted as a number of milliseconds</li>
     *     <li>If the string would become a valid ISO-8601 duration by prefixing it with 'P' or 'PT', that will be done too, making those prefixes effectively optional, so that the string like '1s' will parse to one second.</li>
     * </ul>
     * @see #parseDuration(CharSequence)
     * @since 0.22
     */
    public static Optional<Duration> parseDuration(CharSequence d) {
        return parseDuration(d, null);
    }


    /**
     * @since 0.22
     */
    @PolyNull
    public static Duration parseDurationOrThrow(@PolyNull CharSequence d) {
        return d == null ? null : parseDuration(d).orElseThrow(() -> new IllegalArgumentException("Cannot convert to duration  " + d));
    }

    /**
     * @since 0.22
     */
    public static Optional<Duration> parseDuration(CharSequence d, ZonedDateTime at) {
        return parseDuration(null, d, at);
    }


    private static final Pattern WEEKS = Pattern.compile("^P(\\d+)W$");
    private static final Pattern COMPLETE_FORMAT = Pattern.compile("^(P(?:\\d+Y)?(?:\\d+M)?(?:\\d+D)?)(T(?:\\d+H)?(?:\\d+M)?(?:[\\d.]+S)?)?$");

    private static Optional<Duration> parseDuration(DateTimeParseException original, CharSequence d, @Nullable ZonedDateTime at) {
        if (isBlank(d)) {
            return Optional.empty();
        }
        if (d.toString().startsWith("${")) {// unresolved spring setting;
            log.warning(() -> "Found %s as duration, returning empty".formatted(d));
            return Optional.empty();
        }

        try {
            return Optional.of(Duration.parse(d));
        } catch (DateTimeParseException dtp) {
            if (original != null) {
                dtp = original;
            }
            // For some reason Duration.parse does not support ISO_8601's PnW format (https://en.wikipedia.org/wiki/ISO_8601#Durations)
            Matcher matcher = WEEKS.matcher(d);
            if (matcher.matches()) {
                return Optional.of(Duration.ofDays(7L * Integer.parseInt(matcher.group(1))));
            }

            String ds = d.toString().replaceAll("\\s*", "");
            if (ds.length() < d.length()) {
                return parseDuration(dtp, ds, at);
            }

            try {
                return Optional.of(Duration.ofMillis(Long.parseLong(ds)));
            } catch (NumberFormatException nfe) {
                // ignore
            }
            if (!ds.startsWith("P")) {
                if (ds.contains("T")) {
                    return parseDuration(dtp, "P" + ds, at);
                } else {
                    return parseDuration(dtp, "PT" + ds, at);
                }
            } else {
                Matcher completeMatcher = COMPLETE_FORMAT.matcher(ds);
                if (completeMatcher.matches()) {
                    Period p = Period.parse(completeMatcher.group(1));
                    Duration time = completeMatcher.group(2) == null
                        ? Duration.ZERO
                        : Duration.parse("P" + completeMatcher.group(2));
                    if (at == null) {
                        ZonedDateTime le = localEpoch();
                        at = le;
                        log.fine(() -> "Implicitly using %s for duration evaluation".formatted(le));

                    }
                    return Optional.of(Duration.between(at, at.plus(p).plus(time)));
                } else if (!ds.startsWith("PT")){
                    // so it did start with P, just not with PT, and it couldn't be parsed
                    return parseDuration(dtp, "PT" + ds.substring(1), at);
                }
            }

            throw new DateTimeParseException(dtp.getParsedString() + ":" + dtp.getMessage(), dtp.getParsedString(), dtp.getErrorIndex());
        }
    }


    /**
     * Parses a {@link CharSequence} to a {@link TemporalAmount}. First using {@link Period#parse(CharSequence)}, and than, if that fails, using
     * {@link #parseDuration(CharSequence)}
     * @since 0.22
     */
    public static Optional<? extends TemporalAmount> parseTemporalAmount(@Nullable CharSequence d) {
        if (d == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Period.parse(d));
        } catch (DateTimeParseException dte) {
            return parseDuration(dte, d, Instant.EPOCH.atZone(zoneId()));
        }

    }

    /**
     * @since 0.22
     */
    public static String toParsableString(Duration duration) {
        return duration.toString().substring(2);
    }
    /**
     * @since 0.22
     */
    public static Optional<LocalDateTime> parseLocalDateTime(@Nullable CharSequence d) {
        if (isBlank(d)) {
            return Optional.empty();
        }
        if (d.toString().startsWith("${")) {// unresolved spring setting;
            log.warning(() -> "Found %s as localdatetime, returing empty".formatted(d));
            return Optional.empty();
        }


        try {
            return Optional.of(LocalDateTime.parse(d));
        } catch (DateTimeParseException dtp) {
            try {
                return Optional.of(LocalDate.parse(d).atStartOfDay());
            } catch (DateTimeParseException dtp2) {
                throw new DateTimeParseException(dtp.getParsedString() + ":" + dtp.getMessage(), dtp.getParsedString(), dtp.getErrorIndex());
            }
        }
    }

    /**
     * @since 0.22
     */
    public static Optional<LocalDate> parseLocalDate(CharSequence d) {
        if (isBlank(d)) {
            return Optional.empty();
        }
        if (d.toString().startsWith("${")) {// unresolved spring setting;
            log.warning(() -> "Found %s as localdatetime, returing empty".formatted(d));
            return Optional.empty();
        }


        try {
            return Optional.of(LocalDate.parse(d));
        } catch (DateTimeParseException dtp) {
            try {
                return Optional.of(LocalDateTime.parse(d).toLocalDate());
            } catch (DateTimeParseException dtp2) {
                throw new DateTimeParseException(dtp.getParsedString() + ":" + dtp.getMessage(), dtp.getParsedString(), dtp.getErrorIndex());
            }
        }
    }

    /**
     *
     * @since 0.22
     */
    public static Optional<Duration> durationOf(Integer i) {
        return Optional.ofNullable(i == null ? null : Duration.ofMillis(i));
    }


    /**
     *
     * @since 0.22
     */
    public static OptionalInt toSecondsInteger(Duration d) {
        return d == null ? OptionalInt.empty() : OptionalInt.of( (int) (d.toMillis() / 1000));
    }

    /**
     *
     * @since 0.22
     */
    public static Optional<Float> toSeconds(@Nullable Duration d) {
        return Optional.ofNullable(d == null ? null : d.toMillis() / 1000f);
    }

    /**
     *
     * @since 0.22
     */
    public static OptionalLong toMillis(@Nullable Duration d) {
        return d == null ? OptionalLong.empty() : OptionalLong.of(  d.toMillis());
    }



    /**
     * {@code null} safe version of {@link Duration#between(Temporal, Temporal)}. If one  or both of the arguments are null, the result is {@code null} too.
     * @since 0.22
     */
    @PolyNull
    public static Duration between(@PolyNull Temporal instant1, @PolyNull Temporal instant2) {
        if (instant1 == null || instant2 == null) {
            return null;
        }
        return Duration.between(instant1, instant2);
    }


    /**
     *
     * @since 0.22
     */
    public static boolean isLarger(@Nullable Duration duration1, @Nullable Duration duration2) {
        if (duration1 == null || duration2 == null) {
            return false;
        }
        return duration1.compareTo(duration2) > 0;
    }

    /**
     * Rounds the duration to the nearest millis (This may round up half a millis).
     * @since 0.22
     */
    @PolyNull
    public static Duration roundToMillis(@PolyNull Duration duration) {
        return duration == null ? null : Duration.ofMillis(duration.plus(Duration.ofNanos(500_000)).toMillis());

    }

    /**
     * @since 0.22
     */
    @PolyNull
    public static Instant truncatedTo(
        @PolyNull Instant instant,
        @Nullable ChronoUnit unit) {
        return instant == null ? null : instant.truncatedTo(unit);
    }

    /**
     * @since 0.22
     */
    @PolyNull
    public static Instant truncated(
        @PolyNull Instant instant) {
        return truncatedTo(instant, ChronoUnit.MILLIS);
    }




    private static final DateTimeFormatter LOCAL_TIME_PATTERN = DateTimeFormatter.ofPattern("H:mm");


    /**
     * @since 0.22
     */
    public static LocalTime parseLocalTime(CharSequence t) {
        try {
            return LocalTime.parse(t);
        } catch (DateTimeParseException dateTimeParseException) {
            return LocalTime.parse(t, LOCAL_TIME_PATTERN);
        }
    }

}
