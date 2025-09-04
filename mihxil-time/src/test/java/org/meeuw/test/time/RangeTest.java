package org.meeuw.test.time;

import java.time.Year;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.Test;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.theories.BasicObjectTheory;
import org.meeuw.time.Range;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RangeTest {

    @Test
    void ofYears() {
        Range<Year> range = Range.ofYears(2000, 2005);
        assertThat(range.getStart()).isEqualTo(Year.of(2000));
        assertThat(range.getEnd()).isEqualTo(Year.of(2005));
        assertThat(range.forward()).isTrue();
        assertThat(Range.stream(range)).containsExactly(
            Year.of(2000),
            Year.of(2001),
            Year.of(2002),
            Year.of(2003),
            Year.of(2004),
            Year.of(2005)
        );
        assertThat(range.test(Year.of(1999))).isFalse();
        assertThat(range.test(Year.of(2003))).isTrue();
        assertThat(range.test(Year.of(2005))).isTrue();
        assertThat(range.test(Year.of(2006))).isFalse();

        assertThat(range.encloses(Range.ofYears(2000, 2005))).isTrue();
        assertThat(range.encloses(Range.ofYears(1999, 2005))).isFalse();
        assertThat(range.encloses(Range.ofYears(1999, 2006))).isFalse();
        assertThat(range.encloses(Range.ofYears(2001, 2004))).isTrue();

    }

    @Test
    void ofYear() {
        Range<Year> range = Range.ofYear(2025);
        assertThat(range.getStart()).isEqualTo(Year.of(2025));
        assertThat(range.getEnd()).isEqualTo(Year.of(2025));
        assertThat(range.forward()).isTrue();
        assertThat(Range.stream(range)).containsExactly(
            Year.of(2025)
        );
        assertThat(range.isConnected(Range.fromYear(2026))).isFalse();
        assertThat(Range.fromYear(2026).isConnected(range)).isFalse();

        assertThat(range.isConnected(Range.untilYear(2026))).isTrue();
        assertThat(Range.untilYear(2026).isConnected(range)).isTrue();

    }

    @Test
    void fromYear() {
        Range<Year> range = Range.fromYear(2025, false);
        assertThat(range.getStart()).isEqualTo(Year.of(2025));
        assertThat(range.getEnd()).isNull();
        assertThat(range.forward()).isFalse();
        assertThat(Range.stream(range).limit(10)).containsExactly(
            Year.of(2025),
            Year.of(2024),
            Year.of(2023),
            Year.of(2022),
            Year.of(2021),
            Year.of(2020),
            Year.of(2019),
            Year.of(2018),
            Year.of(2017),
            Year.of(2016)
        );
        assertThat(range.isConnected(Range.fromYear(2026))).isFalse();
        assertThat(Range.fromYear(2026).isConnected(range)).isFalse();

        assertThat(range.isConnected(Range.untilYear(2026))).isTrue();
        assertThat(Range.untilYear(2026).isConnected(range)).isTrue();
    }

    @Test
    public void illegal() {
        assertThatThrownBy(() -> new Range<>(null, 2020)).isInstanceOf(IllegalArgumentException.class);
    }


    public static class YearRangeTest implements BasicObjectTheory<Range<Year>> {

        @Override
        public Arbitrary<@NonNull Range<Year>> datapoints() {
            return Arbitraries.integers().between(1900, 2100)
                .flatMap(start ->
                    Arbitraries.integers()
                        .between(1900, 2100)
                        .injectNull(0.1)
                    .map(end -> Range.ofYears(start, end))
                );
        }
    }
    public static class DoubleRangeTest implements BasicObjectTheory<Range<Double>> {

        @Override
        public Arbitrary<@NonNull Range<Double>> datapoints() {
            return Arbitraries.doubles().between(-100, 100)
                .flatMap(start ->
                    Arbitraries.doubles()
                        .between(-100, 100)
                        .injectNull(0.1)
                        .map(end -> new Range<>(start, end))
                );
        }
    }


}
