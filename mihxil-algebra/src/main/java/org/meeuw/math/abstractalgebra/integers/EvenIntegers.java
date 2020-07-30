package org.meeuw.math.abstractalgebra.integers;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class EvenIntegers  implements Rng<EvenIntegerElement>, Streamable<EvenIntegerElement>  {

    public static final EvenIntegers INSTANCE = new EvenIntegers();

    public static final EvenIntegerElement ZERO = EvenIntegerElement.of(0);

    private EvenIntegers() {
    }

    @Override
    public EvenIntegerElement zero() {
        return ZERO;
    }

    @Override
    public Stream<EvenIntegerElement> stream() {
        return Stream.iterate(zero(), i -> i.isPositive() ? i.negation() : i.negation().plus(EvenIntegerElement.of(2)));
    }
}
