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

import java.util.Arrays;
import java.util.stream.Collectors;

import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.validation.Square;

import static org.meeuw.math.ArrayUtils.determinant2x2;

/**
 * A square 2x2 matrix of {@code double}s
 * <p>
 * Note this does not use {@link UncertainDouble} to back the matrix, but simple 'doubles'
 * This means that rounding errors are not considered, and theory testing may involve some fiddling.
 *
 * @author Michiel Meeuwissen
 * @since 0.14
 */
public strictfp class Matrix2 implements
    MultiplicativeGroupElement<Matrix2>, WithScalarOperations<Matrix2, RealNumber>, WithDoubleOperations<Matrix2> {


    final double[][] values;

    public static Matrix2 of(
        double v11, double v12,
        double v21, double v22
    ) {
        return new Matrix2(new double[][] {
            {v11, v12},
            {v21, v22}
        });
    }


    private static Matrix2 of(@Square(2) double[] value) {
        return of(
                value[0], value[1],
                value[2], value[3]
        );
    }

    Matrix2(double[][] values) {
        this.values = values;
    }

    @Override
    public Matrix2 times(Matrix2 multiplier) {
        return of(timesDouble(multiplier.values));
    }

    public Vector2[] asVectors() {
        return new Vector2[] {
            Vector2.of(values[0][0], values[0][1]),
            Vector2.of(values[1][0], values[1][1])
        };
    }

    @Override
    public Matrix2Group getStructure() {
        return Matrix2Group.INSTANCE;
    }

    double[] timesDouble(@Square(2) double[][] matrix2) {
        double[] result = new double[4];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double v = 0;
                for (int k = 0; k < 2; k++) {
                    v += values[i][k] * matrix2[k][j];
                }
                result[i * 2 + j] = v;
            }
        }
        return result;
    }

    double[] timesDouble(double multiplier) {
        double[] result = new double[4];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                result[i * 2 + j] = multiplier * values[i][j];
            }
        }
        return result;
    }

    @Override
    public Matrix2 times(double multiplier) {
        return Matrix2.of(timesDouble(multiplier));
    }

    @Override
    public Matrix2 dividedBy(double multiplier) {
        return Matrix2.of(timesDouble(1d / multiplier));
    }

    @Override
    public Matrix2 times(RealNumber multiplier) {
        return times(multiplier.doubleValue());
    }

    @Override
    public Matrix2 dividedBy(RealNumber divisor) {
        if (divisor.isZero()) {
            throw new DivisionByZeroException(this, divisor);
        }
        return dividedBy(divisor.doubleValue());
    }

    public double determinant() {
        double a = values[0][0];
        double b = values[0][1];
        double c = values[1][0];
        double d = values[1][1];
        return determinant2x2(a, b, c, d);
    }

     @Override
    // https://www.mathsisfun.com/algebra/matrix-inverse-minors-cofactors-adjugate.html
    public Matrix2 reciprocal() {
        return adjugate().dividedBy(determinant());
    }

    public Matrix2 adjugate() {
        return new Matrix2(adjugateMatrix());
    }

    double[][] adjugateMatrix() {
        double a = values[0][0];
        double b = values[0][1];
        double c = values[1][0];
        double d = values[1][1];
        final double[][] adjugate =  new double[2][2];
        adjugate[0][0] = d;
        adjugate[0][1] = -1 * b;
        adjugate[1][0] = -1 * c;
        adjugate[1][1] = a;
        return adjugate;
    }

    private int skip(int i, int skip) {
        return i < skip ? i : i + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix2 matrix3 = (Matrix2) o;
        return getStructure().getEquivalence().test(this, matrix3);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "(" + Arrays.stream(asVectors()).map(Vector2::toString).collect(Collectors.joining(", ")) + ")";
    }

    @Square(3)
    public double[][] getValues() {
        return Arrays.stream(values).map(double[]::clone).toArray(double[][]::new);
    }
}
