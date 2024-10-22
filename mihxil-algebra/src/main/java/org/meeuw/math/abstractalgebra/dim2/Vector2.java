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

import java.util.Iterator;
import java.util.stream.Stream;

import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.DivisionByZeroException;

import static org.meeuw.math.DoubleUtils.uncertaintyForDouble;

/**
 * A two-dimensional {@link Vector}, backed by {@code double}s.
 * @author Michiel Meeuwissen
 * @since 0.14
 */
public class Vector2 implements
    Vector<Vector2, RealNumber>,
    WithScalarOperations<Vector2, RealNumber>,
    WithDoubleOperations<Vector2> {

    @With
    final double x;

    @With
    final double y;



    public static Vector2 of(double x, double y) {
        return new Vector2(x, y);
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 times(Matrix2 matrix2) {
        return of(
            matrix2.values[0][0] * x +  matrix2.values[0][1] * y,
            matrix2.values[1][0] * x +  matrix2.values[1][1] * y
        );
    }

    @Override
    public boolean eq(Vector2 other) {
        return  x == other.x && y == other.y;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 vector3 = (Vector2) o;

        if (Math.abs(vector3.x - x) > 2 * uncertaintyForDouble(x)) return false;
        if (Math.abs(vector3.y -  y) > 2 * uncertaintyForDouble(y)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @Override
    public String toString() {
        return "(" + get(0) + ", " + get(1) + ")";
    }


    @Override
    public Vector2 times(double multiplier) {
        return of(x * multiplier, y * multiplier);
    }

    @Override
    public Vector2 dividedBy(double divisor) {
        if (divisor == 0) {
            throw new DivisionByZeroException(this, divisor);
        }
        return of(x / divisor, y / divisor);
    }

    @Override
    public Vector2 times(RealNumber multiplier) {
        return times(multiplier.doubleValue());
    }

    @Override
    public Vector2 dividedBy(RealNumber divisor) {
        return dividedBy(divisor.doubleValue());
    }

    @Override
    public Vector2 plus(Vector2 summand) {
        return of(x + summand.x, y + summand.y);
    }

    @Override
    public RealNumber dot(Vector2 multiplier) {
        return RealNumber.of(x * multiplier.x + y * multiplier.y);
    }

    @Override
    public Vector2 negation() {
        return of(-1 * x, -1 * y);
    }

    @Override
    public RealNumber get(int i) {
        switch(i) {
            case 0:
                return RealNumber.of(x);
            case 1:
                return RealNumber.of(y);
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public VectorSpace<RealNumber, Vector2> getSpace() {
        return Vector2Space.INSTANCE;
    }

    @Override
    public Iterator<RealNumber> iterator() {
        return Stream.of(x, y).map(RealNumber::of).iterator();
    }

    @Override
    public AbelianRing<Vector2> getStructure() {
        return getSpace();
    }

    @Override
    public Vector2 times(Vector2 multiplier) {
        return of(x * multiplier.x, y * multiplier.y);
    }
}
