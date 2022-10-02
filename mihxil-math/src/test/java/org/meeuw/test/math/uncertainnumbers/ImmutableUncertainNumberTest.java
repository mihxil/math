package org.meeuw.test.math.uncertainnumbers;

import org.junit.jupiter.api.Test;

import org.meeuw.math.uncertainnumbers.ImmutableUncertainNumber;

import static org.assertj.core.api.Assertions.assertThat;

class ImmutableUncertainNumberTest {

    @Test
    public void tString() {
        ImmutableUncertainNumber<Long> t = ImmutableUncertainNumber.of(5L, () -> 3L);
        assertThat(t.toString()).isEqualTo("");
    }

}
