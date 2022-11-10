package org.meeuw.test.math.numbers;

import ch.obermuhlner.math.big.BigDecimalMath;
import lombok.extern.log4j.Log4j2;

import java.math.*;
import java.time.Duration;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {"-200", "-150.5", "-2", "-0.5", "0", "0.5", "2", "150.5", "200"})
    public void pow(String exp) {
        final BigDecimal value = new BigDecimal("200");
        final BigDecimal exponent = new BigDecimal(exp);
        final MathContext context = new MathContext(6);
        long count = 10000;
        {
            BigDecimal result = null;
            long nano = System.nanoTime();
            for (int i = 0; i < count; i++) {
                result = BigDecimalMath.pow(value, exponent, context);
            }
            log.info("BigDecimalMath: {}^{}={}\n({} /calc)", value, exponent, result, Duration.ofNanos(System.nanoTime() - nano).dividedBy(count));
        }
        {
            BigDecimal result = null;
            long nano = System.nanoTime();
            for (int i = 0; i < count; i++) {
                result = BigDecimalUtils.pow(value, exponent, context);
            }
            log.info("BigDecimalUtils: {}^{}={}\n({} /calc)\n", value, exponent, result, Duration.ofNanos(System.nanoTime() - nano).dividedBy(count));
        }
    }



	@Test
	public void testPowLargeNegative() {
		BigDecimal pow = BigDecimalMath.pow(new BigDecimal("200"), new BigDecimal("-200"), new MathContext(6));

        assertThat(pow).isEqualTo("6.22302E-461");


    }

    @Test
	public void testSqrt() {
		BigDecimal sqrt = BigDecimalMath.sqrt(new BigDecimal("1.23e-160"), new MathContext(6));

        assertThat(sqrt).isEqualTo("1.10905E-80");


    }


}
