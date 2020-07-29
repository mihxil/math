package org.meeuw.math.abstractalgebra.dim3;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.doubles.DoubleElement;
import org.meeuw.math.abstractalgebra.doubles.DoubleField;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.dim3.FieldVector3.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class RotationTest {

    @Test
    public void roty() {
        Rotation y = Rotation.Ry(Math.PI);
        FieldVector3<DoubleElement, DoubleField> v = of(1, 0, 0);
        FieldVector3<DoubleElement, DoubleField> rotated = y.rotate(v);
        assertThat(rotated).isEqualTo(of(-1, 0, 0));
    }

     @Test
    public void rotz() {
        Rotation z = Rotation.Rz(Math.PI);
        FieldVector3<DoubleElement, DoubleField> v = of(0, 1, 0);
        FieldVector3<DoubleElement, DoubleField> rotated = z.rotate(v);
        assertThat(rotated).isEqualTo(of(0, -1, 0));
    }


}
