package org.meeuw.math.abstractalgebra.dim3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class RotationTest {

    @Test
    public void rotx() {
        Rotation z = Rotation.Rz(Math.PI / 2);

        Vector3 v = new Vector3(0, 1, 0);

        Vector3 rotated = z.rotate(v);
        assertThat(rotated).isEqualTo(new Vector3(0, -1, 0));
    }


}
