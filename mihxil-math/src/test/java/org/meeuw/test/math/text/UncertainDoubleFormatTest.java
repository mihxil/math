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
package org.meeuw.test.math.text;

import lombok.extern.log4j.Log4j2;

import java.text.*;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.assertj.core.data.Offset;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.reals.DoubleElement;
import org.meeuw.math.text.FormatService;
import org.meeuw.math.text.UncertainDoubleFormat;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.TextUtils.superscript;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class UncertainDoubleFormatTest {

    @BeforeAll
    public static void setup() {
        ConfigurationService.resetToDefaultDefaults();
    }
    UncertainDoubleFormat formatter = new UncertainDoubleFormat();


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
        formatter.setNumberFormat(NumberFormat.getNumberInstance(new Locale("nl")));
        assertThat(formatter.scientificNotationWithUncertainty(5., 1.9)).isEqualTo("5,0 ± 1,9");
    }

    @Test
    public void zero() {
        assertThat(formatter.scientificNotationWithUncertainty(0, 0)).isEqualTo("0 ± 0");
    }


    @Test
    public void zeroWithUncertainty() {
        assertThat(formatter.scientificNotationWithUncertainty(0, 0.003)).isEqualTo("0.000 ± 0.003");
    }
    @Test
    public void zeroWithUncertainty1() {
        assertThat(formatter.scientificNotationWithUncertainty(0, 0.001)).isEqualTo("0.0000 ± 0.0010");
    }

    @Test
    public void infinity() {
        assertThat(formatter.scientificNotationWithUncertainty(Double.POSITIVE_INFINITY, 0)).isEqualTo("∞");
        assertThat(formatter.scientificNotationWithUncertainty(Double.NEGATIVE_INFINITY, 0)).isEqualTo("-∞");
    }

    @Test
    public void infinityExact() {
        assertThat(UncertainDoubleFormat.scientificNotation(Double.POSITIVE_INFINITY, 1)).isEqualTo("∞");
        assertThat(UncertainDoubleFormat.scientificNotation(Double.NEGATIVE_INFINITY, 0)).isEqualTo("-∞");
    }

    @Test
    public void zeroExact() {
        assertThat(UncertainDoubleFormat.scientificNotation(0, 1)).isEqualTo("0");
    }

    @Test
    public void parentheses() {
        formatter.setUncertaintyNotation(UncertaintyConfiguration.Notation.PARENTHESES);
        assertThat(formatter.scientificNotationWithUncertainty(5., 1.9)).isEqualTo("5.0(1.9)");
        assertThat(formatter.scientificNotationWithUncertainty(1234.234, 0.0456)).isEqualTo("1234.23(5)");
    }

    @Test
    public void grouping() {
        ConfigurationService.withAspect(NumberConfiguration.class, nc -> {

            DecimalFormat nf = nc.getNumberFormat();
            nf.setGroupingUsed(true);
            nf.setGroupingSize(4);
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(Locale.US);
            dfs.setGroupingSeparator('_');
            nf.setDecimalFormatSymbols(dfs);

            return nc.withNumberFormat(nf);
        }, () -> {
            UncertainDoubleFormat formatter = FormatService.getFormat(UncertainDoubleFormatProvider.class);

            assertThat(formatter.scientificNotationWithUncertainty(5_000_000d,
                2d)).isEqualTo("500_0000 ± 2");
        });
    }

    @Test
    public void formatSmall() {

        String s= formatter.scientificNotationWithUncertainty(        0.019820185668507406d, 6.938893903907228E-18);
        assertThat(s).isEqualTo("0.019820185668507406 ± 0.000000000000000007");

    }

    @Test
    public void formatSmall2() {

        String s= formatter.scientificNotationWithUncertainty(        -0.22967301287511077d, 5.551115123125783E-17);
        assertThat(s).isEqualTo("-0.22967301287511077 ± 0.00000000000000006");
    }


    @Test
    public void formatSmallWithE() {
        String s= formatter.scientificNotationWithUncertainty(
            -2.2967301287511077E-10,
               0.000005551115123125783E-10);
        assertThat(s).isEqualTo("(-2.296730 ± 0.000006)·10⁻¹⁰");

    }



    @Test
    public void formatNegative() {

        String s= formatter.scientificNotationWithUncertainty(
            -2.2967301287511077E-10,
               0.000005551115123125783E-10);
        assertThat(s).isEqualTo("(-2.296730 ± 0.000006)·10⁻¹⁰");
    }

    @ParameterizedTest
    @ValueSource(doubles = {
        1.3660434920643638d
    })
    public void formatAndParse(double d) {
        DoubleElement from = DoubleElement.of(d);
        String toString = formatter.format(from);
        ParsePosition parsePosition = new ParsePosition(0);
        DoubleElement doubleElement = formatter.parseObject(toString, parsePosition);
        assertThat(parsePosition.getIndex()).isEqualTo(toString.length());
        log.info("{} -> {} -> {} -> {}", d, from, toString, doubleElement.toString());
        assertThat(doubleElement.eq(from)).isTrue();

    }

    @Test
    public void parseBracket() {
        DoubleElement doubleElement = formatter.parseObject("1.567(45)");
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(1.567);
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(0.045);
    }

    @Test
    public void parseBracket0() {
        DoubleElement doubleElement = formatter.parseObject("1.567()");
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(1.567);
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(0.0);
    }

    @Test
    public void parseBracket1() {
        DoubleElement doubleElement = formatter.parseObject("-1123.567(4)");
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(-1123.567);
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(0.004);
    }


    @Test
    public void parseBracket10() {
        DoubleElement doubleElement = formatter.parseObject("-1.567(4) ·10" + superscript(5));
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(-156700d);
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(400);
    }

    @Test
    public void parseBracketE() {
        DoubleElement doubleElement = formatter.parseObject("-1.567(4)E5");
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(-156700d);
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(400);
    }

    @Test
    public void parsePlusMin() {
        DoubleElement doubleElement = formatter.parseObject("(-2.296730 ± 0.000006)·10⁻¹⁰)");

        assertThat(doubleElement.getValue().doubleValue()).isCloseTo(-2.296730e-10, Offset.offset(0.000001e-10));
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(0.000006e-10);
    }



}
