package org.meeuw.math.abstractalgebra.bool;

import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.operators.*;

import static org.meeuw.configuration.ReflectionUtils.getDeclaredBinaryMethod;
import static org.meeuw.configuration.ReflectionUtils.getDeclaredMethod;

/**
 * The booleans form a <a href="https://en.wikipedia.org/wiki/Boolean_ring">Ring</a>
 * @since 0.20
 */
@Singleton
@Example(AbelianRing.class)
public class BooleanRing implements AbelianRing<BooleanElement>, Streamable<BooleanElement> {


    public static final BooleanRing INSTANCE = new BooleanRing();

    SimpleAlgebraicBinaryOperator AND = new SimpleAlgebraicBinaryOperator(
        getDeclaredBinaryMethod(BooleanElement.class, "and"),
        "∧",
        2,
        "conjunction"
    );

    SimpleAlgebraicBinaryOperator XOR = new SimpleAlgebraicBinaryOperator(
        getDeclaredBinaryMethod(BooleanElement.class, "xor"),
        "⊕",
        2,
        "exclusive disjunction"
    );

    SimpleAlgebraicBinaryOperator OR = new SimpleAlgebraicBinaryOperator(
        getDeclaredBinaryMethod(BooleanElement.class, "or"),
        "∨",
        2,
        "disjunction"
    );

    AlgebraicUnaryOperator NOT = new SimpleAlgebraicUnaryOperator(
        getDeclaredMethod(BooleanElement.class, "not"),
        "¬",
        "negation"
    );


    private BooleanRing()  {
    }

    @Override
    public NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return CollectionUtils.navigableSet(AbelianRing.super.getSupportedOperators(), AND, OR, XOR);
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

    @Override
    public String toString() {
        return "R";
    }
}
