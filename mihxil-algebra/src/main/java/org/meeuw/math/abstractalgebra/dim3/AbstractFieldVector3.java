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

import java.util.Arrays;
import java.util.Iterator;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.NonAlgebraic;
import org.meeuw.math.WithScalarOperations;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.FieldIncompleteException;
import org.meeuw.math.numbers.Sizeable;

/**
 * A 3-dimension vector on a {@link ScalarField}.
 *
 * @author Michiel Meeuwissen
 */
abstract class AbstractFieldVector3<E extends ScalarFieldElement<E,C>, C extends CompleteScalarFieldElement<C>, SELF extends AbstractFieldVector3<E, C, SELF>>
    implements
    Sizeable<E>,
    Vector<SELF, E>,
    WithScalarOperations<SELF, E> {

    final E x;
    final E y;
    final E z;

    public AbstractFieldVector3(E x, E y, E z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    abstract SELF _of(E x, E y, E z);

    public SELF times(FieldMatrix3<E, C> matrix3) {
        return _of(
            matrix3.values[0][0].times(x).plus(matrix3.values[0][1].times(y)).plus(matrix3.values[0][2].times(z)),
            matrix3.values[1][0].times(x).plus(matrix3.values[1][1].times(y)).plus(matrix3.values[1][2].times(z)),
            matrix3.values[2][0].times(x).plus(matrix3.values[2][1].times(y)).plus(matrix3.values[2][2].times(z))
        );
    }

 /*   public FieldVector3<E> times(double multiplier) {

        return of(x.times(multiplier), y.times(multiplier), z.times(multiplier));
    }*/

    @Override
    @NonAlgebraic
    public E abs() {
        E result  = (x.sqr().plus(y.sqr()).plus(z.sqr()));
        if (result instanceof CompleteScalarFieldElement) {
            return (E) ((CompleteScalarFieldElement<?>) result).sqrt();
        } else {
            throw new FieldIncompleteException("Field of " + this + " is not complete");
        }
    }

    @Override
    public SELF times(E multiplier) {
        return _of(x.times(multiplier), y.times(multiplier), z.times(multiplier));
    }

    @Override
    public SELF plus(SELF summand) {
        return _of(
            x.plus(summand.x),
            y.plus(summand.y),
            z.plus(summand.z)
        );
    }

    @Override
    public E dot(SELF multiplier) {
        return (x.times(multiplier.x)).plus(y.times(multiplier.y)).plus(z.times(multiplier.z));
    }

    @Override
    public SELF negation() {
        return _of(x.negation(), y.negation(), z.negation());
    }

    @Override
    public E get(int i) {
        return switch (i) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> throw new ArrayIndexOutOfBoundsException();
        };
    }

    @Override
    public SELF dividedBy(E divisor) {
        return _of(x.dividedBy(divisor), y.dividedBy(divisor), z.dividedBy(divisor));
    }

    @Override
    @lombok.NonNull
    public Iterator<E> iterator() {
        return Arrays.asList(x, y, z).iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SELF vector3 = (SELF) o;

        return x.equals(vector3.x) &&
            y.equals(vector3.y) &&
            z.equals(vector3.z);
    }

    @Override
    public boolean eq(SELF o) {
        if (this == o) return true;

        return x.eq(o.x) &&
            y.eq(o.y) &&
            z.eq(o.z);
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        result = 31 * result + z.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    @Override
    public @NonNull AbelianRing<SELF> getStructure() {
        return getSpace();
    }

    @Override
    public SELF times(SELF multiplier) {
        return _of(x.times(multiplier.x), y.times(multiplier.y), z.times(multiplier.z));
    }

    public SELF withX(E x) {
        return _of(x, y, z);
    }

    public SELF withY(E y) {
        return _of(x, y, z);
    }

    public SELF withZ(E z) {
        return _of(x, y, z);
    }
}
