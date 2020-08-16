package org.meeuw.math.abstractalgebra.rationalnumbers;

import java.math.BigInteger;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Implementation of the field of Rational Numbers, commonly referred to as ℚ
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RationalNumbers extends AbstractAlgebraicStructure<RationalNumber> implements NumberField<RationalNumber>, Streamable<RationalNumber> {

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
    public Stream<RationalNumber> stream() {
        return Stream.iterate(new State(),
                State::next).map(State::rationalNumber);
    }



    /**
     * Helper class for {@link #stream()}. Contains the logic to find the 'next' rational number.
     *
     * They are found by tracing diagonals in the positive numerator/denominator plain (alternating also producing negative values).
     *
     * And thus obtained fractions that can be simplified are skipped.
     */
    private static class State  {
        final BigInteger size;
        final BigInteger numerator;
        final BigInteger denominator;

        private State() {
            this(ZERO, ZERO, ONE);
        }
        private State(BigInteger size, BigInteger numerator, BigInteger denominator) {
            this.size = size;
            this.numerator = numerator;
            this.denominator = denominator;
        }

        RationalNumber rationalNumber() {
            return new RationalNumber(numerator, denominator);
        }
        public State _next() {
            BigInteger an = numerator.abs();
            if (an.compareTo(ONE) < 0) {
                return new State(size.add(ONE), size.add(ONE), ONE);
            }
            return new State(size, an.add(ONE.negate()), denominator.add(ONE));
        }
        public boolean unique() {
            return numerator.gcd(denominator).equals(ONE);
        }
        public State next() {
            if (numerator.compareTo(ZERO) > 0) {
                return new State(size, numerator.negate(), denominator);
            }

            State proposal = _next();
            while (! proposal.unique()) {
                proposal = proposal._next();
            }
            return proposal;
        }
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
}
