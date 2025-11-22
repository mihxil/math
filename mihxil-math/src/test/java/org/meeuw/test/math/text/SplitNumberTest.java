package org.meeuw.test.math.text;

import org.junit.jupiter.api.Test;

import org.meeuw.math.text.SplitNumber;

import static org.assertj.core.api.Assertions.assertThat;

class SplitNumberTest {


    @Test
    public void splitSmallNumber() {
        SplitNumber<Double> split = SplitNumber.split(6.62607015E-34).orElseThrow();
        assertThat(split.coefficient).isEqualTo(6.62607015);
    }

}
