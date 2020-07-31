package org.meeuw.math.abstractalgebra.rationalnumbers;

import org.meeuw.math.abstractalgebra.NumberField;
import org.meeuw.math.abstractalgebra.Streamable;

import java.math.BigInteger;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;

/**
 * Implementation of the field of Rational Numbers, commonly referred to as ℚ
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class RationalNumbers implements NumberField<RationalNumber>, Streamable<RationalNumber> {

    public static final RationalNumbers INSTANCE = new RationalNumbers();

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
        final long size;
        final long numerator;
        final long denominator;

        private State() {
            this(0, 0, 1);
        }
        private State(long size, long numerator, long denominator) {
            this.size = size;
            this.numerator = numerator;
            this.denominator = denominator;
        }

        RationalNumber rationalNumber() {
            return RationalNumber.of(numerator, denominator);
        }
        public State _next() {
            long an = Math.abs(numerator);
            if (an <= 1) {
                return new State(size + 1, size + 1, 1);
            }
            return new State(size, an - 1, denominator + 1);
        }
        public boolean unique() {
            return BigInteger.valueOf(numerator).gcd(BigInteger.valueOf(denominator)).equals(ONE);
        }
        public State next() {
            if (numerator > 0) {
                return new State(size, numerator * -1, denominator);
            }

            State proposal = _next();
            while (! proposal.unique()) {
                proposal = proposal._next();
            }
            return proposal;
        }
    }

}
