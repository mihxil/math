package org.meeuw.time;

import java.time.Year;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Simple range implementation for Comparable types.
 * It always as a start. The end value can be null, which means it is unbounded.
 * <p>
 * The end value may also be less than the start value, which means it is a backward range.
 * <p>
 * Times often occur in ranges.
 *
 * @author Michiel Meeuwissen
 * @since 0.19
 */

public class Range<C extends Comparable<C>> implements Predicate<C> {

    private final C start;
    private final C end;
    private final boolean forward;

    public static Range<Year> ofYears(int startYear, int endYear) {
        return new Range<>(Year.of(startYear), Year.of(endYear));
    }

    public static Range<Year> ofYear(int year) {
        Year y = Year.of(year);
        return new Range<>(y, y);
    }

    public static Range<Year> fromYear(int year) {
        return fromYear(year, true);
    }

    public static Range<Year> untilYear(int year) {
        return fromYear(year, false);
    }

    public static Range<Year> fromYear(int year, boolean forward) {
        Year y = Year.of(year);
        return new Range<>(y, forward);
    }

    public static Stream<Year> stream(Range<Year> range) {
        return range.stream(Year::plusYears);
    }

    public Range(@NonNull C start, C end) {
        if (start == null) {
            throw new IllegalArgumentException("Start cannot be null");
        }
        this.start = start;
        this.end = end;
        this.forward = end == null || start.compareTo(end) <= 0;
    }

    public Range(@NonNull C start, boolean forward) {
        if (start == null) {
            throw new IllegalArgumentException("Start cannot be null");
        }
        this.start = start;
        this.end = null;
        this.forward = forward;
    }

    public C getStart() {
        return start;
    }

    public C getEnd() {
        return end;
    }

    public boolean forward() {
        return forward;
    }

    public Stream<C> stream(BiFunction<C, Integer, C> nextFunction) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<C>() {
            C current = null;
            ;
            final int step = forward ? 1 : -1;

            @Override
            public boolean hasNext() {
                return current == null || end == null || (forward ? current.compareTo(end) < 0 : current.compareTo(end) > 0);
            }

            @Override
            public C next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in the range");
                }

                current = current == null ? start : nextFunction.apply(current, step);
                return current;
            }
        }, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL), false);

    }

    @Override
    public boolean test(C value) {
        if (value == null) {
            return false;
        }
        if (forward) {
            return start.compareTo(value) <= 0 && (end == null || end.compareTo(value) >= 0);
        } else {
            return start.compareTo(value) >= 0 && (end == null || end.compareTo(value) <= 0);
        }
    }

    public boolean encloses(Range<C> value) {
        if (value == null) {
            return false;
        }
        if (! test(value.start)) {
             return false;
        }
        if (value.end != null) {
            return test(value.end);
        } else {
            return end == null && value.forward == forward;
        }
    }

    public boolean isConnected(Range<C> value) {
        if (value == null) {
            return false;
        }
        if (test(value.start)) {
            return true;
        }
        if (value.end != null && test(value.end)) {
            return true;
        } else {
            return end == null && value.forward == forward;
        }
    }


    @Override
    public String toString() {
        return "Range{" +
            "start=" + start +
            ", end=" + end +
            '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Range)) return false;

        Range<?> range = (Range<?>) o;
        return start.equals(range.start) && end.equals(range.end);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }
}
