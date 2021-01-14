package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.WithScalarOperations;

import static org.meeuw.math.Utils.uncertaintyForDouble;

/**
 * @author Michiel Meeuwissen
 */
public class Vector3  implements WithScalarOperations<Vector3, Double> {

    final double x;
    final double y;
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

    public Vector3 times(double multiplier) {
        return of(x * multiplier, y * multiplier, z * multiplier);
    }

    public Vector3 dividedBy(double divisor) {
        return of(x / divisor, y / divisor, z / divisor);
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
        return "(" + x + "," + y + "," + z + ")";
    }


    @Override
    public Vector3 times(Double multiplier) {
        return new Vector3(x * multiplier, y * multiplier, z * multiplier);
    }

    @Override
    public Vector3 dividedBy(Double divisor) {
        return new Vector3(x / divisor, y / divisor, z / divisor);
    }
}
