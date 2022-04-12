/*
 *  Copyright 2022 Michiel Meeuwissen
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.meeuw.math.abstractalgebra.dim3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.meeuw.math.Equivalence;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.exceptions.ReciprocalException;
import org.meeuw.math.validation.Square;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FieldMatrix3<E extends ScalarFieldElement<E>>
    implements MultiplicativeGroupElement<FieldMatrix3<E>>,
    WithScalarOperations<FieldMatrix3<E>, E>
{

    @Square(3)
    final E[][] values;

    final ScalarField<E> elementStructure;

    final E zero;

    public static <E extends ScalarFieldElement<E>> FieldMatrix3<E> of(
        E v00, E v01, E v02,
        E v10, E v11, E v12,
        E v20, E v21, E v22) {
        return of(v00.getStructure().getElementClass(),
            v00, v01, v02,
            v10, v11, v12,
            v20, v21, v22);

    }


    @SuppressWarnings("unchecked")
    static <E extends ScalarFieldElement<E>> FieldMatrix3<E> of(
        Class<E> clazz,
        E v00, E v01, E v02,
        E v10, E v11, E v12,
        E v20, E v21, E v22
        ) {
        E[][] fs = (E[][]) Array.newInstance(clazz, 3, 3);
        fs[0][0]  = v00;
        fs[0][1]  = v01;
        fs[0][2]  = v02;

        fs[1][0]  = v10;
        fs[1][1]  = v11;
        fs[1][2]  = v12;

        fs[2][0]  = v20;
        fs[2][1]  = v21;
        fs[2][2]  = v22;
        FieldMatrix3<E> fm =  new FieldMatrix3<>(fs);
        if (fm.determinant().isZero()) {
            throw new InvalidElementCreationException("Determinant is zero, so this is not invertiable " + fm);
        }
        return fm;

    }

    public FieldMatrix3(@Square(3) E[][] values) {
        this.elementStructure = values[0][0].getStructure();
        this.values = values;
        this.zero = this.elementStructure.zero();
    }

    @Override
    public FieldMatrix3<E> times(FieldMatrix3<E> multiplier) {
        return new FieldMatrix3<>(timesMatrix(multiplier.values));
    }

    @Override
    public FieldMatrix3<E> times(E multiplier) {
        E[][] result = empty();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = values[i][j].times(multiplier);
            }
        }
        return new FieldMatrix3<>(result);
    }

    @Override
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
    public FieldMatrix3<E> reciprocal() throws ReciprocalException {
        try {
            E det = determinant();
            if (det.isZero()) {
                throw new ReciprocalException("Determinant of " + this + " is zero");
            }

            return adjugate().dividedBy(det);
        } catch (InvalidElementCreationException invalidElementCreationException) {
            throw new ReciprocalException(invalidElementCreationException);
        }
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

    E[][] timesMatrix(@Square(3) E[][] matrix3) {
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

    public E determinant() {
        E a = values[0][0];
        E b = values[0][1];
        E c = values[0][2];
        E d = values[1][0];
        E e = values[1][1];
        E f = values[1][2];
        E g = values[2][0];
        E h = values[2][1];
        E i = values[2][2];
        return a.times((e.times(i)).minus(f.times(h)))
                .minus(
                    b.times((d.times(i)).minus(f.times(g)))
                ).plus(
                    c.times((d.times(h)).minus(e.times(g)))
            );
    }

    E determinant2x2(E a, E b, E c, E d) {
        return a.times(d).minus(b.times(c));
    }



    @Override
    public String toString() {
        return "(" + Arrays.stream(asVectors()).map(FieldVector3::toString).collect(Collectors.joining(", ")) + ")";
    }

    @SuppressWarnings("unchecked")
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
