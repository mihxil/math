package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.stream.Stream;

import org.meeuw.math.Example;
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
@Example(AdditiveMonoid.class)
public class NaturalNumbers extends AbstractIntegers<NaturalNumber, NaturalNumber, NaturalNumbers>
    implements
    MultiplicativeMonoid<NaturalNumber>,
    AdditiveMonoid<NaturalNumber>,
    AdditiveAbelianSemiGroup<NaturalNumber> {

    private static final NavigableSet<Operator> OPERATORS = navigableSet(MULTIPLICATION, ADDITION);

    public static final NaturalNumbers INSTANCE = new NaturalNumbers();

    protected NaturalNumbers() {
        super(NaturalNumber.class);
    }

    @Override
    NaturalNumber of(BigInteger value) {
        return new NaturalNumber(value);
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
    public Stream<NaturalNumber> stream() {
        return Stream.iterate(zero(), i -> i.plus(one()));
    }

    @Override
    public String toString() {
        return "ℕ";
    }
}
