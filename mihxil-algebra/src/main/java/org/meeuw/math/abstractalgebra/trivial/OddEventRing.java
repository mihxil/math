package org.meeuw.math.abstractalgebra.trivial;

import java.util.Arrays;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;

@Example(AbelianRing.class)
@Singleton
public class OddEventRing implements AbelianRing<OddEven>, Streamable<OddEven> {

    public static final OddEventRing INSTANCE = new OddEventRing();

    private OddEventRing() {
        // singleton
    }

    @Override
    public OddEven one() {
        return OddEven.odd;
    }

    @Override
    public OddEven zero() {
        return OddEven.even;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.of(OddEven.values().length);
    }

    @Override
    public Class<OddEven> getElementClass() {
        return OddEven.class;
    }

    @Override
    public Stream<OddEven> stream() {
        return Arrays.stream(OddEven.values());
    }
}
