package org.meeuw.math.abstractalgebra.dim3;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.abstractalgebra.WithScalarOperations;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

/**
 * A square 3x3 matrix of {@code double}s
 *
 * Note this does not use {@link UncertainDouble} to back the matrix, but simple 'doubles'
 * This means that rounding errors are not considered, and theory testing may involve some fiddling with {@link jdk
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public strictfp class Matrix3 implements
    MultiplicativeGroupElement<Matrix3>, WithScalarOperations<Matrix3, RealNumber> {


    final double[][] values;

    public static Matrix3 of(
        double v11, double v12, double v13,
        double v21, double v22, double v23,
        double v31, double v32, double v33
    ) {
        return new Matrix3(new double[][] {
            {v11, v12, v13},
            {v21, v22, v23},
            {v31, v32, v33}
        });
    }


    private static Matrix3 of(double[] value) {
        return of(
                value[0], value[1], value[2],
                value[3], value[4], value[5],
                value[6], value[7], value[8]
        );
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
    public Matrix3Group getStructure() {
        return Matrix3Group.INSTANCE;
    }

    double[] timesDouble(double[][] matrix3) {
        double[] result = new double[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double v = 0;
                for (int k = 0; k < 3; k++) {
                    v += values[i][k] * matrix3[k][j];
                }
                result[i * 3 + j] = v;
            }
        }
        return result;
    }

    double[] timesDouble(double multiplier) {
        double[] result = new double[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i * 3 + j] = multiplier * values[i][j];
            }
        }
        return result;
    }

    public Matrix3 times(double multiplier) {
        return Matrix3.of(timesDouble(multiplier));
    }
    public Matrix3 dividedBy(double multiplier) {
        return Matrix3.of(timesDouble(1 / multiplier));
    }

    @Override
    public Matrix3 times(RealNumber multiplier) {
        return times(multiplier.getValue());
    }

    @Override
    public Matrix3 dividedBy(RealNumber divisor) {
        return dividedBy(divisor.getValue());
    }


    double determinant() {
        double a = values[0][0];
        double b = values[0][1];
        double c = values[0][2];
        double d = values[1][0];
        double e = values[1][1];
        double f = values[1][2];
        double g = values[2][0];
        double h = values[2][1];
        double i = values[2][2];
        return
            a * (e * i - f * h)
                -  b * (d * i - f * g)
                + c * (d * h - e * g);
    }
    double determinant2x2(double a, double b, double c, double d) {
        return a * d - b * c;
    }


     @Override
    // https://www.mathsisfun.com/algebra/matrix-inverse-minors-cofactors-adjugate.html
    public Matrix3 reciprocal() {
        return adjugate().dividedBy(determinant());
    }

    public Matrix3 adjugate() {
        return new Matrix3(adjugateMatrix());
    }

    double[][] adjugateMatrix() {
        final double[][] adjugate =  new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                adjugate[j][i] = determinant2x2(
                    values[skip(0, i)][skip(0, j)], values[skip(0, i)][skip(1, j)],
                    values[skip(1, i)][skip(0, j)], values[skip(1, i)][skip(1, j)]
                );
                if ((i + j) % 2 == 1) {
                    adjugate[j][i] = adjugate[j][i] * -1;
                }
            }
        }
        return adjugate;
    }
    private int skip(int i, int skip) {
        return i < skip ? i : i + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix3 matrix3 = (Matrix3) o;
        return getStructure().getEquivalence().test(this, matrix3);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "(" + Arrays.stream(asVectors()).map(Vector3::toString).collect(Collectors.joining(", ")) + ")";
    }
}
