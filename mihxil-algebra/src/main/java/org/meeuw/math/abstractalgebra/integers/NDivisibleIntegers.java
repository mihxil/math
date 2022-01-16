package org.meeuw.math.abstractalgebra.integers;

import java.util.Map;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Example(Rng.class)
public class NDivisibleIntegers extends
    AbstractAlgebraicStructure<NDivisibleInteger>
    implements Rng<NDivisibleInteger>,
    Streamable<NDivisibleInteger>  {

    private static final Map<Integer, NDivisibleIntegers> INSTANCES = new ConcurrentHashMap<>();

    private final int divisor;

    public static NDivisibleIntegers of(int divisor) {
        return INSTANCES.computeIfAbsent(divisor, NDivisibleIntegers::new);
    }

    private NDivisibleIntegers(int divisor) {
        super(NDivisibleInteger.class);
        this.divisor = divisor;
    }

    @Override
    public NDivisibleInteger zero() {
        return new NDivisibleInteger(this, 0);
    }

    @Override
    public NavigableSet<ComparisonOperator> getSupportedComparisonOperators() {
        return ComparisonOperator.ALL;
    }

    @Override
    public Stream<NDivisibleInteger> stream() {
        return Stream.iterate(zero(), i -> i.signum() > 0 ? i.negation() : i.negation().plus(new NDivisibleInteger(this, divisor)));
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }
}
