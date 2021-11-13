package org.meeuw.math.abstractalgebra.integers;

import java.util.NavigableSet;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.ADDITION;
import static org.meeuw.math.abstractalgebra.Operator.MULTIPLICATION;

/**
 * The 'Monoid' (multiplicative and additive) of natural numbers.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NaturalNumbers extends AbstractAlgebraicStructure<NaturalNumber>
    implements
    MultiplicativeMonoid<NaturalNumber>,
    AdditiveMonoid<NaturalNumber>,
    Streamable<NaturalNumber> {

    private static final NavigableSet<Operator> OPERATORS = navigableSet(MULTIPLICATION, ADDITION);

    public static final NaturalNumbers INSTANCE = new NaturalNumbers();

    protected NaturalNumbers() {
        super(NaturalNumber.class);
    }

    @Override
    public NaturalNumber zero() {
        return NaturalNumber.ZERO;
    }

    @Override
    public NaturalNumber one() {
        return NaturalNumber.ONE;
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
    public Stream<NaturalNumber> stream() {
        return  Stream.iterate(zero(), i -> i.plus(one()));
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }
}
