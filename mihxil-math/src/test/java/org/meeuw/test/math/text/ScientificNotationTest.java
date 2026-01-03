package org.meeuw.test.math.text;

import org.junit.jupiter.api.Test;

import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.text.ScientificNotation;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class ScientificNotationTest {

    UncertaintyConfiguration.Notation notation = UncertaintyConfiguration.Notation.ROUND_VALUE;
    int minimumExponent = 3;

    final ScientificNotation<Double> scientific = new ScientificNotation<>(
        () -> minimumExponent,
        () -> 6,
        () -> notation,
        NumberConfiguration::getDefaultNumberFormat,
        DoubleOperations.INSTANCE
    );

    @Test
    public void notationRound() {
        assertThat(scientific.formatWithUncertainty(1000.0d, 0d))
            .isEqualTo("1000");
    }

    @Test
    public void notationParen() {
        notation = UncertaintyConfiguration.Notation.PARENTHESES;
        assertThat(scientific. formatWithUncertainty(1000.0d, 5d)).isEqualTo("1000(5)");
    }

    @Test
    public void notationParen0() {
        notation = UncertaintyConfiguration.Notation.PARENTHESES;
        minimumExponent = 0;
        assertThat(scientific. formatWithUncertainty(1000.0d, 5d)).isEqualTo("1000(5)");
    }




}
