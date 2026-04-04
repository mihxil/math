package org.meeuw.test.math.text;

import lombok.extern.java.Log;

import java.util.Optional;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.junit.jupiter.api.Test;

import org.meeuw.math.text.SplitNumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@Log
class SplitNumberTest {



    @Test
    public void splitSmallNumber() {
        SplitNumber<Double> split = SplitNumber.split(6.62607015E-34).orElseThrow();
        assertThat(split.coefficient).isEqualTo(6.62607015);
        assertThat(split.exponent).isEqualTo(-34);
    }

    @Test
    public void splitCloseToOne() {
        SplitNumber<Double> split = SplitNumber.split(1.6260701).orElseThrow();
        assertThat(split.coefficient).isEqualTo(1.6260701);
        assertThat(split.exponent).isEqualTo(0);
    }

    @Property
    public void validate(@ForAll() double value) {
        Optional<SplitNumber<Double>> split = SplitNumber.split(value);

        log.info(() -> value + "->" + split);
        assumeThat(split).isPresent();

        assertThat(Math.abs(split.get().coefficient)).isGreaterThanOrEqualTo(1.0);
        assertThat(Math.abs(split.get().coefficient)).isLessThan(10.0);
    }

}
