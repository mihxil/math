package org.meeuw.math.abstractalgebra.integers;

import java.util.NavigableSet;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.abstractalgebra.integers.EvenInteger.ZERO;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(Rng.class)
public class EvenIntegers extends AbstractAlgebraicStructure<EvenInteger>
    implements Rng<EvenInteger>,
    Streamable<EvenInteger>  {

    public static final EvenIntegers INSTANCE = new EvenIntegers();

    private EvenIntegers() {
        super(EvenInteger.class);
    }

    @Override
    public EvenInteger zero() {
        return ZERO;
    }

    @Override
    public NavigableSet<ComparisonOperator> getSupportedComparisonOperators() {
        return ComparisonOperator.ALL;
    }

    @Override
    public Stream<EvenInteger> stream() {
        return Stream.iterate(zero(), i -> i.signum() > 0 ? i.negation() : i.negation().plus(EvenInteger.of(2)));
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }
}
