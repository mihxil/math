package org.meeuw.math.abstractalgebra.integers;

import java.util.NavigableSet;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.ADDITION;
import static org.meeuw.math.abstractalgebra.Operator.MULTIPLICATION;

/**
 * The 'Monoid' (multiplicative and additive) of natural numbers.
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class PositiveIntegers extends AbstractAlgebraicStructure<PositiveInteger>
    implements
    MultiplicativeMonoid<PositiveInteger>,
    AdditiveAbelianSemiGroup<PositiveInteger>,
    Streamable<PositiveInteger> {

    private static final NavigableSet<Operator> OPERATORS = navigableSet(MULTIPLICATION, ADDITION);

    public static final PositiveIntegers INSTANCE = new PositiveIntegers();

    protected PositiveIntegers() {
        super(PositiveInteger.class);
    }

    @Override
    public PositiveInteger one() {
        return PositiveInteger.ONE;
    }

    @Override
    public NavigableSet<Operator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    public NavigableSet<ComparisonOperator> getSupportedComparisonOperators() {
        return ComparisonOperator.ALL;
    }

    @Override
    public Stream<PositiveInteger> stream() {
        return  Stream.iterate(one(), i -> i.plus(one()));
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public String toString() {
        return "ℕ" + TextUtils.superscript("+");
    }
}
