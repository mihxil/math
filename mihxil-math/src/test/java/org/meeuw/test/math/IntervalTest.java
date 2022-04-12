/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math;

import java.util.*;

import org.junit.jupiter.api.Test;

import static java.lang.Double.NEGATIVE_INFINITY;
import static org.assertj.core.api.Assertions.assertThat;

import org.meeuw.math.Interval;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class IntervalTest {

    @Test
    void closedOpen() {
        Interval<Integer> closedOpen = Interval.closedOpen(1, 10);
        assertThat(closedOpen).accepts(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertThat(closedOpen).rejects(0, 10, 20);
        assertThat(closedOpen.toString()).isEqualTo("[1,10)");
        assertThat(closedOpen.lowerEndpoint()).isEqualTo(1);
        assertThat(closedOpen.upperEndpoint()).isEqualTo(10);
    }

    @Test
    void openClosed() {
        Interval<Double> closedOpen = Interval.openClosed(0d, 10d);
        assertThat(closedOpen).accepts(0.1d, 2d, 3.333d, 10d);
        assertThat(closedOpen).rejects(-10d, -0d, 10.001d, 11d);
        assertThat(closedOpen.toString()).isEqualTo("(0.0,10.0]");
    }

    @Test
    void open() {
        Interval<Double> open = Interval.open(null, 10d);
        assertThat(open).accepts(NEGATIVE_INFINITY, 0.1d, 2d, 3.333d, 9.9999d);
        assertThat(open).rejects(10.001d, 11d, Double.POSITIVE_INFINITY);
        assertThat(open.toString()).isEqualTo("(-∞,10.0)");
    }

    @Test
    void closed() {
        Interval<Double> open = Interval.closed(NEGATIVE_INFINITY, 10d);
        assertThat(open).accepts(NEGATIVE_INFINITY, 0.1d, 2d, 3.333d, 9.9999d);
        assertThat(open).rejects(10.001d, 11d, Double.POSITIVE_INFINITY);
        assertThat(open.toString()).isEqualTo("[-Infinity,10.0]");
    }

    @Test
    void lowerEndPointComparator() {

        Interval<Double> i0 = Interval.open(null, 10d);

        Interval<Double> i1 = Interval.closed(NEGATIVE_INFINITY, 10d);
        Interval<Double> i2 = Interval.closed(-10d, 10d);
        Interval<Double> i3 = Interval.open(-10d, 10d);


        SortedSet<Interval<Double>> set = new TreeSet<>(Interval.lowerEndPointComparator());
        set.addAll(Arrays.asList(i2, i1, i3, i0));
        assertThat(set).containsExactly(i0, i1, i2, i3);
    }


    @Test
    void upperEndPointComparator() {


        Interval<Double> i0 = Interval.open(-10d, 10d);
        Interval<Double> i1 = Interval.closed(-10d, 10d);
        Interval<Double> i2 = Interval.closed(0d, Double.POSITIVE_INFINITY);
        Interval<Double> i3 = Interval.open(null, null);

        SortedSet<Interval<Double>> set = new TreeSet<>(Interval.upperEndPointComparator());
        set.addAll(Arrays.asList(i2, i1, i3, i0));
        assertThat(set).containsExactly(i0, i1, i2, i3);
    }

}
