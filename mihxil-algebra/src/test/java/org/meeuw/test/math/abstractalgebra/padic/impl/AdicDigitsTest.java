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
        assertThat(a.toString()).isEqualTo("...0 3");
    }

    @Test
    void repeatingWithRepetend() {
        AdicDigits a = AdicDigits.of("7", "1234");
        assertThat(a.repeating(0)).isFalse();
        assertThat(a.get(0).value()).isEqualTo((byte) 4);
        assertThat(a.getIndex(0)).isEqualTo(0);

        assertThat(a.repeating(1)).isFalse();
        assertThat(a.get(1).value()).isEqualTo((byte)3);
        assertThat(a.getIndex(1)).isEqualTo(1);

         assertThat(a.repeating(2)).isFalse();
        assertThat(a.get(2).value()).isEqualTo((byte)2);
        assertThat(a.getIndex(2)).isEqualTo(2);

        assertThat(a.repeating(3)).isFalse();
        assertThat(a.get(3).value()).isEqualTo((byte)1);
        assertThat(a.getIndex(3)).isEqualTo(3);

        for (int i = 4 ; i < 100; i++) {
            assertThat(a.repeating(i)).isTrue();
            assertThat(a.get(i).value()).isEqualTo((byte)7);
            assertThat(a.getIndex(i)).isEqualTo(4);
        }
        assertThat(a.toString()).isEqualTo("...7 1234");
    }

    @Test
    void shift() {
        AdicDigits a = AdicDigits.of(3, 4);
        AdicDigits right = a.rightShift(1);

        assertThat(right.toString()).isEqualTo("...0 3");

        AdicDigits left = a.leftShift(1);

        assertThat(left.toString()).isEqualTo("...0 340");
    }

    @Test
    void shiftWithRepetend() {
        AdicDigits a = AdicDigits.of(3, 4).withRepetend(8, 9);
        AdicDigits right = a.rightShift(1);

        assertThat(right.toString()).isEqualTo("...89 3");

        AdicDigits right2 = a.rightShift(4);
        assertThat(right2.toString()).isEqualTo("...89 ");

        AdicDigits right3 = a.rightShift(3);
        assertThat(right3.toString()).isEqualTo("...98 ");

        AdicDigits left = a.leftShift(1);

        assertThat(left.toString()).isEqualTo("...89 340");
    }
}
