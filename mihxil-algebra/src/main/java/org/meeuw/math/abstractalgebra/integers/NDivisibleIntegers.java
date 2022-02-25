package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.RandomConfiguration;
import org.meeuw.math.abstractalgebra.Rng;
import org.meeuw.math.exceptions.InvalidElementCreationException;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Example(Rng.class)
public class NDivisibleIntegers extends
    AbstractIntegers<NDivisibleInteger, NDivisibleInteger, NDivisibleIntegers>
    implements Rng<NDivisibleInteger> {

    private static final Map<Integer, NDivisibleIntegers> INSTANCES = new ConcurrentHashMap<>();

    @Getter
    final int divisor;

    private final BigInteger bigDivisor;

    public static NDivisibleIntegers of(int divisor) {
        return INSTANCES.computeIfAbsent(divisor, NDivisibleIntegers::new);
    }

    private NDivisibleIntegers(int divisor) {
        super();
        this.divisor = divisor;
        this.bigDivisor = BigInteger.valueOf(divisor);
    }

    @Override
    public NDivisibleInteger zero() {
        return of(BigInteger.ZERO);
    }

    @Override
    public Stream<NDivisibleInteger> stream() {
        return Stream.iterate(zero(),
            i -> i.signum() > 0 ?
                i.negation() :
                i.negation().plus(
                    of(bigDivisor))
        );
    }


    @Override
    public NDivisibleInteger nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextLong(random) * divisor));
    }

    @Override
    NDivisibleInteger of(BigInteger value) {
        return new NDivisibleInteger(this, value);
    }

    @Override
    public NDivisibleInteger newElement(BigInteger value) throws InvalidElementCreationException {
        if (! value.remainder(bigDivisor).equals(BigInteger.ZERO)) {
            throw new InvalidElementCreationException("The argument must be dividable by " + divisor + " (" + value + " isn't)");
        }
        return of(value);
    }


    @Override
    public String toString() {
        return divisor + "ℤ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NDivisibleIntegers that = (NDivisibleIntegers) o;

        return divisor == that.divisor;
    }

    @Override
    public int hashCode() {
        return divisor;
    }
}
