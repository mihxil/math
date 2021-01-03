package org.meeuw.math.text;

import java.text.NumberFormat;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.meeuw.math.text.spi.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class UncertainDoubleFormatTest {

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
    public void parentheses() {
        UncertainDoubleFormat formatter = new UncertainDoubleFormat();
        formatter.setUncertaintyNotation(Configuration.UncertaintyNotation.PARENTHESES);
        assertThat(formatter.scientificNotationWithUncertainty(5., 1.9)).isEqualTo("5.0(1.9)");
        assertThat(formatter.scientificNotationWithUncertainty(1234.234, 0.0456)).isEqualTo("1234.23(5)");
    }

}
