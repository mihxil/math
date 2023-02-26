package org.meeuw.test.math;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import org.meeuw.math.BigDecimalUtils;
import org.meeuw.math.exceptions.IllegalPowerException;

@Log4j2
public class BigDecimalUtilsTest {


    @Test
    public void pow() {
        Assertions.assertThatThrownBy(()-> {
            BigDecimalUtils.pow(BigDecimal.ZERO, -1, MathContext.DECIMAL128);
        }).isInstanceOf(IllegalPowerException.class);
    }
}
