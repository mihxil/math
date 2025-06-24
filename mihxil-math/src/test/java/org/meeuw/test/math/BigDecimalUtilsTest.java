package org.meeuw.test.math;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.BigDecimalUtils;
import org.meeuw.math.exceptions.IllegalPowerException;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class BigDecimalUtilsTest {


    @Test
    public void pow() {
        Assertions.assertThatThrownBy(()-> {
            BigDecimalUtils.pow(BigDecimal.ZERO, -1, MathContext.DECIMAL128);
        }).isInstanceOf(IllegalPowerException.class);
        assertThat(BigDecimalUtils.pow(BigDecimal.ZERO, 1, MathContext.DECIMAL128)).isEqualTo(BigDecimal.ONE);
    }


    @Test
    public void uncertaintyForBigDecimal() {
        assertThat(BigDecimalUtils.uncertaintyForBigDecimal(BigDecimal.TEN, MathContext.DECIMAL128)).isEqualTo(BigDecimal.ZERO);
        assertThat(BigDecimalUtils.uncertaintyForBigDecimal(new BigDecimal("0.123"), MathContext.DECIMAL32)).isEqualTo(new BigDecimal("1E-7"));

        assertThat(BigDecimalUtils.uncertaintyForBigDecimal(new BigDecimal("0.123456"), new MathContext(2))).isEqualTo(new BigDecimal("0.01"));


    }

}
