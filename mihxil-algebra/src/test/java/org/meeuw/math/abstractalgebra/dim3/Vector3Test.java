package org.meeuw.math.abstractalgebra.dim3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class Vector3Test {

    @Test
    void times() {
        Vector3 example = Vector3.of(1, 2, 3);
        assertThat(example.times(Matrix3Group.INSTANCE.one())).isEqualTo(example);
        assertThat(example.times(Matrix3Group.INSTANCE.one().times(2))).isEqualTo(example.times(2));

    }
}
