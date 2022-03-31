package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import javax.validation.constraints.Min;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.operators.*;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.Utils.navigableSet;

/**
 * The 'Monoid' (multiplicative and additive) of positive integers
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public class PositiveIntegers extends AbstractIntegers<PositiveInteger, PositiveInteger, PositiveIntegers>
    implements
    MultiplicativeMonoid<PositiveInteger>,
    AdditiveAbelianSemiGroup<PositiveInteger> {


    private static final NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(MultiplicativeMonoid.OPERATORS, AdditiveAbelianSemiGroup.OPERATORS);

    private static final NavigableSet<AlgebraicUnaryOperator> UNARY_OPERATORS = navigableSet(MultiplicativeMonoid.UNARY_OPERATORS, AdditiveAbelianSemiGroup.UNARY_OPERATORS, navigableSet(Factoriable.FACT));

    private static final NavigableSet<GenericFunction> FUNCTIONS = navigableSet(MultiplicativeMonoid.FUNCTIONS, AdditiveAbelianSemiGroup.FUNCTIONS, navigableSet(BasicFunction.ABS));


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
    public NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    public NavigableSet<AlgebraicUnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    public NavigableSet<GenericFunction> getSupportedFunctions() {
        return FUNCTIONS;
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
