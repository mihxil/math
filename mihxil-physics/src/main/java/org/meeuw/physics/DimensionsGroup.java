package org.meeuw.physics;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 */
public class DimensionsGroup extends AbstractAlgebraicStructure<Dimensions> implements MultiplicativeAbelianGroup<Dimensions>, Streamable<Dimensions> {

    private static final Dimensions ONE = new Dimensions();

    public static final DimensionsGroup INSTANCE = new DimensionsGroup();

    private DimensionsGroup() {
        super(Dimensions.class);
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
        long[] counters = new long[Dimension.values().length];
        // TODO:
        return Stream.iterate(new State(), State::next).map(State::dimensions);
    }

    private static class State {
        int[] counters = new int[Dimension.values().length];
        int max = 1;
        int i = 0;
        Dimensions dimensions() {
            return new Dimensions(counters);
        }
        public State next() {
          return this;
        }
    }
}
