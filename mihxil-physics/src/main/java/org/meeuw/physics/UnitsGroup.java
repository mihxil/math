package org.meeuw.physics;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class UnitsGroup extends AbstractAlgebraicStructure<Units> implements MultiplicativeGroup<Units>, Streamable<Units> {

    private static final Units ONE = UnitsImpl.DIMENSIONLESS;

    public static final UnitsGroup INSTANCE = new UnitsGroup();

    private UnitsGroup() {
        super(Units.class);
    }

    @Override
    public Units one() {
        return ONE;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public Stream<Units> stream() {
        // TODO
        return Stream.empty();
    }
}
