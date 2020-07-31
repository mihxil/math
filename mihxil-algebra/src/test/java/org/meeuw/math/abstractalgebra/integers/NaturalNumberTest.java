package org.meeuw.math.abstractalgebra.integers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.integers.NaturalNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class NaturalNumberTest {

    @Test
    public void test() {
        assertThatThrownBy(() -> {of(1).times(of(-1));}).isInstanceOf(IllegalArgumentException.class);

        assertThat(of(5).plus(of(7))).isEqualTo(of(12));
    }
}
