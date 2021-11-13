package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(VectorSpaceInterface.class)
public class Vector3Space implements VectorSpaceInterface<RealNumber, Vector3>, AbelianRing<Vector3> {

    public static final Vector3Space INSTANCE = new Vector3Space();

    @Override
    public int getDimension() {
        return 3;
    }

    @Override
    public Vector3 zero() {
        return Vector3.of(0, 0, 0);
    }

    @Override
    public RealField getField() {
        return RealField.INSTANCE;
    }

    @Override
    public Vector3 one() {
        return Vector3.of(1, 1, 1);
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_1;
    }

    @Override
    public Class<Vector3> getElementClass() {
        return Vector3.class;
    }
}
