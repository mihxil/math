package org.meeuw.math.abstractalgebra.dim3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.dim3.Matrix3.of;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
class Matrix3Test {

    @Test
    void times() {
        Matrix3 example = of(
            1, 2, 4,
            4, 5, 6,
            7, 8, 9
        );
        assertThat(example.timesDouble(2)).isEqualTo(
            new double[] {
                2, 4, 8,
                8, 10, 12,
                14, 16, 18
            }
        );
        assertThat(example.times(example.structure().one())).isEqualTo(example);
    }

    @Test
    void pow() {
    }

    @Test
    void testTimes() {
    }

    @Test
    void testTimes1() {
    }
}
