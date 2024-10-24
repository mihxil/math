/*
 *  Copyright 2024 Michiel Meeuwissen
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
package org.meeuw.math.abstractalgebra.dim2;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.meeuw.math.Equivalence;
import org.meeuw.math.WithScalarOperations;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.*;
import org.meeuw.math.validation.Square;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class FieldMatrix2<E extends ScalarFieldElement<E>>
    implements MultiplicativeGroupElement<FieldMatrix2<E>>,
    WithScalarOperations<FieldMatrix2<E>, E>
{

    @Square(2)
    final E[][] values;

    final ScalarField<E> elementStructure;

    final E zero;

    public static <E extends ScalarFieldElement<E>> FieldMatrix2<E> of(
        E v00, E v01,
        E v10, E v11) {
        return of(v00.getStructure().getElementClass(),
            v00, v01,
            v10, v11);

    }


    @SuppressWarnings("unchecked")
    static <E extends ScalarFieldElement<E>> FieldMatrix2<E> of(
        Class<E> clazz,
        E v00, E v01,
        E v10, E v11
        ) {
        E[][] fs = (E[][]) Array.newInstance(clazz, 3, 3);
        fs[0][0]  = v00;
        fs[0][1]  = v01;

        fs[1][0]  = v10;
        fs[1][1]  = v11;
        FieldMatrix2<E> fm =  new FieldMatrix2<>(fs);
        if (fm.determinant().isZero()) {
            throw new NotInvertibleException("Determinant is zero, so this is not invertible " + fm);
        }
        return fm;

    }

    public FieldMatrix2(@Square(3) E[][] values) {
        this.elementStructure = values[0][0].getStructure();
        this.values = values;
        this.zero = this.elementStructure.zero();
    }

    @Override
    public FieldMatrix2<E> times(FieldMatrix2<E> multiplier) {
        return new FieldMatrix2<>(timesMatrix(multiplier.values));
    }

    @Override
    public FieldMatrix2<E> times(E multiplier) {
        E[][] result = empty();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                result[i][j] = values[i][j].times(multiplier);
            }
        }
        return new FieldMatrix2<>(result);
    }

    @Override
    public FieldMatrix2<E> dividedBy(E divisor) {
        return times(divisor.reciprocal());
    }

    @SuppressWarnings({"unchecked"})
    public FieldVector2<E>[] asVectors() {
        FieldVector2<E>[] result = (FieldVector2<E>[]) new FieldVector2[2];
        result[0] = FieldVector2.of(values[0][0], values[0][1]);
        result[1] = FieldVector2.of(values[1][0], values[1][1]);
        return result;
    }

    @Override
    // https://www.mathsisfun.com/algebra/matrix-inverse-minors-cofactors-adjugate.html
    public FieldMatrix2<E> reciprocal() throws ReciprocalException {
        try {
            E det = determinant();
            if (det.isZero()) {
                throw new ReciprocalException("Determinant is zero", "det(" + this + ")");
            }

            return adjugate().dividedBy(det);
        } catch (InvalidElementCreationException invalidElementCreationException) {
            throw new ReciprocalException("reciprocal(" + this + ")", invalidElementCreationException);
        }
    }

    public FieldMatrix2<E> adjugate() {
        return new FieldMatrix2<>(adjugateMatrix());
    }

    E[][] adjugateMatrix() {
        final E[][] adjugate =  empty();
        adjugate[0][0] = values[1][1];
        adjugate[1][1] = values[0][0];
        adjugate[1][0] = values[1][0].negation();
        adjugate[0][1] = values[0][1].negation();
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
         return (E[][]) Array.newInstance(elementStructure.getElementClass(), 2, 2);
    }

    @Override
    public FieldMatrix2Group<E> getStructure() {
        return FieldMatrix2Group.of(elementStructure);
    }

    E[][] timesMatrix(@Square(2) E[][] matrix2) {
        E[][] result = empty();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                E v = elementStructure.zero();
                for (int k = 0; k < 2; k++) {
                    v = v.plus(values[i][k].times(matrix2[k][j]));
                }
                result[i][j] = v;
            }
        }
        return result;
    }

    public E determinant() {
        E a = values[0][0];
        E b = values[0][1];
        E c = values[1][0];
        E d = values[1][1];
        return determinant2x2(a, b, c, d);
    }

    public static <E extends ScalarFieldElement<E>> E determinant2x2(E a, E b, E c, E d) {
        return a.times(d).minus(b.times(c));
    }



    @Override
    public String toString() {
        return "(" + Arrays.stream(asVectors()).map(FieldVector2::toString).collect(Collectors.joining(", ")) + ")";
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldMatrix2<E> that = (FieldMatrix2<E>) o;
        Equivalence<E> equivalence = elementStructure.getEquivalence();
        boolean result = true;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
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
