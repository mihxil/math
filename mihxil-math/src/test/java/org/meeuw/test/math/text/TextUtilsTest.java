/*
 * Copyright (C) 2012 All rights reserved
 * VPRO The Netherlands
 */

package org.meeuw.test.math.text;


import java.lang.reflect.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.assertj.core.api.Assertions;

import org.checkerframework.checker.nullness.qual.PolyNull;
import org.meeuw.math.text.TextUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class TextUtilsTest {

    @Test
    public void subscript() {
        Assertions.assertThat(TextUtils.subscript("-1234567890 P")).isEqualTo("₋₁₂₃₄₅₆₇₈₉₀ ₚ");
        assertThat(TextUtils.subscript(-123)).isEqualTo("₋₁₂₃");
        assertThat(TextUtils.subscript(0)).isEqualTo("₀");
    }

    @Test
    public void superscript() {
        assertThat("+" + TextUtils.superscript("+-1234567890 P")).isEqualTo("+⁺⁻¹²³⁴⁵⁶⁷⁸⁹⁰ ᴾ");
        assertThat(TextUtils.superscript(-123)).isEqualTo("⁻¹²³");
    }

    @Test
    public void underLine() {
        assertThat(TextUtils.underLine("foo bar 123")).isEqualTo("f̲o̲o̲ ̲b̲a̲r̲ ̲1̲2̲3̲");
    }

    @Test
    public void underLineDouble() {
        assertThat(TextUtils.underLineDouble("foo bar 123")).isEqualTo("f̳o̳o̳ ̳b̳a̳r̳ ̳1̳2̳3̳");
    }

    @Test
    public void overLine() {
        assertThat(TextUtils.overLine("foo bar 123")).isEqualTo("f̅o̅o̅ ̅b̅a̅r̅ ̅1̅2̅3̅");
    }

    @Test
    public void overLineDouble() {
        assertThat(TextUtils.overLineDouble("foo bar 123")).isEqualTo("f̿o̿o̿ ̿b̿a̿r̿ ̿1̿2̿3̿");
    }

    @Test
    public void controlNull() {
        assertThat(TextUtils.controlEach(null, '\u033f')).isNull();
    }

    @Test
    public void instant() {
        assertThat(TextUtils.format(Instant.parse("2021-08-22T20:00:14Z"), ChronoUnit.DAYS)).startsWith("2021-08");

        assertThat(TextUtils.format(ZoneId.of("Europe/Amsterdam"), Instant.parse("2021-08-22T20:00:14Z"), ChronoUnit.DAYS)).isEqualTo("2021-08-22");
        assertThat(TextUtils.format(ZoneId.of("Europe/Amsterdam"), Instant.parse("2021-08-22T20:00:14Z"), ChronoUnit.SECONDS)).isEqualTo("2021-08-22T22:00:14");
    }

    @ParameterizedTest
    @MethodSource("polyNullMethods")
    public void polyNull(Method m) throws InvocationTargetException, IllegalAccessException {
        Object[] parameters = new Object[m.getParameterCount()];
        for (int i = 0 ; i < m.getParameterCount(); i++) {
            if (boolean.class.equals(m.getParameterTypes()[i])) {
                parameters[i] = Boolean.FALSE;
            }
            if (int.class.equals(m.getParameterTypes()[i])) {
                parameters[i] = 10;
            }
        }
        Object invoke = m.invoke(null, parameters);
        assertThat(invoke).isNull();
    }

    public static Stream<Arguments> polyNullMethods() {
         return Arrays.stream(TextUtils.class.getDeclaredMethods())
             .filter(m -> Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers()))
             .filter(m -> m.getAnnotatedReturnType().getAnnotation(PolyNull.class) != null)
             .map(Arguments::of);
    }
}

