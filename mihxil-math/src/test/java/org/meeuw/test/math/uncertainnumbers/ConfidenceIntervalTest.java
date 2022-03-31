package org.meeuw.test.math.uncertainnumbers;

import org.junit.jupiter.api.Test;

import org.meeuw.math.uncertainnumbers.ConfidenceInterval;

import static org.assertj.core.api.Assertions.assertThat;

class ConfidenceIntervalTest {

    @Test
    void ofAndToString() {
        ConfidenceInterval<Double> of = ConfidenceInterval.of(10d, 2d, 2);
        assertThat(of.toString()).isEqualTo("(6.0,14.0)");
    }
}
