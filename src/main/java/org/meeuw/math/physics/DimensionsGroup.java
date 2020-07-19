package org.meeuw.math.physics;

import org.meeuw.math.abstractalgebra.Group;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class DimensionsGroup implements Group<Dimensions, DimensionsGroup> {

    private static final Dimensions ONE = new Dimensions();

    public static final DimensionsGroup INSTANCE = new DimensionsGroup();

    private DimensionsGroup() {

    }

    @Override
    public Dimensions one() {
        return ONE;

    }
}
