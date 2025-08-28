/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra.dim3;

import java.util.function.UnaryOperator;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.validation.Square;

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

    private Rotation(@Square(3) RealNumber[][] values) {
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

    public FieldMatrix3<RealNumber> asMatrix() {
        return rot;
    }

    @Override
    public FieldVector3<RealNumber> apply(FieldVector3<RealNumber> in) {
        return rotate(in);
    }

    @Override
    public Rotation reciprocal() {
        return new Rotation(
            rot.reciprocal()
        );
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
