package org.meeuw.test.math.abstractalgebra.padic.impl;

import org.junit.jupiter.api.Test;

import org.meeuw.math.abstractalgebra.padic.impl.AdicDigits;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AdicDigitsTest {

    @Test
    void repeating() {
        AdicDigits a = AdicDigits.of(3);
        assertThat(a.repeating(0)).isFalse();
        assertThat(a.repeating(1)).isFalse();
    }
}
