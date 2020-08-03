package org.meeuw.physics;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class UnitsGroup implements MultiplicativeGroup<Units>, Streamable<Units> {

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

    @Override
    public Stream<Units> stream() {
        // TODO
        return Stream.empty();
    }
}
