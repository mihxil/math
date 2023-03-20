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

import lombok.extern.log4j.Log4j2;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.theories.abstractalgebra.FieldTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber.*;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers.INSTANCE;

/**
 * @author Michiel Meeuwissen
 */
@Log4j2
class RationalFieldTest implements FieldTheory<RationalNumber> {

    @Test
    public void string() {
        assertThat(of(1).toString()).isEqualTo("1");
        RationalNumber half  = of(1).dividedBy(of(2));
        assertThat(half.toString()).isEqualTo("¹⁄₂");
    }

    @Test
    public void minus() {
        assertThat(of(1).minus(of(0))).isEqualTo(of(1));
    }

    @Test
    public void adjugate() {
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
    public void determinant() {
        // https://planetcalc.com/8351/
        RationalNumber[][] realNumbers = new RationalNumber[][] {
            new RationalNumber[]{of(-3), of(2), of(-5)},
            new RationalNumber[]{of(-1), of(0), of(-2)},
            new RationalNumber[]{of(3), of(-4), of(-1)}
        };

        assertThat(INSTANCE.determinant(realNumbers)).isEqualTo(of(-10));
    }
    @Test
    public void determinant2() {
        RationalNumber[][] realNumbers = new RationalNumber[][] {
            new RationalNumber[]{of(1), of(2)},
            new RationalNumber[]{of(3), of(4)},
        };

        assertThat(INSTANCE.determinant(realNumbers)).isEqualTo(of(-2));
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
