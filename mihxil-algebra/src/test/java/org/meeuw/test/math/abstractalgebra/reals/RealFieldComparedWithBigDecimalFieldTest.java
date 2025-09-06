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
package org.meeuw.test.math.abstractalgebra.reals;

import lombok.extern.java.Log;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.IllegalPowerException;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.configuration.ConfigurationService.withAspect;
import static org.meeuw.math.abstractalgebra.reals.DoubleElement.exactly;
import static org.meeuw.math.abstractalgebra.reals.RealField.INSTANCE;


/**
 * This just adds some extra tests for {@link org.meeuw.math.abstractalgebra.reals.RealField} where we also use {@link BigDecimalElement}
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 *
 */
@Log
class RealFieldComparedWithBigDecimalFieldTest {

    public Arbitrary<RealNumber> elements() {
        return Arbitraries
            .<RealNumber>randomValue(INSTANCE::nextRandom)
            .dontShrink()
            .edgeCases(c -> {
                c.add(RealNumber.ONE);
                c.add(RealNumber.ZERO);
            });
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


    @Property
    public void errorPropagation(
        @ForAll("bigdecimals") final BigDecimal r1,
        @ForAll("bigdecimals") final BigDecimal r2,
        @ForAll("operators") final AlgebraicBinaryOperator operator) {

        withAspect(UncertaintyConfiguration.class,
            (nc) -> nc.withConsiderRoundingErrorFactor(0), () -> {
                BigDecimal big2 = r2;
                if (operator == BasicAlgebraicBinaryOperator.POWER) {
                    big2 = big2.divide(BigDecimal.valueOf(100));
                }
                RealNumber a = exactly(r1.doubleValue());
                RealNumber b = exactly(big2.doubleValue());
                BigDecimalElement ba = BigDecimalElement.of(r1);
                BigDecimalElement bb = BigDecimalElement.of(big2);

                try {
                    RealNumber applied = operator.apply(a, b);
                    log.info( operator.stringify(a, b) + "=" +  applied);

                    BigDecimalElement exactApplied = operator.apply(ba, bb);
                    log.info(operator.stringify(ba, bb) + "=" + exactApplied);

                    assertThat(applied.eq(exactly(exactApplied.doubleValue()))).isTrue();
                } catch (ReciprocalException | IllegalPowerException rce) {
                    log.info(operator.stringify(ba, bb) + "->" + rce.getMessage());
                }
            });

    }



}
