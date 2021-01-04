package org.meeuw.math.abstractalgebra.dim3;

/**
 * @author Michiel Meeuwissen
 */
public class Vector3  {

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

        if (Double.compare(vector3.x, x) != 0) return false;
        if (Double.compare(vector3.y, y) != 0) return false;
        return Double.compare(vector3.z, z) == 0;
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


}
