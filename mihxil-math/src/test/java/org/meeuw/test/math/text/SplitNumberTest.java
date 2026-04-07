package org.meeuw.test.math.text;

import lombok.extern.java.Log;

import java.util.Optional;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assumptions;

import org.meeuw.math.text.SplitNumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.numbers.DoubleOperations.INSTANCE;

@Log
class SplitNumberTest {

    @Test
    public void splitSmallNumber() {
        SplitNumber<Double> split = INSTANCE.split(6.62607015E-34).orElseThrow();
        assertThat(split.coefficient).isEqualTo(6.62607015);
        assertThat(split.exponent).isEqualTo(-34);
    }
    @Test
    public void splitVerySmallNumber() {
        SplitNumber<Double> split = INSTANCE.split(1.0E-323).orElseThrow();
        assertThat(split.coefficient).isEqualTo(1.0);
        assertThat(split.exponent).isEqualTo(-323);
    }

    @Test
    public void splitCloseToOne() {
        SplitNumber<Double> split = INSTANCE.split(1.6260701).orElseThrow();
        assertThat(split.coefficient).isEqualTo(1.6260701);
        assertThat(split.exponent).isEqualTo(0);
    }

    @Property
    public void validate(@ForAll() double value) {
        Optional<SplitNumber<Double>> split = INSTANCE.split(value);


        log.info(() -> value + "->" + split);
        Assumptions.assumeThat(split).isPresent();


        assertThat(Math.abs(split.get().coefficient)).isGreaterThanOrEqualTo(1.0);
        assertThat(Math.abs(split.get().coefficient)).isLessThan(10.0);
    }

}
