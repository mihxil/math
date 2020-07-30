package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.NumberFieldElement;
import org.meeuw.math.abstractalgebra.bigdecimal.BigDecimalElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

import java.math.BigDecimal;

/**
 * @author Michiel Meeuwissen
 */
public class FieldVector3<F extends NumberFieldElement<F>> {

    final F x;
    final F y;
    final F z;

    public static <F extends NumberFieldElement<F>> FieldVector3<F> of(F x, F y, F z) {
        return new FieldVector3<>(x, y, z);
    }

    public static FieldVector3<RealNumber> of(double x, double y, double z) {
        return new FieldVector3<>(new RealNumber(x), new RealNumber(y), new RealNumber(z));
    }

    public static FieldVector3<BigDecimalElement> of(BigDecimal x, BigDecimal y, BigDecimal z) {
        return new FieldVector3<>(new BigDecimalElement(x), new BigDecimalElement(y), new BigDecimalElement(z));
    }

    public FieldVector3(F x, F y, F z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public FieldVector3<F> times(FieldMatrix3<F> matrix3) {
        return of(
            matrix3.values[0][0].times(x).plus(matrix3.values[0][1].times(y)).plus(matrix3.values[0][2].times(z)),
            matrix3.values[1][0].times(x).plus(matrix3.values[1][1].times(y)).plus(matrix3.values[1][2].times(z)),
            matrix3.values[2][0].times(x).plus(matrix3.values[2][1].times(y)).plus(matrix3.values[2][2].times(z))
        );
    }

 /*   public FieldVector3<F> times(double multiplier) {

        return of(x.times(multiplier), y.times(multiplier), z.times(multiplier));
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldVector3<?> vector3 = (FieldVector3) o;

        return vector3.x.equals(x) &&
            vector3.y.equals(y) &&
            vector3.z.equals(z);
    }



    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
}
