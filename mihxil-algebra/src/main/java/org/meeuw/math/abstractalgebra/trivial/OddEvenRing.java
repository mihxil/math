package org.meeuw.math.abstractalgebra.trivial;

import java.util.Arrays;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;

@Example(AbelianRing.class)
@Singleton
public class OddEvenRing implements AbelianRing<OddEven>, Streamable<OddEven> {

    public static final OddEvenRing INSTANCE = new OddEvenRing();

    private OddEvenRing() {
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
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
