package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.MultiplicativeGroup;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class Matrix3Group implements MultiplicativeGroup<Matrix3, Matrix3Group> {

    public static Matrix3Group INSTANCE = new Matrix3Group();

    private Matrix3Group() {

    }

    @Override
    public Matrix3 one() {
        return new Matrix3(new double[][] {
            {1, 0, 0},
            {0, 1, 0},
            {0 , 0, 1}
        });

    }
}
