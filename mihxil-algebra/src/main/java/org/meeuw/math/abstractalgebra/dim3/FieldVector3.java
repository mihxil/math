package org.meeuw.math.abstractalgebra.dim3;

import java.math.BigDecimal;

import org.meeuw.math.abstractalgebra.NumberFieldElement;
import org.meeuw.math.abstractalgebra.NumberField;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

import static java.math.BigDecimal.ZERO;

/**
 * A 3 dimension vector on a {@link NumberField}.
 * @author Michiel Meeuwissen
 */
public class FieldVector3<E extends NumberFieldElement<E>> {

    final E x;
    final E y;
    final E z;

    public static <E extends NumberFieldElement<E>> FieldVector3<E> of(E x, E y, E z) {
        return new FieldVector3<>(x, y, z);
    }

    public static FieldVector3<RealNumber> of(double x, double y, double z) {
        return new FieldVector3<>(RealNumber.of(x), RealNumber.of(y), RealNumber.of(z));
    }

    public static FieldVector3<BigDecimalElement> of(BigDecimal x, BigDecimal y, BigDecimal z) {
        return new FieldVector3<>(new BigDecimalElement(x, ZERO), new BigDecimalElement(y, ZERO), new BigDecimalElement(z, ZERO));
    }

    public FieldVector3(E x, E y, E z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public FieldVector3<E> times(FieldMatrix3<E> matrix3) {
        return of(
            matrix3.values[0][0].times(x).plus(matrix3.values[0][1].times(y)).plus(matrix3.values[0][2].times(z)),
            matrix3.values[1][0].times(x).plus(matrix3.values[1][1].times(y)).plus(matrix3.values[1][2].times(z)),
            matrix3.values[2][0].times(x).plus(matrix3.values[2][1].times(y)).plus(matrix3.values[2][2].times(z))
        );
    }

 /*   public FieldVector3<E> times(double multiplier) {

        return of(x.times(multiplier), y.times(multiplier), z.times(multiplier));
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldVector3<?> vector3 = (FieldVector3) o;

        return x.equalsWithEpsilon((E) vector3.x) &&
            y.equalsWithEpsilon((E) vector3.y) &&
            z.equalsWithEpsilon((E) vector3.z);
    }



    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
}
