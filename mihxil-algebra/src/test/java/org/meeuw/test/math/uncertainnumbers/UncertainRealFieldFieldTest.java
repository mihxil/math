/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.uncertainnumbers;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.reals.*;
import org.meeuw.math.abstractalgebra.test.CompleteScalarFieldTheory;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.uncertainnumbers.field.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.configuration.ConfigurationService.withAspect;
import static org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement.exactly;
import static org.meeuw.math.uncertainnumbers.field.UncertainRealField.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class UncertainRealFieldFieldTest implements CompleteScalarFieldTheory<UncertainReal> {

    @Test
    public void testToString() {
        UncertainDoubleElement uncertainDouble = new UncertainDoubleElement(5, 1);
        assertThat(uncertainDouble.toString()).isEqualTo("5.0 ± 1.0");
    }

    @Test
    public void pow() {
        UncertainDoubleElement w = new UncertainDoubleElement(-1971, 680);
        assertThat(w.pow(-2).doubleUncertainty()).isPositive();
    }

    @Test
    public void determinant2() {
         UncertainDoubleElement[][] realNumbers = new UncertainDoubleElement[][] {
            new UncertainDoubleElement[]{UncertainDoubleElement.exactly(1), UncertainDoubleElement.exactly(2)},
            new UncertainDoubleElement[]{UncertainDoubleElement.exactly(3), UncertainDoubleElement.exactly(4)},
        };

        assertThat(UncertainRealField.INSTANCE.determinant(realNumbers))
            .isEqualTo(UncertainDoubleElement.exactly(-2));
    }

    @Property
    public void errorPropagation(
        @ForAll("bigdecimals") final BigDecimal r1,
        @ForAll("bigdecimals") final BigDecimal r2,
        @ForAll("operators") final BasicAlgebraicBinaryOperator operator) {

        withAspect(UncertaintyConfiguration.class,
            (nc) -> nc.withConsiderRoundingErrorFactor(0), () -> {
                BigDecimal big2 = r2;
                if (operator == BasicAlgebraicBinaryOperator.POWER) {
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
        return Arbitraries.randomValue(INSTANCE::nextRandom);
    }


    @Provide
    public Arbitrary<AlgebraicBinaryOperator> operators() {
        return Arbitraries.of(INSTANCE.getSupportedOperators());
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
