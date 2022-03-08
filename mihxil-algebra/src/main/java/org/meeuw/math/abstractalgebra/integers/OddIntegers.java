package org.meeuw.math.abstractalgebra.integers;

import java.math.BigInteger;
import java.util.NavigableSet;
import java.util.Random;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidElementCreationException;
import org.meeuw.math.text.TextUtils;

import static org.meeuw.math.abstractalgebra.integers.AbstractIntegerElement.BigTWO;
import static org.meeuw.math.abstractalgebra.integers.OddInteger.ONE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(MultiplicativeMonoid.class)
@Example(MultiplicativeAbelianSemiGroup.class)
public class OddIntegers extends AbstractIntegers<OddInteger, OddInteger, OddIntegers>
    implements
    MultiplicativeMonoid<OddInteger>,
    MultiplicativeAbelianSemiGroup<OddInteger>{

    public static final OddIntegers INSTANCE = new OddIntegers();

    static NavigableSet<UnaryOperator> UNARY_OPERATORS = Utils.navigableSet(MultiplicativeMonoid.UNARY_OPERATORS, UnaryOperator.NEGATION, UnaryOperator.ABS);


    private OddIntegers() {
        super(OddInteger.class);
    }

    @Override
    public NavigableSet<UnaryOperator> getSupportedUnaryOperators() {
        return UNARY_OPERATORS;
    }

    @Override
    public OddInteger nextRandom(Random random) {
        return of(BigInteger.valueOf(RandomConfiguration.nextLong(random) * 2 + 1));
    }

    @Override
    OddInteger of(BigInteger value) {
        return new OddInteger(value);
    }

    @Override
    public OddInteger newElement(BigInteger value) throws InvalidElementCreationException {
        if (value.remainder(BigTWO).equals(BigInteger.ZERO)) {
            throw new InvalidElementCreationException("The argument must be odd (" + value + " isn't)");
        }
        return of(value);
    }

    @Override
    public boolean multiplicationIsCommutative() {
        return true;
    }

    @Override
    public OddInteger one() {
        return ONE;
    }

    @Override
    public Stream<OddInteger> stream() {
        return Stream.iterate(
            one(),
            i -> i.signum() > 0 ?
                i.negation() :
                i.negation().plus(EvenInteger.TWO)
        );
    }

    @Override
    public String toString() {
        return "ℕ" + TextUtils.subscript("o");
    }

}
