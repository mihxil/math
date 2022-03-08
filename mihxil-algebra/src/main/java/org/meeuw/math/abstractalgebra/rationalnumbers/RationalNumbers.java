package org.meeuw.math.abstractalgebra.rationalnumbers;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

import org.meeuw.math.*;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.streams.StreamUtils;

import static java.math.BigInteger.ONE;

/**
 * Implementation of the field of Rational Numbers, commonly referred to as ℚ
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(ScalarField.class)
public class RationalNumbers extends AbstractAlgebraicStructure<RationalNumber>
    implements ScalarField<RationalNumber>, Streamable<RationalNumber>, Randomizable<RationalNumber> {

    public static final RationalNumbers INSTANCE = new RationalNumbers();

    protected RationalNumbers() {
        super(RationalNumber.class);
    }

    @Override
    public RationalNumber zero() {
        return RationalNumber.ZERO;
    }

    @Override
    public RationalNumber one() {
        return RationalNumber.ONE;
    }

    @Override
    public NavigableSet<ComparisonOperator> getSupportedComparisonOperators() {
        return ComparisonOperator.ALL;
    }

    @Override
    public Stream<RationalNumber> stream() {
        return Stream.concat(
            Stream.of(zero()),
            StreamUtils.diagonalStream(
                (s) -> StreamUtils.reverseBigIntegerStream(BigInteger.valueOf(s), false),
                () -> StreamUtils.bigIntegerStream(ONE, false),
                (a, b) -> a.abs().gcd(b).equals(ONE) ? new RationalNumber(a.negate(), b) : null)
                .filter(Objects::nonNull)
                .flatMap(s -> Stream.of(s, s.negation()))
        );
    }

    @Override
    public RationalNumber nextRandom(Random random) {
        long numerator = random.nextLong();
        long denumator = 0L;
        while (denumator == 0L) {
            denumator = random.nextLong();
        }
        return RationalNumber.of(numerator, denumator);
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RationalNumbers;
    }

    @Override
    public String toString() {
        return "ℚ";
    }
}
