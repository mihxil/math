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

import lombok.With;

import java.util.Iterator;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.DivisionByZeroException;

import static org.meeuw.math.Utils.uncertaintyForDouble;

/**
 * @author Michiel Meeuwissen
 */
public class Vector3 implements
    Vector<Vector3, RealNumber>,
    WithScalarOperations<Vector3, RealNumber> {

    @With
    final double x;

    @With
    final double y;

    @With
    final double z;

    public static Vector3 of(double x, double y, double z) {
        return new Vector3(x, y, z);
    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 times(Matrix3 matrix3) {
        return of(
            matrix3.values[0][0] * x +  matrix3.values[0][1] * y + matrix3.values[0][2] * z,
            matrix3.values[1][0] * x +  matrix3.values[1][1] * y + matrix3.values[1][2] * z,
            matrix3.values[2][0] * x +  matrix3.values[2][1] * y + matrix3.values[2][2] * z
        );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3 vector3 = (Vector3) o;

        if (Math.abs(vector3.x - x) > 2 * uncertaintyForDouble(x)) return false;
        if (Math.abs(vector3.y -  y) > 2 * uncertaintyForDouble(y)) return false;
        return Math.abs(vector3.z -  z) < 2 * uncertaintyForDouble(z);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @Override
    public String toString() {
        return "(" + get(0) + ", " + get(1) + ", " + get(2) + ")";
    }


    public Vector3 times(double multiplier) {
        return of(x * multiplier, y * multiplier, z * multiplier);
    }

    public Vector3 dividedBy(double divisor) {
        if (divisor == 0) {
            throw new DivisionByZeroException(this, divisor);
        }
        return of(x / divisor, y / divisor, z / divisor);
    }

    @Override
    public Vector3 times(RealNumber multiplier) {
        return times(multiplier.doubleValue());
    }

    @Override
    public Vector3 dividedBy(RealNumber divisor) {
        return dividedBy(divisor.doubleValue());
    }

    @Override
    public Vector3 plus(Vector3 summand) {
        return of(x + summand.x, y + summand.y, z + summand.z);
    }

    @Override
    public RealNumber dot(Vector3 multiplier) {
        return RealNumber.of(x * multiplier.x + y * multiplier.y + z * multiplier.z);
    }

    @Override
    public Vector3 negation() {
        return of(-1 * x, -1 * y, -1 * z);
    }

    @Override
    public RealNumber get(int i) {
        switch(i) {
            case 0:
                return RealNumber.of(x);
            case 1:
                return RealNumber.of(y);
            case 2:
                return RealNumber.of(z);
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public VectorSpace<RealNumber, Vector3> getSpace() {
        return Vector3Space.INSTANCE;
    }

    @Override
    public Iterator<RealNumber> iterator() {
        return Stream.of(x, y, z).map(RealNumber::of).iterator();
    }

    @Override
    public AbelianRing<Vector3> getStructure() {
        return getSpace();
    }

    @Override
    public Vector3 times(Vector3 multiplier) {
        return of(x * multiplier.x, y * multiplier.y, z * multiplier.z);
    }
}
