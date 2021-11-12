package org.meeuw.math.abstractalgebra.integers;

import java.util.Set;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.abstractalgebra.integers.IntegerElement.ONE;
import static org.meeuw.math.abstractalgebra.integers.IntegerElement.ZERO;

/**
 * The Ring of integers, commonly referred to as ℤ.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Integers extends AbstractAlgebraicStructure<IntegerElement>
    implements Ring<IntegerElement>, Streamable<IntegerElement>, MultiplicativeMonoid<IntegerElement> {

    public static final Integers INSTANCE = new Integers();


    private Integers() {
        super(IntegerElement.class);
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
    public Set<ComparisonOperator> getSupportedComparisonOperators() {
        return ComparisonOperator.ALL;
    }

    @Override
    public Stream<IntegerElement> stream() {
        return Stream.iterate(zero(), i -> i.isPositive() ? i.negation() : i.negation().plus(one()));
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public String toString() {
        return "ℤ";
    }
}
