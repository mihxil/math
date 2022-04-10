package org.meeuw.math.abstractalgebra.trivial;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

public class TrivialRing implements Ring<TrivialRingElement>, Streamable<TrivialRingElement> {

    public  static final TrivialRing INSTANCE = new TrivialRing();

    @Override
    public TrivialRingElement zero() {
        return TrivialRingElement.e;
    }

    @Override
    public TrivialRingElement one() {
        return TrivialRingElement.e;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ONE;
    }

    @Override
    public Class<TrivialRingElement> getElementClass() {
        return TrivialRingElement.class;
    }

    @Override
    public String toString() {
        return "{0}";

    }

    @Override
    public Stream<TrivialRingElement> stream() {
        return Stream.of(TrivialRingElement.e);
    }
}
