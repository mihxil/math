package org.meeuw.math.abstractalgebra.integers;

import java.util.NavigableSet;
import java.util.stream.Stream;

import org.meeuw.math.Example;
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
@Example(AdditiveAbelianSemiGroup.class)
public class PositiveNumbers extends AbstractAlgebraicStructure<PositiveNumber>
    implements
    MultiplicativeMonoid<PositiveNumber>,
    AdditiveAbelianSemiGroup<PositiveNumber>,
    Streamable<PositiveNumber> {

    private static final NavigableSet<Operator> OPERATORS = navigableSet(MULTIPLICATION, ADDITION);

    public static final PositiveNumbers INSTANCE = new PositiveNumbers();

    protected PositiveNumbers() {
        super(PositiveNumber.class);
    }

    @Override
    public PositiveNumber one() {
        return PositiveNumber.ONE;
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
    public Stream<PositiveNumber> stream() {
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
