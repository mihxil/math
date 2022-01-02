package org.meeuw.test.math.abstractalgebra.dim3;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.dim3.FieldVector3;
import org.meeuw.math.abstractalgebra.dim3.Rotation;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;

import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.dim3.FieldVector3.of;
import static org.meeuw.math.abstractalgebra.dim3.Rotation.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class RotationTest implements MultiplicativeGroupTheory<Rotation> {

    @Test
    public void rotx() {
        Rotation half = Rx(PI);
        Rotation quarter= Rx(PI / 2);
        FieldVector3<RealNumber> v = of(0, 1, 0);
        FieldVector3<RealNumber> rotated = half.apply(v);
        assertThat(rotated).isEqualTo(of(0, -1, 0));

        FieldVector3<RealNumber> rotatedQuarter = quarter.apply(v);
        assertThat(rotatedQuarter).isEqualTo(of(0, 0, 1));

        RealNumber det = half.asMatrix().determinant();
        log.info("Determinant of {}: {}", half.asMatrix(), det);
        Rotation reciprocal = half.reciprocal();
        log.info("{}", reciprocal);

        Rotation pow = half.pow(-1);
        log.info("{}", pow);
    }

    @Test
    public void roty() {
        Rotation y = Ry(PI);
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

    @Property
    public void determinantShouldBeOne(@ForAll(ELEMENTS) Rotation r) {
        RealNumber determinant = r.asMatrix().determinant();
        assertThat(determinant.equals(RealNumber.ONE))
            .withFailMessage("det(" + r.asMatrix() + ") = " + determinant + " != 1").isTrue();
    }

    @Override
    public Arbitrary<Rotation> elements() {
        return Arbitraries.randoms()
            .map(r -> r.nextDouble() * 2 * PI).tuple3()
            .map(t -> Rx(t.get1()).times(Ry(t.get2())).times(Rz(t.get3())));
    }
}
