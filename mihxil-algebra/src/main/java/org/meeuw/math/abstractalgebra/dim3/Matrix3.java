package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Matrix3 implements MultiplicativeGroupElement<Matrix3, Matrix3Group> {

    final double[][] values;

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
        return new Matrix3(times(multiplier.values));

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


    double[][] times(double[][] matrix3) {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double v = 0;
                for (int k = 0; k < 3; k++) {
                    v += values[i][k] + matrix3[k][j];
                }
                result[i][j] = v;
            }
        }
        return result;
    }

    double[][] times(double multiplier) {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = multiplier * values[i][j];
            }
        }
        return result;
    }

    double determinant() {
        //double A =(values[0][2] *  values[2][2] - );
        double B;
        double C;
        return 0;
    }
}
