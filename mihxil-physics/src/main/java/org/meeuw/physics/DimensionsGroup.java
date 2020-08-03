package org.meeuw.physics;

import org.meeuw.math.abstractalgebra.Cardinality;
import org.meeuw.math.abstractalgebra.MultiplicativeGroup;

/**
 * @author Michiel Meeuwissen
 */
public class DimensionsGroup implements MultiplicativeGroup<Dimensions> {

    private static final Dimensions ONE = new Dimensions();

    public static final DimensionsGroup INSTANCE = new DimensionsGroup();

    private DimensionsGroup() {
    }

    @Override
    public Dimensions one() {
        return ONE;
    }

    @Override
    public Cardinality cardinality() {
        return Cardinality.ALEPH_0;
    }
}
