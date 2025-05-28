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
package org.meeuw.math.abstractalgebra.dim2;

import lombok.With;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;

import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.WithScalarOperations;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.BigDecimalElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.FieldIncompleteException;
import org.meeuw.math.numbers.Sizeable;

import static java.math.BigDecimal.ZERO;

/**
 * A 2-dimensional vector on a {@link CompleteField}.
 *
 * @author Michiel Meeuwissen
 */
public class FieldVector2<E extends ScalarFieldElement<E>>
    implements
    Sizeable<E>,
    Vector<FieldVector2<E>, E>,
    WithScalarOperations<FieldVector2<E>, E> {

    @With
    final E x;
    @With
    final E y;


    public static <E extends ScalarFieldElement<E>> FieldVector2<E> of(E x, E y) {
        return new FieldVector2<>(x, y);
    }

    public static FieldVector2<RealNumber> of(double x, double y) {
        return new FieldVector2<>(RealNumber.of(x), RealNumber.of(y));
    }

    public static FieldVector2<BigDecimalElement> of(BigDecimal x, BigDecimal y) {
        return of(new BigDecimalElement(x, ZERO), new BigDecimalElement(y, ZERO));
    }

    public FieldVector2(E x, E y) {
        this.x = x;
        this.y = y;
    }

    public FieldVector2<E> times(FieldMatrix2<E> matrix2) {
        return of(
            matrix2.values[0][0].times(x).plus(matrix2.values[0][1].times(y)),
            matrix2.values[1][0].times(x).plus(matrix2.values[1][1].times(y))
        );
    }

    @Override
    @NonAlgebraic
    public E abs() {
        E result  = (x.sqr().plus(y.sqr()));
        if (result instanceof CompleteFieldElement) {
            return (E) ((CompleteFieldElement<?>) result).sqrt();
        } else {
            throw new FieldIncompleteException("Field of " + this + " is not complete");
        }
    }

    @Override
    public FieldVector2<E> times(E multiplier) {
        return of(x.times(multiplier), y.times(multiplier));
    }

    @Override
    public FieldVector2<E> plus(FieldVector2<E> summand) {
        return of(
            x.plus(summand.x),
            y.plus(summand.y)
        );
    }

    @Override
    public E dot(FieldVector2<E> multiplier) {
        return (x.times(multiplier.x)).plus(y.times(multiplier.y));
    }

    @Override
    public FieldVector2<E> negation() {
        return of(x.negation(), y.negation());
    }

    @Override
    public E get(int i) {
        switch(i) {
            case 0: return x;
            case 1: return y;
            default: throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public VectorSpace<E, FieldVector2<E>> getSpace() {
        return FieldVector2Space.of(x.getStructure());
    }

    @Override
    public FieldVector2<E> dividedBy(E divisor) {
        return new FieldVector2<>(x.dividedBy(divisor), y.dividedBy(divisor));
    }

    @Override
    @lombok.NonNull
    public Iterator<E> iterator() {
        return Arrays.asList(x, y).iterator();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldVector2<?> vector3 = (FieldVector2) o;

        return x.equals(vector3.x) &&
            y.equals(vector3.y);
    }

    @Override
    public boolean eq(FieldVector2<E> o) {
        if (this == o) return true;

        return x.eq(o.x) &&
            y.eq(o.y);
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y  + ")";
    }

    @Override
    public AbelianRing<FieldVector2<E>> getStructure() {
        return getSpace();
    }

    @Override
    public FieldVector2<E> times(FieldVector2<E> multiplier) {
        return new FieldVector2<>(x.times(multiplier.x), y.times(multiplier.y));
    }
}
