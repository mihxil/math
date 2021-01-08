/*
 * Copyright (C) 2012 All rights reserved
 * VPRO The Netherlands
 */

package org.meeuw.math.text;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *

 */
public class TextUtilsTest {

    @Test
    public void subscript() {
        assertThat(TextUtils.subscript("-123")).isEqualTo("₋₁₂₃");
        assertThat(TextUtils.subscript(-123)).isEqualTo("₋₁₂₃");
    }

    @Test
    public void superscript() {
        assertThat(TextUtils.superscript("-123")).isEqualTo("⁻¹²³");
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

}

