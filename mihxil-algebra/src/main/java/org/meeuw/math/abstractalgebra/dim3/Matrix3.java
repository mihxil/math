package org.meeuw.math.abstractalgebra.dim3;

import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@EqualsAndHashCode
public class Matrix3 implements MultiplicativeGroupElement<Matrix3, Matrix3Group> {

    final double[][] values;

    public static Matrix3 of(double[][] values) {
        return new Matrix3(values);
    }

    Matrix3(double[][] values) {
        this.values = values;
    }

     Matrix3() {
         this.values = new double[][] {
             {0, 0, 0},
             {0, 0, 0},
             {0, 0, 0}
         };
     }

    @Override
    public Matrix3 times(Matrix3 multiplier) {
        return of(timesDouble(multiplier.values));
    }

    public Vector3[] asVectors() {
        return new Vector3[] {
            Vector3.of(values[0][0], values[0][1], values[0][2]),
            Vector3.of(values[1][0], values[1][1], values[1][2]),
            Vector3.of(values[2][0], values[2][1], values[2][2])
        };
    }

    @Override
    public Matrix3 pow(int exponent) {
        return null;

    }

    @Override
    public Matrix3Group structure() {
        return Matrix3Group.INSTANCE;

    }

    @Override
    public Matrix3 self() {
        return this;

    }


    double[][] timesDouble(double[][] matrix3) {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double v = 0;
                for (int k = 0; k < 3; k++) {
                    v += values[i][k] * matrix3[k][j];
                }
                result[i][j] = v;
            }
        }
        return result;
    }

    double[][] timesDouble(double multiplier) {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = multiplier * values[i][j];
            }
        }
        return result;
    }

    public Matrix3 times(double multiplier) {
        return Matrix3.of(timesDouble(multiplier));
    }

    double determinant() {
        //double A =(values[0][2] *  values[2][2] - );
        double B;
        double C;
        return 0;
    }

    @Override
    public String toString() {
        return "(" + Arrays.stream(asVectors()).map(Vector3::toString).collect(Collectors.joining(", ")) + ")";
    }
}
