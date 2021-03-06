package org.meeuw.math.abstractalgebra.dim3;

import java.util.function.UnaryOperator;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Rotation implements
    MultiplicativeGroupElement<Rotation>,
    UnaryOperator<FieldVector3<RealNumber>> {

    private static final RealNumber U = RealNumber.ONE;
    private static final RealNumber Z = RealNumber.ZERO;

    public static final Rotation ONE = new Rotation(new RealNumber[][]{
        {U, Z, Z},
        {Z, U, Z},
        {Z, Z, U}
    });


    final FieldMatrix3<RealNumber> rot;

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
        RealNumber cos = RealNumber.of(phi).cos();
        RealNumber sin = RealNumber.of(phi).sin();

        return new Rotation(new RealNumber[][]{
            {U, Z, Z},
            {Z, cos, sin.negation()},
            {Z, sin, cos},
        });
    }

    public static Rotation Ry(double phi) {
        RealNumber cos = RealNumber.of(phi).cos();
        RealNumber sin = RealNumber.of(phi).sin();
        return new Rotation(new RealNumber[][]{
            {cos, Z, sin},
            {Z, U, Z},
            {sin.negation(), Z, cos}
        });
    }

    public static Rotation Rz(double phi) {
        RealNumber cos = RealNumber.of(phi).cos();
        RealNumber sin = RealNumber.of(phi).sin();
        return new Rotation(new RealNumber[][]{
            {cos, sin.negation(), Z},
            {sin, cos, Z},
            {Z, Z, U}
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

    @Override
    public String toString() {
        return rot.toString();
    }

}
