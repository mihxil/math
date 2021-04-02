package org.meeuw.test.math.uncertainnumbers;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.Operator;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.test.CompleteFieldTheory;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.text.spi.FormatService;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.assertj.core.api.Assertions.assertThat;

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
        @ForAll("bigdecimals") BigDecimal r1,
        @ForAll("bigdecimals") BigDecimal r2, @ForAll Operator operator) {
        FormatService.with(
            (configurationBuilder) -> configurationBuilder.aspect(UncertaintyConfiguration.class, (nc) -> nc.withConsiderRoundingErrorFactor(0)), () -> {

                UncertainReal a = UncertainDoubleElement.exact(r1.doubleValue());
                UncertainReal b = UncertainDoubleElement.exact(r2.doubleValue());

                UncertainReal applied = operator.apply(a, b);
                log.info("{} = {}", operator.stringify(a, b), applied);
                BigDecimalElement ba = BigDecimalElement.of(r1);
                BigDecimalElement bb = BigDecimalElement.of(r2);
                BigDecimalElement exactApplied = operator.apply(ba, bb);
                log.info("{} = {}", operator.stringify(ba, bb), exactApplied);

                assertThat(applied.equals(UncertainDoubleElement.exact(exactApplied.doubleValue()))).isTrue();
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
            return new BigDecimal(a  + "." + b);
        });
    }
}
