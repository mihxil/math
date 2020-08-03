package org.meeuw.physics;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 */
public class DimensionsGroup implements MultiplicativeGroup<Dimensions>, Streamable<Dimensions> {

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

    @Override
    public Stream<Dimensions> stream() {
        // TODO:
        return Stream.empty();

    }
}
