package org.meeuw.math.abstractalgebra.integers;

import java.util.NavigableSet;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.abstractalgebra.integers.OddInteger.ONE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(MultiplicativeMonoid.class)
@Example(MultiplicativeAbelianSemiGroup.class)
public class OddIntegers extends AbstractAlgebraicStructure<OddInteger>
    implements
    MultiplicativeMonoid<OddInteger>,
    MultiplicativeAbelianSemiGroup<OddInteger>,
    Streamable<OddInteger>  {

    public static final OddIntegers INSTANCE = new OddIntegers();

    private OddIntegers() {
        super(OddInteger.class);
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    public OddInteger one() {
        return ONE;
    }

    @Override
    public NavigableSet<ComparisonOperator> getSupportedComparisonOperators() {
        return ComparisonOperator.ALL;
    }

    @Override
    public Stream<OddInteger> stream() {
        return Stream.iterate(one(), i -> i.signum() > 0 ? i.negation() : i.negation().plus(EvenInteger.of(2)));
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }
}
