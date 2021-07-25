package org.meeuw.test.math.uncertainnumbers;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.Operator;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.test.CompleteFieldTheory;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.text.spi.FormatService;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class UncertainRealFieldFieldTest implements CompleteFieldTheory<UncertainReal> {

    @Test
    public void testToString() {
        UncertainDoubleElement uncertainDouble = new UncertainDoubleElement(5, 1);
        assertThat(uncertainDouble.toString()).isEqualTo("5.0 ± 1.0");
    }

    @Test
    public void pow() {
        UncertainDoubleElement w = new UncertainDoubleElement(-1971, 680);
        assertThat(w.pow(-2).getUncertainty()).isPositive();
    }

    @Property
    public void errorPropagation(
        @ForAll("bigdecimals") final BigDecimal r1,
        @ForAll("bigdecimals") final BigDecimal r2,
        @ForAll Operator operator) {

        FormatService.with(
            (configurationBuilder) -> configurationBuilder.aspect(UncertaintyConfiguration.class, (nc) -> nc.withConsiderRoundingErrorFactor(0)), () -> {
                BigDecimal big2 = r2;
                if (operator == Operator.POWER) {
                    big2 = big2.divide(BigDecimal.valueOf(100));
                }
                UncertainReal a = exactly(r1.doubleValue());
                UncertainReal b = exactly(big2.doubleValue());

                UncertainReal applied = operator.apply(a, b);
                log.info("{} = {}", operator.stringify(a, b), applied);
                BigDecimalElement ba = BigDecimalElement.of(r1);
                BigDecimalElement bb = BigDecimalElement.of(big2);
                try {
                    BigDecimalElement exactApplied = operator.apply(ba, bb);
                    log.info("{} = {}", operator.stringify(ba, bb), exactApplied);

                    assertThat(applied.equals(exactly(exactApplied.doubleValue()))).isTrue();
                } catch (ReciprocalException rce) {
                    log.info("{} -> {}", operator.stringify(ba, bb), rce.getMessage());
                }
            });

    }

    @Override
    public Arbitrary<UncertainReal> elements() {
        return Arbitraries.randomValue(r -> {
            double value = 10000 * (r.nextDouble() - 0.5d);
            return new UncertainDoubleElement(value, Math.abs(value * r.nextDouble()));
        });
    }


    @Provide
    public Arbitrary<BigDecimal> bigdecimals() {
        return Arbitraries.randomValue(r -> {
            BigInteger a = new BigInteger(10, r);
            BigInteger b = new BigInteger(40, r);
            return new BigDecimal((r.nextBoolean() ? "+" : "-") + a  + "." + b);
        });
    }

}
