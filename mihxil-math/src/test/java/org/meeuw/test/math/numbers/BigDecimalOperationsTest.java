package org.meeuw.test.math.numbers;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.jupiter.api.Test;

import org.meeuw.math.numbers.BigDecimalOperations;
import org.meeuw.math.numbers.MathContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
class BigDecimalOperationsTest {

    BigDecimalOperations operations = BigDecimalOperations.INSTANCE;
    @Test
    public void uncertaintyContextTest() {
        operations.withUncertaintyContext(() -> {
            MathContext mc = MathContextConfiguration.get().getContext();
            assertThat(mc).isSameAs(MathContextConfiguration.get().getUncertaintyContext());

            BigDecimal one = new BigDecimal("1.00000000001");
            BigDecimal two = new BigDecimal("1.00000000002");
            BigDecimal dif = one.sqrt(mc).min(two.sqrt(mc));
            log.info("{}", dif);
            return null;
        });
    }

}
