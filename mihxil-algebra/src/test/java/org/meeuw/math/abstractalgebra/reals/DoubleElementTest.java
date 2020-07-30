package org.meeuw.math.abstractalgebra.reals;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.RealNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class DoubleElementTest {


    @Test
    public void test() {
        assertThat(of(5d).times(of(6d))).isEqualTo(of(30d));
    }

}
