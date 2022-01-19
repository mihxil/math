package org.meeuw.math.abstractalgebra.integers;

import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.Rng;

import static org.meeuw.math.abstractalgebra.integers.EvenInteger.ZERO;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(Rng.class)
public class EvenIntegers extends AbstractIntegers<EvenInteger>
    implements Rng<EvenInteger> {

    public static final EvenIntegers INSTANCE = new EvenIntegers();

    private EvenIntegers() {
        super(EvenInteger.class);
    }

    @Override
    public EvenInteger zero() {
        return ZERO;
    }

    @Override
    public Stream<EvenInteger> stream() {
        return Stream.iterate(zero(), i -> i.signum() > 0 ? i.negation() : i.negation().plus(EvenInteger.of(2)));
    }

    @Override
    public String toString() {
        return "2ℤ";
    }
}
