package org.meeuw.math.abstractalgebra.bool;

import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.Singleton;
import org.meeuw.math.abstractalgebra.*;

/**
 * The booleans form a <a href="https://en.wikipedia.org/wiki/Boolean_ring">Ring</a>
 * @since 0.20
 */
@Singleton
public class BooleanRing implements AbelianRing<BooleanElement>, Streamable<BooleanElement> {

    public static final BooleanRing INSTANCE = new BooleanRing();

    private BooleanRing() {
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.of(BooleanElement.values().length);
    }

    @Override
    public Class<BooleanElement> getElementClass() {
        return BooleanElement.class;
    }
    /**
     * {@inheritDoc}
     * <p>
     * A AND true == A => multiplicative identify = true
     */
    @Override
    public BooleanElement one() {
        return BooleanElement.TRUE;
    }
    /**
     * {@inheritDoc}
     * <p>
     * A ^ false == A => additive identify = false
     */
    @Override
    public BooleanElement zero() {
        return BooleanElement.FALSE;
    }

    @Override
    public Stream<BooleanElement> stream() {
        return Stream.of(BooleanElement.values());
    }

    @Override
    public BooleanElement nextRandom(Random random) {
        return random.nextBoolean() ? BooleanElement.TRUE : BooleanElement.FALSE;
    }

    public BooleanElement conjunction(BooleanElement a, BooleanElement b) {
        return a.and(b);
    }

    public BooleanElement exclusiveDisjunction(BooleanElement a, BooleanElement b) {
        return a.xor(b);
    }
}
