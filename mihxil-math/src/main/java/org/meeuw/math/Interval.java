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
package org.meeuw.math;

import lombok.EqualsAndHashCode;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Simple interval implementation. Used to use guava's Range for this, but this was all we need. Just cutting the dependency. It's not hard to convert this to guava's Range.
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <T> The type of the endpoints
 */
@EqualsAndHashCode
public class Interval<T extends Comparable<T>> implements Predicate<T>  {

    public enum BoundType {
        OPEN,
        CLOSED
    }

    private final T lowerEndpoint;
    private final boolean includeLower;

    private final T upperEndpoint;
    private final boolean includeUpper;

    private Interval(@Nullable T lowerEndpoint, boolean includeLower, @Nullable T upperEndpoint, boolean includeUpper) {
        if (lowerEndpoint != null && upperEndpoint != null && lowerEndpoint.compareTo(upperEndpoint) > 0) {
            // just turn them around
            T tempT = lowerEndpoint;
            lowerEndpoint = upperEndpoint;
            upperEndpoint = tempT;
            boolean tempBoolean = includeLower;
            includeLower = includeUpper;
            includeUpper = tempBoolean;
        }
        this.lowerEndpoint = lowerEndpoint;
        this.includeLower = includeLower;
        this.upperEndpoint = upperEndpoint;
        this.includeUpper = includeUpper;
    }

    public static <S extends Comparable<S>> Interval<S> closedOpen(S begin, S end) {
        return new Interval<>(begin, true,  end, false);
    }
    public static <S extends Comparable<S>> Interval<S> openClosed(S begin, S end) {
        return new Interval<>(begin, false,  end, true);
    }

    public static <S extends Comparable<S>> Interval<S> open(S begin, S end) {
        return new Interval<>(begin, false,  end, false);
    }
    public static <S extends Comparable<S>> Interval<S> closed(S begin, S end) {
        return new Interval<>(begin, true,  end, true);
    }

    public T lowerEndpoint() {
        return lowerEndpoint;
    }

    public T upperEndpoint() {
        return upperEndpoint;
    }

    public BoundType lowerBoundType() {
        return includeLower ? BoundType.CLOSED : BoundType.OPEN;
    }
    public BoundType upperBoundType() {
        return includeUpper ? BoundType.CLOSED : BoundType.OPEN;
    }

    @Override
    public boolean test(@Nullable T t) {
        if (t == null) {
            return false;
        }
        return
            (lowerEndpoint == null || (includeLower ? lowerEndpoint.compareTo(t) <= 0 :   lowerEndpoint.compareTo(t) < 0 ))
                &&
                (upperEndpoint == null || (includeUpper ? upperEndpoint.compareTo(t) >= 0 :  upperEndpoint.compareTo(t) > 0 ));

    }

    public static <C extends Comparable<C>> Comparator<Interval<C>> lowerEndPointComparator() {
        Comparator<C> comparing = Comparator.nullsFirst(Comparator.naturalOrder());
        Comparator<Interval<C>> boundType = Comparator.comparing(Interval::lowerBoundType);
        return Comparator.comparing(
            (Function<Interval<C>, C>) interval -> interval.lowerEndpoint, comparing).thenComparing(boundType.reversed());
    }


    public static <C extends Comparable<C>> Comparator<Interval<C>> upperEndPointComparator() {
        Comparator<C> comparing = Comparator.nullsLast(Comparator.naturalOrder());
        Comparator<Interval<C>> boundType = Comparator.comparing(Interval::upperBoundType);
        return Comparator.comparing(
            (Function<Interval<C>, C>) interval -> interval.upperEndpoint, comparing).thenComparing(boundType);
    }

    @Override
    public String toString() {
        return (includeLower ? "[" : "(") +
            (lowerEndpoint == null ? "-∞" : lowerEndpoint) +
            ',' +
            (upperEndpoint == null ? "+∞" : upperEndpoint) +
            (includeUpper ? ']' : ')');
    }

}
