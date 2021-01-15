package org.meeuw.math.abstractalgebra.dim3;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Vector3Space implements VectorSpaceInterface<RealNumber, Vector3> {

    public static Vector3Space INSTANCE = new Vector3Space();

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
}
