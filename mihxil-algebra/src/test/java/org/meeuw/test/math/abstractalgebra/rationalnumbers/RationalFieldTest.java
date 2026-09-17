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
package org.meeuw.test.math.abstractalgebra.rationalnumbers;

import lombok.extern.java.Log;

import java.math.BigDecimal;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.jupiter.WithUncertaintyConfiguration;
import org.meeuw.math.abstractalgebra.bigdecimals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.theories.abstractalgebra.FieldTheory;
import org.meeuw.theories.abstractalgebra.SignedNumberTheory;
import org.meeuw.theories.numbers.ScalarTheory;

import static java.math.BigInteger.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber.*;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;

/**
 * @author Michiel Meeuwissen
 */
@Log
@WithUncertaintyConfiguration
//@SetNumberConfiguration
class RationalFieldTest implements FieldTheory<RationalNumber>,
    ScalarTheory<RationalNumber>,
    SignedNumberTheory<RationalNumber> {


    @Test
    void string() {
        assertThat(of(1).toString()).isEqualTo("1");
        RationalNumber half  = of(1).dividedBy(of(2));
        assertThat(half.toString()).isEqualTo("¹⁄₂");
    }

    @Test
    void minus() {
        assertThat(of(1).minus(of(0))).isEqualTo(of(1));
    }

    @Test
    void approx() {
        assertThat(INSTANCE.approx(BigDecimalElement.of(new BigDecimal("1.25"))))
            .isEqualTo(of(5, 4));
        assertThat(INSTANCE.approx(BigDecimalElement.of(new BigDecimal("100.0"))))
            .isEqualTo(of(100));
    }

    @Test
    void floor() {
        assertThat(RationalNumber.of(5, 4).floor()).isEqualTo(RationalNumber.of(1));
        assertThat(RationalNumber.of(-5, 4).floor()).isEqualTo(RationalNumber.of(0));
    }


    @Test
    void adjugate() {
        RationalNumber[][] realNumbers = new RationalNumber[][] {
            new RationalNumber[]{of(-3), of(2), of(-5)},
            new RationalNumber[]{of(-1), of(0), of(-2)},
            new RationalNumber[]{of(3), of(-4), of(-1)}
        };

        assertThat(INSTANCE.adjugate(realNumbers)).isDeepEqualTo(
            new RationalNumber[][] {
                new RationalNumber[]{of(-8), of(22), of(-4)},
                new RationalNumber[]{of(-7), of(18), of(-1)},
                new RationalNumber[]{of(4), of(-6), of(2)}
            }
        );
    }

    @Test
    void determinant() {
        // https://planetcalc.com/8351/
        RationalNumber[][] realNumbers = new RationalNumber[][] {
            new RationalNumber[]{of(-3), of(2), of(-5)},
            new RationalNumber[]{of(-1), of(0), of(-2)},
            new RationalNumber[]{of(3), of(-4), of(-1)}
        };

        assertThat(INSTANCE.determinant(realNumbers)).isEqualTo(of(-10));
    }

    @Test
    void determinant2() {
        RationalNumber[][] realNumbers = new RationalNumber[][] {
            new RationalNumber[]{of(1), of(2)},
            new RationalNumber[]{of(3), of(4)},
        };

        assertThat(INSTANCE.determinant(realNumbers)).isEqualTo(of(-2));
    }

    @Test
    void test() {
        assertThatThrownBy(() -> RationalNumber.of(null, valueOf(1))).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> RationalNumber.of(valueOf(1L), null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> RationalNumber.of(valueOf(1L), valueOf(0))).isInstanceOf(InvalidElementCreationException.class);
        assertThat(of(valueOf(1L), valueOf(4L))
            .times(of(1, 2))).isEqualTo(of(1, 8));

        assertThat(of(2, 5).times(of(1, 2))).isEqualTo(of(1, 5));

        assertThat(of(3, 7).reciprocal()).isEqualTo(of(7, 3));
        assertThat(of(3, 7).reciprocal().times(of(3, 7))).isEqualTo(INSTANCE.one());

        assertThat(of(3, 7).plus(INSTANCE.zero())).isEqualTo(of(6, 14));

        assertThat(of(10).dividedBy(of(3))).isEqualTo(of(10, 3));
        assertThat(of(1).dividedBy(of(3))).isEqualTo(of(1, 3));

        assertThatThrownBy(() -> of(10, 0)).isInstanceOf(InvalidElementCreationException.class);
    }



    @Test
    void stream() {
        assertThat(INSTANCE
            .stream()
            .limit(30)
            .map(RationalNumber::toString))
            .containsExactly(
                "0",
                "1",
                "-1",
                "2",
                "-2",
                "¹⁄₂",
                "-¹⁄₂",
                "3",
                "-3",
                "¹⁄₃",
                "-¹⁄₃",
                "4",
                "-4",
                "³⁄₂",
                "-³⁄₂",
                "²⁄₃",
                "-²⁄₃",
                "¹⁄₄",
                "-¹⁄₄",
                "5",
                "-5",
                "¹⁄₅",
                "-¹⁄₅",
                "6",
                "-6",
                "⁵⁄₂",
                "-⁵⁄₂",
                "⁴⁄₃",
                "-⁴⁄₃",
                "³⁄₄"
            );
    }
    @Test
    void reverseStream() {
        assertThat(INSTANCE.reverseStream(10)).map(RationalNumber::toString).containsExactly(
            "¹⁄₃", "-3", "3", "-¹⁄₂", "¹⁄₂", "-2", "2", "-1", "1", "0"
        );
    }
    @Test
    void all() {
        INSTANCE.stream().limit(100).forEach(i -> {
            log.info(i.toString() + ":" + i.bigDecimalValue());
        });
    }


    @Override
    @Provide
    public Arbitrary<RationalNumber> elements() {
        return
            Arbitraries.randomValue(
                    INSTANCE::nextRandom
                )
                .injectDuplicates(0.1)
                .dontShrink()
                .edgeCases(realNumberConfig -> {
                    realNumberConfig.add(of(0));
                    realNumberConfig.add(of(-1));
                    realNumberConfig.add(ONE);
                    realNumberConfig.add(ZERO);
                    realNumberConfig.add(of(1));
                })
            ;
    }
}
