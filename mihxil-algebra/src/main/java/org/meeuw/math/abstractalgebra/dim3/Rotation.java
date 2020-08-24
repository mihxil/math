package org.meeuw.math.abstractalgebra.dim3;

import java.util.function.UnaryOperator;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Rotation implements MultiplicativeGroupElement<Rotation>, UnaryOperator<FieldVector3<RealNumber>> {

    public static final Rotation ONE = new Rotation(new double[][]{
        {1, 0, 0},
        {0, 1, 0},
        {0, 0, 1}
    });

    final FieldMatrix3<RealNumber> rot;

    private Rotation(double[][] values) {
        rot = FieldMatrix3.of(
            new RealNumber(values[0][0]), new RealNumber(values[0][1]), new RealNumber(values[0][2]),
            new RealNumber(values[1][0]), new RealNumber(values[1][1]), new RealNumber(values[1][2]),
            new RealNumber(values[2][0]), new RealNumber(values[2][1]), new RealNumber(values[2][2])
        );
    }

    private Rotation(RealNumber[][] values) {
        rot = FieldMatrix3.of(
            values[0][0], values[0][1], values[0][2],
            values[1][0], values[1][1], values[1][2],
            values[2][0], values[2][1],values[2][2]
        );
    }
    private Rotation(FieldMatrix3<RealNumber> rot) {
        this.rot = rot;
    }

    public static Rotation Rx(double phi) {
        double cos = Math.cos(phi);
        double sin = Math.sin(phi);
        return new Rotation(new double[][]{
            {1, 0, 0},
            {0, cos, -1 * sin},
            {0, sin, cos},
        });
    }

    public static Rotation Ry(double phi) {
        double cos = Math.cos(phi);
        double sin = Math.sin(phi);
        return new Rotation(new double[][]{
            {cos, 0, sin},
            {0, 1, 0},
            {-1 * sin, 0, cos}
        });
    }

    public static Rotation Rz(double phi) {
        double cos = Math.cos(phi);
        double sin = Math.sin(phi);
        return new Rotation(new double[][]{
            {cos, -1 * sin, 0},
            {sin, cos, 0},
            {0, 0, 1}
        });
    }

    @Override
    public RotationGroup getStructure() {
        return RotationGroup.INSTANCE;
    }

    @Override
    public Rotation times(Rotation multiplier) {
        return new Rotation(
            rot.times(multiplier.rot)
        );
    }
    protected FieldVector3<RealNumber> rotate(FieldVector3<RealNumber> in) {
        return in.times(rot);
    }

    @Override
    public FieldVector3<RealNumber> apply(FieldVector3<RealNumber> in) {
        return rotate(in);
    }

    @Override
    public Rotation reciprocal() {
        return new Rotation(rot.reciprocal());
    }


    @Override
    public String toString() {
        return rot.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rotation rotation = (Rotation) o;
        return getStructure().getEquivalence().test(this, rotation);
    }

    @Override
    public int hashCode() {
        return rot.hashCode();
    }


}
