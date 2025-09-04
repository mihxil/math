package org.meeuw.test.time;

import java.time.Year;

import org.junit.jupiter.api.Test;

import org.meeuw.time.Range;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(range.isConnected(Range.untilYear(2026))).isTrue();

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
    }



}
