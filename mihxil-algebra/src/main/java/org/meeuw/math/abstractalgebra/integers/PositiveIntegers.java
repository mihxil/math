package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
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
public class PositiveIntegers extends AbstractIntegers<PositiveInteger, PositiveInteger, PositiveIntegers>
    implements
    MultiplicativeMonoid<PositiveInteger>,
    AdditiveAbelianSemiGroup<PositiveInteger> {

    private static final NavigableSet<Operator> OPERATORS = navigableSet(MULTIPLICATION, ADDITION);

    public static final PositiveIntegers INSTANCE = new PositiveIntegers();

    protected PositiveIntegers() {
        super(PositiveInteger.class);
    }


    @Override
    PositiveInteger of(BigInteger value) {
        return new PositiveInteger(value);
    }

    @Override
    public PositiveInteger newElement(@Min(0) BigInteger value) throws InvalidElementCreationException {
        if (value.compareTo(BigInteger.ZERO) <= 0) {
            throw new InvalidElementCreationException("Positive numbers cannot be 0 or negative");
        }
        return of(value);
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
    public Stream<PositiveInteger> stream() {
        return  Stream.iterate(one(), i -> i.plus(one()));
    }

    @Override
    public PositiveInteger nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextNonNegativeLong(random) + 1));
    }

    @Override
    public String toString() {
        return "ℕ" + TextUtils.superscript("+");
    }
}
