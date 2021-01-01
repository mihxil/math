package org.meeuw.math.abstractalgebra.dim3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.meeuw.math.Equivalence;
import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FieldMatrix3<E extends ScalarFieldElement<E>>
    implements MultiplicativeGroupElement<FieldMatrix3<E>> {

    final E[][] values;

    final ScalarField<E> elementStructure;

    final E zero;

    private static <E extends CompleteFieldElement<E>> FieldMatrix3<E> of(E[] array) {
        return of(
            array[0], array[1], array[2],
            array[3], array[4], array[5],
            array[6], array[7], array[8]
        );
    }

    @SuppressWarnings("unchecked")
    public static <E extends ScalarFieldElement<E>> FieldMatrix3<E> of(
        E v00, E v01, E v02,
        E v10, E v11, E v12,
        E v20, E v21, E v22
        ) {
        E[][] fs = (E[][]) Array.newInstance(v11.getClass(), 3, 3);
        fs[0][0]  = v00;
        fs[0][1]  = v01;
        fs[0][2]  = v02;

        fs[1][0]  = v10;
        fs[1][1]  = v11;
        fs[1][2]  = v12;

        fs[2][0]  = v20;
        fs[2][1]  = v21;
        fs[2][2]  = v22;
        return new FieldMatrix3<E>(fs);
    }

    FieldMatrix3(E[][] values) {
        this.elementStructure = values[0][0].getStructure();
        this.values = values;
        this.zero = this.elementStructure.zero();
        assert !determinant().isZero(); // the object _must be invertible__
    }

    @Override
    public FieldMatrix3<E> times(FieldMatrix3<E> multiplier) {
        return new FieldMatrix3<>(timesMatrix(multiplier.values));
    }

    public FieldMatrix3<E> times(E multiplier) {
        E[][] result = empty();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = values[i][j].times(multiplier);
            }
        }
        return new FieldMatrix3<>(result);
    }

    public FieldMatrix3<E> dividedBy(E divisor) {
        return times(divisor.reciprocal());
    }

    @SuppressWarnings({"unchecked"})
    public FieldVector3<E>[] asVectors() {
        FieldVector3<E>[] result = (FieldVector3<E>[]) new FieldVector3[3];
        result[0] = FieldVector3.of(values[0][0], values[0][1], values[0][2]);
        result[1] = FieldVector3.of(values[1][0], values[1][1], values[1][2]);
        result[2] = FieldVector3.of(values[2][0], values[2][1], values[2][2]);
        return result;
    }

    @Override
    // https://www.mathsisfun.com/algebra/matrix-inverse-minors-cofactors-adjugate.html
    public FieldMatrix3<E> reciprocal() {
        E det = determinant();
        if (det.isZero()) {
            throw new IllegalStateException("Determinant of " + this + " is zero");
        }
        return adjugate().dividedBy(det);
    }

    public FieldMatrix3<E> adjugate() {
        return new FieldMatrix3<>(adjugateMatrix());
    }

    E[][] adjugateMatrix() {
        final E[][] adjugate =  empty();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                adjugate[j][i] = determinant2x2(
                    values[skip(0, i)][skip(0, j)], values[skip(0, i)][skip(1, j)],
                    values[skip(1, i)][skip(0, j)], values[skip(1, i)][skip(1, j)]
                );
                if ((i + j) % 2 == 1) {
                    adjugate[j][i] = adjugate[j][i].negation();
                }
            }
        }
        return adjugate;
    }
    private int skip(int i, int skip) {
        return i < skip ? i : i + 1;
    }
/*

    private E[][] transposedMatrix() {
        final E[][] transpose =  empty();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                transpose[i][j] = values[j][i];
            }
        }
        return transpose;
    }
*/

    @SuppressWarnings("unchecked")
    private E[][] empty() {
         return (E[][]) Array.newInstance(elementStructure.getElementClass(), 3, 3);
    }

    @Override
    public FieldMatrix3Group<E> getStructure() {
        return FieldMatrix3Group.of(elementStructure);
    }

    E[][] timesMatrix(E[][] matrix3) {
        E[][] result = empty();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                E v = elementStructure.zero();
                for (int k = 0; k < 3; k++) {
                    v = v.plus(values[i][k].times(matrix3[k][j]));
                }
                result[i][j] = v;
            }
        }
        return result;
    }

    E determinant() {
        E a = values[0][0];
        E b = values[0][1];
        E c = values[0][2];
        E d = values[1][0];
        E e = values[1][1];
        E f = values[1][2];
        E g = values[2][0];
        E h = values[2][1];
        E i = values[2][2];
        return
            a.times(e.times(i))
                .minus(a.times(f).times(h))
                .minus(b.times(d).times(i))
                .plus(b.times(f).times(g))
                .plus(c.times(d).times(h))
                .minus(c.times(e).times(g));
    }

    E determinant2x2(E a, E b, E c, E d) {
        return a.times(d).minus(b.times(c));
    }



    @Override
    public String toString() {
        return "(" + Arrays.stream(asVectors()).map(FieldVector3::toString).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldMatrix3<E> that = (FieldMatrix3<E>) o;
        Equivalence<E> equivalence = elementStructure.getEquivalence();
        boolean result = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result &= equivalence.test(values[i][j], that.values[i][j]);
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
