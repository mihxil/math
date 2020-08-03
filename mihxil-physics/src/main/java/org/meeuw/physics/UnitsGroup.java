package org.meeuw.physics;

import org.meeuw.math.abstractalgebra.Cardinality;
import org.meeuw.math.abstractalgebra.MultiplicativeGroup;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class UnitsGroup implements MultiplicativeGroup<Units> {

    private static final Units ONE = UnitsImpl.DIMENSIONLESS;

    public static final UnitsGroup INSTANCE = new UnitsGroup();

    private UnitsGroup() {
    }

    @Override
    public Units one() {
        return ONE;
    }

    @Override
    public Cardinality cardinality() {
        return Cardinality.ALEPH_0;
    }
}
