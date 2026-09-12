/*
 *  Copyright 2026 Michiel Meeuwissen
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
package org.meeuw.test.math.abstractalgebra.rationalnumbers.text;

import java.math.MathContext;

import org.junit.jupiter.api.Test;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.text.RationalNumberConfiguration.Mode;
import org.meeuw.math.abstractalgebra.rationalnumbers.text.RationalNumberFormat;
import org.meeuw.math.numbers.MathContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber.of;

class RationalNumberFormatTest {

    @Test
    void integerAndFraction() {
        RationalNumberFormat format = new RationalNumberFormat(Mode.INTEGER_AND_FRACTION);

        assertThat(format.format(of(7, 3))).isEqualTo("2¹⁄₃");
        assertThat(format.format(of(7, 1))).isEqualTo("7");
        assertThat(format.format(of(-7, 1))).isEqualTo("-7");
        assertThat(format.format(of(-7, 3))).isEqualTo("-2¹⁄₃");
        assertThat(format.format(of(-1, 3))).isEqualTo("-¹⁄₃");
        assertThat(format.format(RationalNumber.ZERO)).isEqualTo("0");
    }

    @Test
    void decimalWithRepeat() {
        RationalNumberFormat format = new RationalNumberFormat(Mode.DECIMAL_WITH_REPEAT);

        assertThat(format.format(of(1, 8))).isEqualTo("0.125");
        assertThat(format.format(of(-1, 6))).isEqualTo("-0.16̅");
        assertThat(format.format(of(22, 7))).isEqualTo("3.1̅4̅2̅8̅5̅7̅");
    }

    @Test
    void roundedDecimalUsesTheMathContext() {
        RationalNumberFormat format = new RationalNumberFormat(Mode.DECIMAL_ROUNDED);

        ConfigurationService.withAspect(
            MathContextConfiguration.class,
            configuration -> configuration.withContext(new MathContext(3)),
            () -> assertThat(format.format(of(1, 3))).isEqualTo("0.333")
        );
    }
}
