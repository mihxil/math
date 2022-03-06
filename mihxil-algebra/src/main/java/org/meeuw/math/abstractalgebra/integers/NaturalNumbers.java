package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import javax.validation.constraints.Min;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.*;
import static org.meeuw.math.abstractalgebra.UnaryOperator.*;

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

    private static final NavigableSet<Operator> OPERATORS = navigableSet(OPERATION, MULTIPLICATION, ADDITION);

    private static final NavigableSet<UnaryOperator> UNARY_OPERATORS = navigableSet(IDENTIFY, ABS, SQR);


    public static final NaturalNumbers INSTANCE = new NaturalNumbers();

    protected NaturalNumbers() {
        super(NaturalNumber.class);
    }


    @Override
    NaturalNumber of(BigInteger value) {
        return new NaturalNumber(value);
    }

    @Override
    public NaturalNumber newElement(@Min(0) BigInteger value) throws InvalidElementCreationException {
        if (value.compareTo(BigInteger.ZERO) < 0) {
            throw new InvalidElementCreationException("Natural numbers must be non-negative");
        }
        return of(value);
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
    public NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    public Stream<NaturalNumber> stream() {
        return Stream.iterate(zero(), i -> i.plus(one()));
    }

    @Override
    public NaturalNumber nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextLong(random)));
    }


    @Override
    public String toString() {
        return "ℕ";
    }
}
