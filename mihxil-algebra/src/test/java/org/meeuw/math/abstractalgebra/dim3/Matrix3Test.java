package org.meeuw.math.abstractalgebra.dim3;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.dim3.Matrix3.of;

/**
 * @author Michiel Meeuwissen
 */
class Matrix3Test implements MultiplicativeGroupTheory<Matrix3> {

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
        assertThat(example.times(example.getStructure().one()))
                .isEqualTo(example);
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

    @Override
    public Arbitrary<Matrix3> elements() {
        return Arbitraries.of(
            of(
                1, 2, 4,
                4, 5, 6,
                7, 8, 9
            ),
            of(
                4, 2.2, 5,
                0, 1, 6,
                -9, 8, -0.005
            )

        );
    }
}
