package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Matrix3Group extends AbstractAlgebraicStructure<Matrix3> implements MultiplicativeGroup<Matrix3> {

    public static final Matrix3Group INSTANCE = new Matrix3Group();

    private Matrix3Group() {
        super(Matrix3.class);
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
