package org.meeuw.test.math.abstractalgebra.padic.impl;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.padic.impl.AdicDigits;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AdicDigitsTest {

    @Test
    void repeating() {
        AdicDigits a = AdicDigits.of(3);
        assertThat(a.repeating(0)).isFalse();
        assertThat(a.repeating(1)).isTrue(); // It's a zero!

        assertThat(a.get(0).toString()).isEqualTo("0:3");
        assertThat(a.get(1).toString()).isEqualTo("1:0 (repeating)");
    }
}
