package org.meeuw.test.math.uncertainnumbers;

import org.junit.jupiter.api.Test;

import org.meeuw.math.uncertainnumbers.ImmutableUncertainNumber;

import static org.assertj.core.api.Assertions.assertThat;

class ImmutableUncertainNumberTest {

    @Test
    public void tString() {
        ImmutableUncertainNumber<Long> t = ImmutableUncertainNumber.<Long>of(5L, () -> 3L);
        assertThat(t.toString()).isEqualTo("5 ± 3");
        ImmutableUncertainNumber<Long> withUnits = t.withUnitsAsString("m");
        assertThat(withUnits.toString()).isEqualTo("5 ± 3 m");
    }

     @Test
    public void eq() {
        ImmutableUncertainNumber<Double> t1 = ImmutableUncertainNumber.<Double>of(5d, () -> 3d);
        ImmutableUncertainNumber<Double> t2 = ImmutableUncertainNumber.<Double>of(4d, () -> 3d);

        assertThat(t1.eq(t2)).isTrue();
    }

}
