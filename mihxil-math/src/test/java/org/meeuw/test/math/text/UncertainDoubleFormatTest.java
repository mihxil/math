/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.test.math.text;

import java.text.*;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.text.FormatService;
import org.meeuw.math.text.UncertainDoubleFormat;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class UncertainDoubleFormatTest {

    @BeforeAll
    public static void setup() {
        ConfigurationService.resetToDefaultDefaults();
    }

    @Test
    public void basic() {
        UncertainDoubleFormat formatter = new UncertainDoubleFormat();
        assertThat(formatter.scientificNotationWithUncertainty(5, 1)).isEqualTo("5.0 ± 1.0");
        assertThat(formatter.scientificNotationWithUncertainty(5, 2)).isEqualTo("5 ± 2");
        assertThat(formatter.scientificNotationWithUncertainty(5.1, 1.9)).isEqualTo("5.1 ± 1.9");
        assertThat(formatter.scientificNotationWithUncertainty(5.4e-20, 4.34e-22)).isEqualTo("(5.40 ± 0.04)·10⁻²⁰");
    }

    @Test
    public void numberFormat() {
        UncertainDoubleFormat formatter = new UncertainDoubleFormat();
        formatter.setNumberFormat(NumberFormat.getNumberInstance(new Locale("nl")));
        assertThat(formatter.scientificNotationWithUncertainty(5., 1.9)).isEqualTo("5,0 ± 1,9");
    }

    @Test
    public void zero() {
        UncertainDoubleFormat formatter = new UncertainDoubleFormat();
        assertThat(formatter.scientificNotationWithUncertainty(0, 0)).isEqualTo("0 ± 0");
    }

    @Test
    public void infinity() {
        UncertainDoubleFormat formatter = new UncertainDoubleFormat();
        assertThat(formatter.scientificNotationWithUncertainty(Double.POSITIVE_INFINITY, 0)).isEqualTo("∞");
        assertThat(formatter.scientificNotationWithUncertainty(Double.NEGATIVE_INFINITY, 0)).isEqualTo("-∞");
    }

    @Test
    public void parentheses() {
        UncertainDoubleFormat formatter = new UncertainDoubleFormat();
        formatter.setUncertaintyNotation(UncertaintyConfiguration.Notation.PARENTHESES);
        assertThat(formatter.scientificNotationWithUncertainty(5., 1.9)).isEqualTo("5.0(1.9)");
        assertThat(formatter.scientificNotationWithUncertainty(1234.234, 0.0456)).isEqualTo("1234.23(5)");
    }

    @Test
    public void grouping() {
        ConfigurationService.withAspect(NumberConfiguration.class, nc -> {
            DecimalFormat nf = (DecimalFormat) nc.getNumberFormat();
            nf.setGroupingUsed(true);
            nf.setGroupingSize(4);
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(Locale.US);
            dfs.setGroupingSeparator('_');
            nf.setDecimalFormatSymbols(dfs);
            return nc;
        }, () -> {
            UncertainDoubleFormat formatter = FormatService.getFormat(UncertainDoubleFormatProvider.class);

            assertThat(formatter.scientificNotationWithUncertainty(5_000_000d,
                2d)).isEqualTo("500_0000 ± 2");
        });
    }



}
