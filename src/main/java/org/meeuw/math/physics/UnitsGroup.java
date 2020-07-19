package org.meeuw.math.physics;

import org.meeuw.math.abstractalgebra.Group;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class UnitsGroup implements Group<Units> {

    private static final Units ONE = UnitsImpl.DIMENSIONLESS;

    public static final UnitsGroup INSTANCE = new UnitsGroup();

    private UnitsGroup() {

    }

    @Override
    public Units one() {
        return ONE;

    }
}
