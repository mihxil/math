package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.Utils.navigableSet;
import static org.meeuw.math.abstractalgebra.Operator.*;
import static org.meeuw.math.abstractalgebra.integers.IntegerElement.ONE;
import static org.meeuw.math.abstractalgebra.integers.IntegerElement.ZERO;

/**
 * The Ring of integers, commonly referred to as ℤ.
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Integers extends AbstractIntegers<IntegerElement, IntegerElement, Integers>
    implements Ring<IntegerElement>,
    MultiplicativeMonoid<IntegerElement>,
    Group<IntegerElement> {

    @Override
    public NavigableSet<Operator> getSupportedOperators() {
        return navigableSet(OPERATION, ADDITION, SUBTRACTION, MULTIPLICATION);
    }

    @Override
    public  NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return navigableSet(Ring.UNARY_OPERATORS, MultiplicativeMonoid.UNARY_OPERATORS, Group.UNARY_OPERATORS, navigableSet(UnaryOperator.ABS));
    }


    @Example(Ring.class)
    @Example(Group.class)
    public static final Integers INSTANCE = new Integers();


    private Integers() {
        super(IntegerElement.class);
    }

    @Override
    public IntegerElement unity() {
        return zero();
    }



    @Override
    IntegerElement of(BigInteger value) {
        return new IntegerElement(value);
    }

    @Override
    public IntegerElement newElement(BigInteger value) {
        return of(value);
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
    public Stream<IntegerElement> stream() {
        return Stream.iterate(zero(), i -> i.isPositive() ? i.negation() : i.negation().plus(one()));
    }

    @Override
    public IntegerElement nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextLong(random)));
    }

    @Override
    public String toString() {
        return "ℤ";
    }


}
