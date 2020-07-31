package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.reals.RealField;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Rotation implements MultiplicativeGroupElement<Rotation> {

    final FieldMatrix3<RealNumber> rot;

    private Rotation() {
        rot = new FieldMatrix3<RealNumber>(RealField.INSTANCE);
    }

    public Rotation(double[][] values) {
        rot = FieldMatrix3.of(
            new RealNumber(values[0][0]), new RealNumber(values[0][1]), new RealNumber(values[0][2]),
            new RealNumber(values[1][0]), new RealNumber(values[1][1]), new RealNumber(values[1][2]),
            new RealNumber(values[2][0]), new RealNumber(values[2][1]), new RealNumber(values[2][2])
        );
    }

    public Rotation(RealNumber[][] values) {
        rot = FieldMatrix3.of(
            values[0][0], values[0][1], values[0][2],
            values[1][0], values[1][1], values[1][2],
            values[2][0], values[2][1],values[2][2]
        );
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
    public RotationGroup structure() {
        return RotationGroup.INSTANCE;
    }

    @Override
    public Rotation self() {
        return this;
    }

    @Override
    public Rotation times(Rotation multiplier) {
        return new Rotation(
            multiplier.rot.times(multiplier.rot).values
        );
    }

    public FieldVector3<RealNumber> rotate(FieldVector3<RealNumber> in) {
        return in.times(rot);
    }

    @Override
    public Rotation pow(int exponent) {
        Rotation result = structure().one();
        for (int i = 0; i < exponent; i++) {
            result = result.times(this);
        }
        return result;
    }

    @Override
    public String toString() {
        return rot.toString();
    }
}