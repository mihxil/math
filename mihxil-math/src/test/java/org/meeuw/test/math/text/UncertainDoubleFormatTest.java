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

import lombok.extern.java.Log;

import java.text.*;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.assertj.core.data.Offset;

import org.meeuw.configuration.ConfigurationService;
import org.meeuw.math.abstractalgebra.reals.DoubleElement;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.text.*;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation;
import org.meeuw.math.text.spi.UncertainDoubleFormatProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.TextUtils.superscript;
import static org.meeuw.math.text.configuration.UncertaintyConfiguration.Notation.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
class UncertainDoubleFormatTest {

    @BeforeAll
    public static void setup() {
        ConfigurationService.resetToDefaultDefaults();
    }



    UncertainDoubleFormat formatter = new UncertainDoubleFormat();


    @Test
    public void weight() {
        assertThat(FormatService.getFormat(DoubleElement.class, ConfigurationService.getConfiguration()).findFirst()).containsInstanceOf(UncertainDoubleFormat.class);
    }

    @Test
    public void basic() {
        ScientificNotation<Double> scientificNotation = formatter.getScientific();
        assertThat(scientificNotation.formatWithUncertainty(5d, 1d)).isEqualTo("5.0 ± 1.0");
        assertThat(scientificNotation.formatWithUncertainty(5d, 2d)).isEqualTo("5 ± 2");
        assertThat(scientificNotation.formatWithUncertainty(5.1, 1.9)).isEqualTo("5.1 ± 1.9");
        assertThat(scientificNotation.formatWithUncertainty(5.4e-20, 4.34e-22)).isEqualTo("(5.40 ± 0.04)·10⁻²⁰");
    }


    @Test
    public void numberFormat() {
        formatter.setNumberFormat(NumberFormat.getNumberInstance(new Locale("nl")));
        ScientificNotation<Double> scientific = formatter.getScientific();

        assertThat(scientific.formatWithUncertainty(5.d, 1.9d)).isEqualTo("5,0 ± 1,9");
    }

    @Test
    public void zero() {
        ScientificNotation<Double> scientific = formatter.getScientific();
        assertThat(scientific.formatWithUncertainty(0d, 0d)).isEqualTo("0");
    }


    @Test
    public void zeroWithUncertainty() {
        assertThat(formatter.getScientific().formatWithUncertainty(0d, 0.003d)).isEqualTo("0.000 ± 0.003");
    }

    @Test
    public void zeroWithUncertainty1() {
        assertThat(formatter.getScientific().formatWithUncertainty(0d, 0.001d)).isEqualTo("0.0000 ± 0.0010");
    }

    @Test
    public void infinity() {
        assertThat(formatter.getScientific().formatWithUncertainty(Double.POSITIVE_INFINITY, 0d)).isEqualTo("+∞");
        assertThat(formatter.getScientific().formatWithUncertainty(Double.NEGATIVE_INFINITY, 0d)).isEqualTo("-∞");
    }

    @Test
    public void infinityExact() {
        assertThat(formatter.getScientific().formatWithUncertainty(Double.POSITIVE_INFINITY, 1d)).isEqualTo("+∞");
        assertThat(formatter.getScientific().formatWithUncertainty(Double.NEGATIVE_INFINITY, 0d)).isEqualTo("-∞");
    }

    @Test
    public void zeroExact() {
        assertThat(formatter.getScientific()
            .formatWithUncertainty(0d, 0d)).isEqualTo("0");
    }

    @Test
    public void parentheses() {
        formatter.setUncertaintyNotation(PARENTHESES);
        assertThat(formatter.getScientific().formatWithUncertainty(5., 1.9)).isEqualTo("5.0(1.9)");
        assertThat(formatter.getScientific().formatWithUncertainty(1234.234d, 0.0456d)).isEqualTo("1234.23(5)");
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

            assertThat(formatter.getScientific().formatWithUncertainty(5_000_000d,
                2d)).isEqualTo("500_0000 ± 2");
        });
    }

    @Test
    public void formatSmall() {

        String s= formatter.getScientific().formatWithUncertainty(        0.019820185668507406d, 6.938893903907228E-18);
        assertThat(s).isEqualTo("0.019820185668507406 ± 0.000000000000000007");

    }

    @Test
    public void formatSmall2() {

        String s= formatter.getScientific().formatWithUncertainty(        -0.22967301287511077d, 5.551115123125783E-17);
        assertThat(s).isEqualTo("-0.22967301287511077 ± 0.00000000000000006");
    }


    @Test
    public void formatSmallWithE() {
        String s= formatter.getScientific().formatWithUncertainty(
            -2.2967301287511077E-10,
               0.000005551115123125783E-10);
        assertThat(s).isEqualTo("(-2.296730 ± 0.000006)·10⁻¹⁰");

    }



    @Test
    public void formatNegative() {
        String s = formatter.getScientific().formatWithUncertainty(
            -2.2967301287511077E-10,
            0.000005551115123125783E-10);
    }
    public  record Case(
        double value,
        double uncertaintity,
        String rounded,
        String roundedAndTrim,
        String plusminus,
        String parenthesis
    ) {

    }

    private static final List<Case> cases = List.of(
        new Case(1, 0.5,
            "1.0",              "1",               "1.0 ± 0.5",                    "1.0(5)"),
        new Case(1, 0.00001,
            "1.000000",         "1",               "1.000000 ± 0.000010",          "1.000000(10)"),
        new Case(1, 0.20,
            "1.0",              "1",               "1.0 ± 0.2",                    "1.0(2)"),
        new Case(1, 0.00001,
            "1.000000",         "1",                "1.000000 ± 0.000010",          "1.000000(10)"),
        new Case(-2.2967301287511077E-10, 0.000005551115123125783E-10,
            "-2.296730·10⁻¹⁰",  "-2.296730·10⁻¹⁰",  "(-2.296730 ± 0.000006)·10⁻¹⁰",  "-2.296730(6)·10⁻¹⁰"),
        new Case(1000, 0,
            "1000",  "1000",  "1000",  "1000")
    );

    public static Stream<Object[]> cases() {
        return cases.stream().flatMap(c -> {
            return Stream.of(
                new Object[] {c.value, c.uncertaintity, ROUND_VALUE, c.rounded},
                new Object[] {c.value, c.uncertaintity, ROUND_VALUE_AND_TRIM, c.roundedAndTrim},
                new Object[] {c.value, c.uncertaintity, PLUS_MINUS, c.plusminus},
                new Object[] {c.value, c.uncertaintity, PARENTHESES, c.parenthesis}
            );
        });
    }

    @ParameterizedTest
    @MethodSource("cases")
    public void notations(double value, double error, Notation notation, String expected) {
        var el = DoubleElement.of(value,error);

        try (var reset = ConfigurationService.withAspect(UncertaintyConfiguration.class, (uc) -> uc.withNotation(notation))) {
            // note that we bypassed FormatterService, we need to configurer the formatter ourselves.
            formatter.setUncertaintyNotation(notation);
            String toString = formatter.format(el);
            assertThat(toString)
                .withFailMessage(() -> notation + ": toString of " + el.getValue() + "/" + el.getUncertainty() + " is '" + toString + "' but it should have been '" + expected + "'")
                .isEqualTo(expected);
            DoubleElement parsed = (DoubleElement) RealField.INSTANCE.fromString(toString);
            assertThat(parsed.toString())
                .withFailMessage(() -> notation + ": toString of " + el.getValue() + "/" + el.getUncertainty() + " is correct (" + toString + "), but parsing it again resulted " + parsed.getValue() + "/" + parsed.getUncertainty())
                .isEqualTo(expected);
        }
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
        log.info("%s -> %s -> %s -> %s".formatted( d, from, toString, doubleElement.toString()));
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

    @Test
    public void parseNaNUncertainty() {
        DoubleElement doubleElement = formatter.parseObject("306 ± NaN");
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(306);
        assertThat(doubleElement.getUncertainty().doubleValue()).isNaN();
    }



    @Test
    public void impreciseNotation() {
        DoubleElement doubleElement = formatter.parseObject("1.12345678(2)");
        formatter.setUncertaintyNotation(PARENTHESES);
        assertThat(formatter.format(doubleElement)).isEqualTo("1.12345678(2)");

        formatter.setMaximalPrecision(3);
        assertThat(formatter.format(doubleElement)).isEqualTo("1.123");
    }

    @Test
    public void impreciseNotationPlusMinus() {
        DoubleElement doubleElement = DoubleElement.of(1.12345678d, 0.00000002);
        formatter.setUncertaintyNotation(PLUS_MINUS);
        formatter.setMaximalPrecision(3);
        assertThat(formatter.format(doubleElement)).isEqualTo("1.123");
    }


}
