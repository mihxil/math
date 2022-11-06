package org.meeuw.test.math.uncertainnumbers;

import org.junit.jupiter.api.Test;

import org.meeuw.math.uncertainnumbers.DoubleConfidenceInterval;

import static org.assertj.core.api.Assertions.assertThat;

class DoubleConfidenceIntervalTest {

    @Test
    void ofAndToString() {
        DoubleConfidenceInterval of = DoubleConfidenceInterval.of(10d, 2d, 2);
        assertThat(of.toString()).isEqualTo("[6.0,14.0]");
    }

    @Test
    public void test() {
        DoubleConfidenceInterval of = DoubleConfidenceInterval.of(10d, 2d, 2);
        assertThat(of.test(11d)).isTrue();
        assertThat(of.test(14d)).isTrue();
        assertThat(of.test(14.0000001d)).isFalse();
        assertThat(of.test(5d)).isFalse();
    }

}
