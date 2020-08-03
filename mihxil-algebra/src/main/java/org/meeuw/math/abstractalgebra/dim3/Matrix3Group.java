package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.Cardinality;
import org.meeuw.math.abstractalgebra.MultiplicativeGroup;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Matrix3Group implements MultiplicativeGroup<Matrix3> {

    public static final Matrix3Group INSTANCE = new Matrix3Group();

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

    @Override
    public Cardinality cardinality() {
        return Cardinality.ALEPH_1;
    }
}
