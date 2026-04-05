package org.meeuw.test.math;

import lombok.extern.java.Log;

import java.math.BigInteger;

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

    @Test
    public void pow10() {
        assertThat(DoubleUtils.pow10(0)).isEqualTo(1.0);
        assertThat(DoubleUtils.pow10(1)).isEqualTo(10.0);
        assertThat(DoubleUtils.pow10(3)).isEqualTo(1000.0);
        assertThat(DoubleUtils.pow10(-1)).isEqualTo(0.1);
        assertThat(DoubleUtils.pow10(-3)).isEqualTo(0.001);
    }

    @Test
    public void pow2() {
        assertThat(DoubleUtils.pow2(0)).isEqualTo(1.0);
        assertThat(DoubleUtils.pow2(1)).isEqualTo(2.0);
        assertThat(DoubleUtils.pow2(10)).isEqualTo(1024.0);
        assertThat(DoubleUtils.pow2(-1)).isEqualTo(0.5);
        assertThat(DoubleUtils.pow2(-10)).isEqualTo(1.0 / 1024.0);
    }

    @Test
    public void powIntBase() {
        assertThat(DoubleUtils.pow(3, 0)).isEqualTo(1.0);
        assertThat(DoubleUtils.pow(3, 1)).isEqualTo(3.0);
        assertThat(DoubleUtils.pow(3, 3)).isEqualTo(27.0);
        assertThat(DoubleUtils.pow(3, -1)).isEqualTo(1.0 / 3.0);
        assertThat(DoubleUtils.pow(2, -3)).isEqualTo(0.125);
    }

    @Test
    public void powDoubleBase() {
        assertThat(DoubleUtils.pow(0.0, 5)).isEqualTo(1.0);
        assertThat(DoubleUtils.pow(2.0, 0)).isEqualTo(1.0);
        assertThat(DoubleUtils.pow(2.5, 2)).isEqualTo(6.25);
        assertThat(DoubleUtils.pow(4.0, -1)).isEqualTo(0.25);
    }

    @Test
    public void max() {
        assertThat(DoubleUtils.max(1.0, 3.0, 2.0)).isEqualTo(3.0);
        assertThat(DoubleUtils.max(-5.0, -1.0, -3.0)).isEqualTo(-1.0);
        assertThat(DoubleUtils.max(Double.NEGATIVE_INFINITY, 0.0)).isEqualTo(0.0);
        assertThat(DoubleUtils.max(7.0)).isEqualTo(7.0);
        assertThat(DoubleUtils.max(Double.POSITIVE_INFINITY, 1.0)).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @Test
    public void leastSignificantBit() {
        assertThat(DoubleUtils.leastSignificantBit(1.0)).isEqualTo(-51);
        assertThat(DoubleUtils.leastSignificantBit(2.0)).isEqualTo(-50);
        assertThat(DoubleUtils.leastSignificantBit(-1.0)).isEqualTo(-51);
    }

    @Test
    public void isExactProductDoubles() {
        assertThat(DoubleUtils.isExactProduct(2.0, 3.0)).isTrue();
        assertThat(DoubleUtils.isExactProduct(0.5, 4.0)).isTrue();
        assertThat(DoubleUtils.isExactProduct(0.0, 999.0)).isTrue();
        assertThat(DoubleUtils.isExactProduct(1.0, 0.0)).isTrue();

        assertThat(DoubleUtils.isExactProduct(Double.NaN, 1.0)).isFalse();
        assertThat(DoubleUtils.isExactProduct(1.0, Double.NaN)).isFalse();
        assertThat(DoubleUtils.isExactProduct(Double.POSITIVE_INFINITY, 1.0)).isFalse();
        assertThat(DoubleUtils.isExactProduct(1.0, Double.NEGATIVE_INFINITY)).isFalse();

        // 0.1 * 0.2 is not exactly representable in IEEE 754
        assertThat(DoubleUtils.isExactProduct(0.1, 0.2)).isFalse();
    }

    @Test
    public void isExactProductBigInteger() {
        assertThat(DoubleUtils.isExactProduct(2.0, BigInteger.valueOf(3))).isTrue();
        assertThat(DoubleUtils.isExactProduct(0.5, BigInteger.valueOf(4))).isTrue();
        assertThat(DoubleUtils.isExactProduct(0.0, BigInteger.valueOf(999))).isTrue();
        assertThat(DoubleUtils.isExactProduct(1.0, BigInteger.ZERO)).isTrue();

        assertThat(DoubleUtils.isExactProduct(Double.NaN, BigInteger.ONE)).isFalse();
        assertThat(DoubleUtils.isExactProduct(Double.POSITIVE_INFINITY, BigInteger.ONE)).isFalse();
        assertThat(DoubleUtils.isExactProduct(1.0, null)).isFalse();
    }

}
