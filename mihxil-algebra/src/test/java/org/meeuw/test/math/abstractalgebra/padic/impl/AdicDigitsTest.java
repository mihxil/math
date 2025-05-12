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

        assertThat(a.isRepetitive()).isFalse(); // just repeating zero's, that
    }

    @Test
    void repeatingWithRepetend() {
        AdicDigits a = AdicDigits.of("7", "1234");
        assertThat(a.repeating(0)).isFalse();
        assertThat(a.get(0).value()).isEqualTo((byte) 4);

        assertThat(a.repeating(1)).isFalse();
        assertThat(a.get(1).value()).isEqualTo((byte)3);

         assertThat(a.repeating(2)).isFalse();
        assertThat(a.get(2).value()).isEqualTo((byte)2);

        assertThat(a.repeating(3)).isFalse();
        assertThat(a.get(3).value()).isEqualTo((byte)1);

        for (int i = 4 ; i < 100; i++) {
            assertThat(a.repeating(i)).isTrue();
            assertThat(a.get(i).value()).isEqualTo((byte)7);
        }
    }
}
