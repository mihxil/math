package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.Ring;
import org.meeuw.math.abstractalgebra.Streamable;

import java.util.stream.Stream;

/**
 * The Ring of integers, commonly referred to as ℤ.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Integers implements Ring<IntegerElement>, Streamable<IntegerElement> {

    public static final Integers INSTANCE = new Integers();
    public static final IntegerElement ZERO = IntegerElement.of(0);
    public static final IntegerElement ONE = IntegerElement.of(1);

    private Integers() {
    }

    @Override
    public IntegerElement zero() {
        return ZERO;
    }

    @Override
    public IntegerElement one() {
        return ONE;
    }

    @Override
    public Stream<IntegerElement> stream() {
        return Stream.iterate(zero(), i -> i.isPositive() ? i.negation() : i.negation().plus(one()));
    }
}
