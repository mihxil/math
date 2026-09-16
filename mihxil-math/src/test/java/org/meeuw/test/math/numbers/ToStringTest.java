package org.meeuw.test.math.numbers;

import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.junit.jupiter.api.Test;

import org.meeuw.configuration.StringConversionService;
import org.meeuw.math.numbers.DecimalFormatToString;
import org.meeuw.math.text.configuration.NumberConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class ToStringTest {

    @Test
    public void mathContext() {
        assertThat(StringConversionService.toString(new MathContext(1, RoundingMode.CEILING))).contains("precision=1 roundingMode=CEILING");

        assertThat(StringConversionService.fromString("precision=1 roundingMode=CEILING", MathContext.class)).contains(new MathContext(1, RoundingMode.CEILING));

        assertThat(StringConversionService.fromString("unparseable", MathContext.class)).isEmpty();
    }

    @Test
    public void decimalFormat() {
        assertThat(StringConversionService.toString(new DecimalFormat("##.#"))).contains("#0.#");

        assertThat(StringConversionService.fromString("#0.#", DecimalFormat.class)).contains(new DecimalFormat("##.#"));

        assertThat(StringConversionService.fromString("#..#", DecimalFormat.class)).isEmpty();
    }

    @Test
    public void decimalFormatWithUnboundedFractionDigits() {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(Integer.MAX_VALUE);

        assertThat(new DecimalFormatToString().toString(format)).isEmpty();
        assertThat(new NumberConfiguration().toString())
            .contains("DecimalFormat(maximumFractionDigits=2147483647)");
    }
}
