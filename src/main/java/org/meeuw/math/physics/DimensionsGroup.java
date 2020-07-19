package org.meeuw.math.physics;

import org.meeuw.math.abstractalgebra.MultiplicativeGroup;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class DimensionsGroup implements MultiplicativeGroup<Dimensions, DimensionsGroup> {

    private static final Dimensions ONE = new Dimensions();

    public static final DimensionsGroup INSTANCE = new DimensionsGroup();

    private DimensionsGroup() {

    }

    @Override
    public Dimensions one() {
        return ONE;

    }
}
