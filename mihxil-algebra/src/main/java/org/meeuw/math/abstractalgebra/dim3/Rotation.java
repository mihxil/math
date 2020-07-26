package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Rotation implements MultiplicativeGroupElement<Rotation, RotationGroup> {

    final Matrix3 rot;
    private Rotation() {
        rot = new Matrix3();
    }

    public Rotation(double[][] values) {
        rot = new Matrix3(values);
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
            {sin, -1 * cos, 0},
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
        return new Rotation(multiplier.rot.times(multiplier.rot).values);
    }


    public Vector3 rotate(Vector3 in) {
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
}
