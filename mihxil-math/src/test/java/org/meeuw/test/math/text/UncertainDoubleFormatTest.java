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
import org.meeuw.math.uncertainnumbers.UncertainNumber;

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

    UncertainDoubleFormat uncertainDoubleFormat = new UncertainDoubleFormat();
    ScientificNotation<Double> scientificNotation = uncertainDoubleFormat.getScientific();


    @Test
    public void weight() {
        assertThat(FormatService.getFormat(DoubleElement.class, ConfigurationService.getConfiguration()).findFirst()).containsInstanceOf(UncertainDoubleFormat.class);
    }

    @Test
    public void basic() {
        assertThat(scientificNotation.formatWithUncertainty(5d, 1d)).isEqualTo("5.0 ± 1.0");
        assertThat(scientificNotation.formatWithUncertainty(5d, 2d)).isEqualTo("5 ± 2");
        assertThat(scientificNotation.formatWithUncertainty(5.1, 1.9)).isEqualTo("5.1 ± 1.9");
    }
    @Test
    public void basicScientific() {
        assertThat(
            scientificNotation.formatWithUncertainty(5.4e-20, 4.34e-22)
        ).isEqualTo("(5.40 ± 0.04)·10⁻²⁰");
    }


    @Test
    public void numberFormat() {
        uncertainDoubleFormat.setNumberFormat(NumberFormat.getNumberInstance(new Locale("nl")));
        ScientificNotation<Double> scientific = uncertainDoubleFormat.getScientific();

        assertThat(scientific.formatWithUncertainty(5.d, 1.9d)).isEqualTo("5,0 ± 1,9");
    }

    @Test
    public void zero() {
        ScientificNotation<Double> scientific = uncertainDoubleFormat.getScientific();
        assertThat(scientific.formatWithUncertainty(0d, 0d)).isEqualTo("0.00000000000000000");
    }


    @Test
    public void zeroWithUncertainty() {
        assertThat(uncertainDoubleFormat.getScientific().formatWithUncertainty(0d, 0.003d)).isEqualTo("0.000 ± 0.003");
    }

    @Test
    public void zeroWithUncertainty1() {
        assertThat(uncertainDoubleFormat.getScientific().formatWithUncertainty(0d, 0.001d)).isEqualTo("0.0000 ± 0.0010");
    }

    @Test
    public void infinity() {
        assertThat(uncertainDoubleFormat.getScientific().formatWithUncertainty(Double.POSITIVE_INFINITY, 0d)).isEqualTo("+∞");
        assertThat(uncertainDoubleFormat.getScientific().formatWithUncertainty(Double.NEGATIVE_INFINITY, 0d)).isEqualTo("-∞");
    }

    @Test
    public void infinityExact() {
        assertThat(uncertainDoubleFormat.getScientific().formatWithUncertainty(Double.POSITIVE_INFINITY, 1d)).isEqualTo("+∞");
        assertThat(uncertainDoubleFormat.getScientific().formatWithUncertainty(Double.NEGATIVE_INFINITY, 0d)).isEqualTo("-∞");
    }

    @Test
    public void zeroExact() {
        assertThat(uncertainDoubleFormat.getScientific()
            .formatWithUncertainty(0d, 0d)).isEqualTo("0.00000000000000000");
    }

    @Test
    public void parentheses() {
        uncertainDoubleFormat.setUncertaintyNotation(PARENTHESES);
        assertThat(uncertainDoubleFormat.getScientific().formatWithUncertainty(5., 1.9)).isEqualTo("5.0(1.9)");
        assertThat(uncertainDoubleFormat.getScientific().formatWithUncertainty(1234.234d, 0.0456d)).isEqualTo("1234.23(5)");
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

        String s= uncertainDoubleFormat.getScientific().formatWithUncertainty(        0.019820185668507406d, 6.938893903907228E-18);
        assertThat(s).isEqualTo("0.019820185668507406 ± 0.000000000000000007");

    }

    @Test
    public void formatSmall2() {

        String s= uncertainDoubleFormat.getScientific().formatWithUncertainty(        -0.22967301287511077d, 5.551115123125783E-17);
        assertThat(s).isEqualTo("-0.22967301287511077 ± 0.00000000000000006");
    }


    @Test
    public void formatSmallWithE() {
        String s= uncertainDoubleFormat.getScientific().formatWithUncertainty(
            -2.2967301287511077E-10,
               0.000005551115123125783E-10);
        assertThat(s).isEqualTo("(-2.296730 ± 0.000006)·10⁻¹⁰");

    }



    @Test
    public void formatNegative() {
        String s = uncertainDoubleFormat.getScientific().formatWithUncertainty(
            -2.2967301287511077E-10,
            0.000005551115123125783E-10);
    }
    public  record Case(
        double value,
        double uncertainty,
        String rounded,
        String roundedAndStripped,
        String plusminus,
        String parenthesis
    ) implements UncertainNumber<Double> {

        @Override
        public Double getValue() {
            return 0.0;
        }

        @Override
        public Double getUncertainty() {
            return 0.0;
        }

        @Override
        public boolean strictlyEquals(Object o) {
            return false;
        }
    }

    private static final List<Case> cases = List.of(
        new Case(0, 0.,
            "0.00000000000000000",     "0", "0.00000000000000000",  "0.00000000000000000"),
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
            "1000.00000000000000000",  "1000",  "1000.00000000000000000",  "1000.00000000000000000"),
        new Case(6.62607015E-34, 0,
            "6.62607015000000000·10⁻³⁴",
            "6.62607015·10⁻³⁴", "6.62607015000000000·10⁻³⁴", "6.62607015000000000·10⁻³⁴"),
        new Case(6.62607015E-34, 0.0005E-34,
            "6.6261·10⁻³⁴",                  "6.6261·10⁻³⁴", "(6.6261 ± 0.0005)·10⁻³⁴", "6.6261(5)·10⁻³⁴"),
        new Case(1d, 0,
            "1.00000000000000000",                  "1", "1.00000000000000000", "1.00000000000000000"),
        new Case(1.3660434920643638d, 0d,
            "1.36604349206436380", "1.3660434920643638", "1.36604349206436380", "1.36604349206436380"
            )
    );

    public static Stream<Object[]> cases() {
        return cases.stream().flatMap(c ->
        {
            boolean defaultStrip = UncertaintyConfiguration.DEFAULT_STRIP_ZEROS.test(ROUND_VALUE, DoubleElement.of(c.value, c.uncertainty));
            log.info("For case " + c + ", default strip zeros is " + defaultStrip);
            return Stream.of(
                new Object[]{c.value, c.uncertainty, ROUND_VALUE, false, c.rounded},
                new Object[]{c.value, c.uncertainty, ROUND_VALUE, true, c.roundedAndStripped},
                new Object[]{c.value, c.uncertainty, ROUND_VALUE, null, defaultStrip ? c.roundedAndStripped : c.rounded},
                new Object[]{c.value, c.uncertainty, PLUS_MINUS, false, c.plusminus},
                new Object[]{c.value, c.uncertainty, PARENTHESES, false, c.parenthesis}
            );
        });
    }

    @ParameterizedTest
    @MethodSource("cases")
    public void notations(double value, double error, Notation notation, Boolean stripZeros, String expected) {
        var el = DoubleElement.of(value,error);

        // note that we bypassed FormatterService, we need to configurer the formatter ourselves.
        // This test is more low level.
        uncertainDoubleFormat.setUncertaintyNotation(notation);
        uncertainDoubleFormat.setStripZeros(stripZeros == null ? UncertaintyConfiguration.DEFAULT_STRIP_ZEROS : (n, v) -> stripZeros);
        //boolean defaultStrip = UncertaintyConfiguration.DEFAULT_STRIP_ZEROS.test(notation, el);
        String toString = uncertainDoubleFormat.format(el);
        assertThat(toString)
            .withFailMessage(() -> notation + (stripZeros != null && stripZeros ? " (and trim)" : "") + " of " + el.toDebugString() + " is '" + toString + "' but it should have been '" + expected + "'")
            .isEqualTo(expected);
        DoubleElement parsed = (DoubleElement) RealField.INSTANCE.fromString(toString);
        assertThat(parsed.eq(el))
            .withFailMessage(() -> notation + ": toString of " + el.toDebugString() + " is correct (" + toString + "), but parsing it again resulted " + parsed.toDebugString())
            .isTrue();
    }

    /**
     * Testing that
     */
    @ParameterizedTest
    @ValueSource(doubles = {
        1.3660434920643638d
    })
    public void formatAndParse(double d) {
        DoubleElement from = DoubleElement.of(d);
        String toString = uncertainDoubleFormat.format(from);
        ParsePosition parsePosition = new ParsePosition(0);
        DoubleElement doubleElement = uncertainDoubleFormat.parseObject(toString, parsePosition);
        assertThat(parsePosition.getIndex()).isEqualTo(toString.length());
        log.info("\n%s (source) -> \n%s  (element) ->\n%s (toString)->\n%s / %s (parsed)".formatted(
            d, from.toDebugString(),
            toString,
            doubleElement.toDebugString(),
            doubleElement.toString())
        );
        assertThat(doubleElement.eq(from))
            .withFailMessage(doubleElement.toDebugString() + " !eq " + from.toDebugString()).isTrue();

    }

    @Test
    public void parseBracket() {
        DoubleElement doubleElement = uncertainDoubleFormat.parseObject("1.567(45)");
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(1.567);
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(0.045);
    }

    @Test
    public void parseBracket0() {
        DoubleElement doubleElement = uncertainDoubleFormat.parseObject("1.567()");
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(1.567);
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(0.0);
    }

    @Test
    public void parseBracket1() {
        DoubleElement doubleElement = uncertainDoubleFormat.parseObject("-1123.567(4)");
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(-1123.567);
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(0.004);
    }


    @Test
    public void parseBracket10() {
        DoubleElement doubleElement = uncertainDoubleFormat.parseObject("-1.567(4) ·10" + superscript(5));
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(-156700d);
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(400);
    }

    @Test
    public void parseBracketE() {
        DoubleElement doubleElement = uncertainDoubleFormat.parseObject("-1.567(4)E5");
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(-156700d);
        assertThat(doubleElement.getUncertainty().doubleValue()).isEqualTo(400);
    }

    @Test
    public void parsePlusMin() {
        DoubleElement doubleElement = uncertainDoubleFormat.parseObject("(-2.296730 ± 0.000006)·10⁻¹⁰)");

        assertThat(doubleElement.getValue().doubleValue())
            .isCloseTo(-2.296730e-10, Offset.offset(0.000001e-10));
        assertThat(doubleElement.getUncertainty().doubleValue())
            .isCloseTo(0.000006e-10, Offset.offset(0.00000001e-10));
    }

    @Test
    public void parseNaNUncertainty() {
        DoubleElement doubleElement = uncertainDoubleFormat.parseObject("306 ± NaN");
        assertThat(doubleElement.getValue().doubleValue()).isEqualTo(306);
        assertThat(doubleElement.getUncertainty().doubleValue()).isNaN();
    }



    @Test
    public void impreciseNotation() {
        DoubleElement doubleElement = uncertainDoubleFormat.parseObject("1.12345678(2)");
        uncertainDoubleFormat.setUncertaintyNotation(PARENTHESES);
        assertThat(uncertainDoubleFormat.format(doubleElement)).isEqualTo("1.12345678(2)");

        uncertainDoubleFormat.setMaximalPrecision(3);
        assertThat(uncertainDoubleFormat.format(doubleElement)).isEqualTo("1.123");
    }

    @Test
    public void impreciseNotationPlusMinus() {
        DoubleElement doubleElement = DoubleElement.of(1.12345678d, 0.00000002);
        uncertainDoubleFormat.setUncertaintyNotation(PLUS_MINUS);
        uncertainDoubleFormat.setMaximalPrecision(3);
        assertThat(uncertainDoubleFormat.format(doubleElement)).isEqualTo("1.123");
    }


}
