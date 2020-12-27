package org.meeuw.math.abstractalgebra.dim3;

import java.math.BigDecimal;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.numbers.Sizeable;

import static java.math.BigDecimal.ZERO;

/**
 * A 3 dimension vector on a {@link CompleteField}.
 * @author Michiel Meeuwissen
 */
public class FieldVector3<E extends ScalarFieldElement<E>>
    implements
    Sizeable<E>,
    WithScalarOperations<FieldVector3<E>, E> {

    final E x;
    final E y;
    final E z;

    public static <E extends ScalarFieldElement<E>> FieldVector3<E> of(E x, E y, E z) {
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

        return x.equals((E) vector3.x) &&
            y.equals((E) vector3.y) &&
            z.equals((E) vector3.z);
    }



    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    @Override
    public E abs() {
        E result  = (x.sqr().plus(y.sqr()).plus(z.sqr()));
        if (result instanceof CompleteFieldElement) {
            return (E) ((CompleteFieldElement<?>) result).sqrt();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public FieldVector3<E> times(E multiplier) {
        return new FieldVector3<>(x.times(multiplier), y.times(multiplier), z.times(multiplier));
    }

    @Override
    public FieldVector3<E> dividedBy(E divisor) {
        return new FieldVector3<>(x.dividedBy(divisor), y.dividedBy(divisor), z.dividedBy(divisor));
    }
}
