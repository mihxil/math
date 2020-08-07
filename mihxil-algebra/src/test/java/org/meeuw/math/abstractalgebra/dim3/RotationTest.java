package org.meeuw.math.abstractalgebra.dim3;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.MultiplicativeGroupTheory;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.dim3.FieldVector3.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class RotationTest implements MultiplicativeGroupTheory<Rotation> {

    @Test
    public void roty() {
        Rotation y = Rotation.Ry(PI);
        FieldVector3<RealNumber> v = of(1, 0, 0);
        FieldVector3<RealNumber> rotated = y.apply(v);
        assertThat(rotated).isEqualTo(of(-1, 0, 0));
    }

     @Test
    public void rotz() {
        Rotation z = Rotation.Rz(PI);
        FieldVector3<RealNumber> v = of(0, 1, 0);
        FieldVector3<RealNumber> rotated = z.apply(v);
        assertThat(rotated).isEqualTo(of(0, -1, 0));
    }


    @Override
    public Arbitrary<Rotation> elements() {
        return Arbitraries.of(Rotation.Ry(PI / 2), Rotation.Rz(PI / 3), Rotation.Ry(PI / 6).times(Rotation.Rx(PI / 9)));
    }
}
