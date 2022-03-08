package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class NDivisibleIntegers extends
    AbstractIntegers<NDivisibleInteger, NDivisibleInteger, NDivisibleIntegers>
    implements Rng<NDivisibleInteger> {

    private static final Map<Integer, NDivisibleIntegers> INSTANCES = new ConcurrentHashMap<>();


    public static NDivisibleIntegers of(int divisor) {
        return INSTANCES.computeIfAbsent(divisor, NDivisibleIntegers::new);
    }

    @Example(Rng.class)
    public static final NDivisibleIntegers _3Z = NDivisibleIntegers.of(3);

    static NavigableSet<UnaryOperator> UNARY_OPERATORS = Utils.navigableSet(Rng.UNARY_OPERATORS, UnaryOperator.ABS);

    @Getter
    final int divisor;

    private final BigInteger bigDivisor;


    private NDivisibleIntegers(int divisor) {
        super();
        this.divisor = divisor;
        this.bigDivisor = BigInteger.valueOf(divisor);
    }

    @Override
    public NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
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
