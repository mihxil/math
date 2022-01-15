package org.meeuw.math.abstractalgebra.integers;

import java.util.NavigableSet;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.ADDITION;
import static org.meeuw.math.abstractalgebra.integers.NegativeNumber.MINUS_ONE;

/**
 * The 'Semigroup'  of  negative numbers
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Example(AdditiveAbelianSemiGroup.class)
public class NegativeNumbers extends AbstractAlgebraicStructure<NegativeNumber>
    implements
    AdditiveAbelianSemiGroup<NegativeNumber>,
    Streamable<NegativeNumber> {

    private static final NavigableSet<Operator> OPERATORS = navigableSet(ADDITION);

    public static final NegativeNumbers INSTANCE = new NegativeNumbers();

    protected NegativeNumbers() {
        super(NegativeNumber.class);
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
    public Stream<NegativeNumber> stream() {
        return  Stream.iterate(MINUS_ONE, i -> i.plus(MINUS_ONE));
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public String toString() {
        return "ℕ" + TextUtils.superscript("-");
    }
}
