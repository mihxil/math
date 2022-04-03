package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.operators.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;
import static org.meeuw.math.abstractalgebra.integers.AbstractIntegerElement.BigTWO;
import static org.meeuw.math.abstractalgebra.integers.EvenInteger.ZERO;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(Rng.class)
public class EvenIntegers extends AbstractIntegers<EvenInteger, EvenInteger, EvenIntegers>
    implements Rng<EvenInteger> {

    public static final EvenIntegers INSTANCE = new EvenIntegers();

    static NavigableSet<GenericFunction> FUNCTIONS = CollectionUtils.navigableSet(Rng.FUNCTIONS, BasicFunction.ABS);

    private EvenIntegers() {
        super(EvenInteger.class);
    }

    @Override
    public NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
    }

    @Override
    public EvenInteger of(BigInteger value) {
        return new EvenInteger(value);
    }

    @Override
    public EvenInteger newElement(BigInteger value) throws InvalidElementCreationException {
        if (value.remainder(BigTWO).equals(ONE)) {
            throw new InvalidElementCreationException("The argument must be even (" + value + " isn't)");
        }
        return new EvenInteger(value);
    }

    @Override
    public EvenInteger zero() {
        return ZERO;
    }

    @Override
    public Stream<EvenInteger> stream() {
        return Stream.iterate(
            zero(),
            i -> i.signum() > 0 ?
                i.negation() :
                i.negation().plus(EvenInteger.TWO)
        );
    }

    @Override
    public EvenInteger nextRandom(Random random) {
        return of(valueOf(RandomConfiguration.nextLong(random) * 2));
    }

    @Override
    public String toString() {
        return "2ℤ";
    }
}
