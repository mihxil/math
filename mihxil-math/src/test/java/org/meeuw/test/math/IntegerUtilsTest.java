package org.meeuw.test.math;

import org.junit.jupiter.api.Test;

import org.meeuw.math.IntegerUtils;

import static org.assertj.core.api.Assertions.assertThat;

class IntegerUtilsTest {

    @Test
    void gcd() {
        assertThat(IntegerUtils.gcd(123L, 13L)).isEqualTo(1L);
        assertThat(IntegerUtils.gcd(169L, 26L)).isEqualTo(13L);
    }
}
