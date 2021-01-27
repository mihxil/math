package org.meeuw.math.statistics.text;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 */
class StatisticalLongNumberFormatTest {

    @Test
    void format() {
        StatisticalLongNumberFormat format = new StatisticalLongNumberFormat();
        assertThatThrownBy(() -> format.format(new Object())).isInstanceOf(IllegalArgumentException.class);
    }
}
