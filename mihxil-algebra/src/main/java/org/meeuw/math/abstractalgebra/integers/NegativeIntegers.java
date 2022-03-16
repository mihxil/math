package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import javax.validation.constraints.Max;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.AdditiveAbelianSemiGroup;
import org.meeuw.math.abstractalgebra.RandomConfiguration;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.operators.AlgebraicBinaryOperator;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.integers.NegativeInteger.MINUS_ONE;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.ADDITION;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.OPERATION;

/**
 * The 'Semigroup'  of  negative numbers
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
@Example(AdditiveAbelianSemiGroup.class)
public class NegativeIntegers
    extends AbstractIntegers<NegativeInteger, PositiveInteger, NegativeIntegers>
    implements
    AdditiveAbelianSemiGroup<NegativeInteger> {

    private static final NavigableSet<AlgebraicBinaryOperator> OPERATORS = navigableSet(OPERATION, ADDITION);

    public static final NegativeIntegers INSTANCE = new NegativeIntegers();

    protected NegativeIntegers() {
        super(NegativeInteger.class);
    }


    @Override
    NegativeInteger of(BigInteger value) {
        return new NegativeInteger(value);
    }

    @Override
    public NegativeInteger newElement(@Max(-1) BigInteger value) throws InvalidElementCreationException {
        if (value.compareTo(BigInteger.ZERO) >= 0) {
            throw new InvalidElementCreationException("Negative numbers cannot be 0 or positive");
        }
        return of(value);
    }

    @Override
    public NavigableSet<AlgebraicBinaryOperator> getSupportedOperators() {
        return OPERATORS;
    }

    @Override
    public Stream<NegativeInteger> stream() {
        return  Stream.iterate(MINUS_ONE, i -> i.plus(MINUS_ONE));
    }

    @Override
    public NegativeInteger nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextNonNegativeLong(random) * -1 - 1));
    }


    @Override
    public String toString() {
        return "ℕ" + TextUtils.superscript("-");
    }
}
