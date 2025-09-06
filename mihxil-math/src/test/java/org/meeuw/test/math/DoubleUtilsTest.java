package org.meeuw.test.math;

import lombok.extern.java.Log;

import org.junit.jupiter.api.Test;

import org.meeuw.math.DoubleUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Log
public class DoubleUtilsTest {


    @Test
    public void uncertaintyForDouble() {
        assertThat(DoubleUtils.uncertaintyForDouble(0)).isEqualTo(4.9E-324);
        assertThat(DoubleUtils.uncertaintyForDouble(1e-300)).isEqualTo(3.31561842E-316);
        assertThat(DoubleUtils.uncertaintyForDouble(1e-100)).isEqualTo(2.5379418373156492E-116);
        assertThat(DoubleUtils.uncertaintyForDouble(1e-16)).isEqualTo(2.465190328815662E-32);
        assertThat(DoubleUtils.uncertaintyForDouble(-1)).isEqualTo(4.440892098500626E-16);
        assertThat(DoubleUtils.uncertaintyForDouble(1)).isEqualTo(4.440892098500626E-16);
    }


    @Test
    public void round() {
        assertThat(DoubleUtils.round(1.2)).isEqualTo(1);
        assertThat(DoubleUtils.round(1.5)).isEqualTo(2);
        assertThat(DoubleUtils.round(-1.5)).isEqualTo(-1);
        assertThat(DoubleUtils.round(Double.POSITIVE_INFINITY)).isEqualTo(Long.MAX_VALUE);
        assertThat(DoubleUtils.round(Double.NEGATIVE_INFINITY)).isEqualTo(Long.MIN_VALUE);

        assertThatThrownBy(() -> DoubleUtils.round(Double.NaN)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    public void log10() {
        long start = System.currentTimeMillis();
        int d = 0;
        for (int i = 0; i < 1000000L; i++) {
            d = DoubleUtils.log10(123456789);
        }
        log.info("%s : %s".formatted(d, System.currentTimeMillis() - start));
        assertThat(DoubleUtils.log10(10d)).isEqualTo(1);
        assertThat(DoubleUtils.log10(20d)).isEqualTo(1);
    }


    @Test
    public void implicitUncertainty() {
        assertThat(DoubleUtils.implicitUncertaintyForDouble(1, "1")).isEqualTo(0.5d);
        assertThat(DoubleUtils.implicitUncertaintyForDouble(1, "1.00")).isEqualTo(0.005d);
        assertThat(DoubleUtils.implicitUncertaintyForDouble(0.1, "0.01")).isEqualTo(0.005d);


    }

}
