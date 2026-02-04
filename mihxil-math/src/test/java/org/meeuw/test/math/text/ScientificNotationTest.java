package org.meeuw.test.math.text;

import org.junit.jupiter.api.Test;

import org.meeuw.math.numbers.DoubleOperations;
import org.meeuw.math.text.ScientificNotation;
import org.meeuw.math.text.configuration.NumberConfiguration;
import org.meeuw.math.text.configuration.UncertaintyConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class ScientificNotationTest {

    /**
     * Non-final settings. Are fed to our instance via suppliers
     */
    UncertaintyConfiguration.Notation notation = UncertaintyConfiguration.Notation.ROUND_VALUE;
    int minimumExponent = 3;

    // here it is:
    final ScientificNotation<Double> scientific = new ScientificNotation<>(
        () -> minimumExponent,
        () -> 6,
        () -> notation,
        NumberConfiguration::getDefaultNumberFormat,
        DoubleOperations.INSTANCE
    );

    /**
     * An exact value which is required to be rounded doesn't give an uncertainty indication
     */
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
