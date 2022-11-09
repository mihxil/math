package org.meeuw.test.math.numbers;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.extern.log4j.Log4j2;

import java.math.*;
import java.time.Duration;

import org.junit.jupiter.api.Test;

import org.meeuw.math.BigDecimalUtils;
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

    @Test
    public void pow() {
        BigDecimal value = new BigDecimal(200);
        BigDecimal exponent = new BigDecimal("-100.1");
        MathContext context = new MathContext(6);
        long count = 10000;
        {
            BigDecimal result = null;
            long nano = System.nanoTime();
            for (int i = 0; i < count; i++) {
                result = powBDM(value, exponent, context);
            }
            log.info("{}^{}={}\n({} /calc)", value, exponent, result, Duration.ofNanos(System.nanoTime() - nano).dividedBy(count));
        }
        {
            BigDecimal result = null;
            long nano = System.nanoTime();
            for (int i = 0; i < count; i++) {
                result = BigDecimalUtils.pow(value, exponent, context);
            }
            log.info("{}^{}={}\n({} /calc)", value, exponent, result, Duration.ofNanos(System.nanoTime() - nano).dividedBy(count));
        }


    }

    /**
     *
     */
    public BigDecimal powBDM(BigDecimal value, BigDecimal exponent, MathContext context) {
        return BigDecimalMath.pow(value, exponent, context);
    }




}
