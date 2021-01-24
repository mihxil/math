package org.meeuw.math.abstractalgebra.complex;

import java.util.stream.Stream;

import org.meeuw.math.Streams;
import org.meeuw.math.abstractalgebra.Field;
import org.meeuw.math.abstractalgebra.Streamable;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;

/**
 * The {@link Field} of {@link GaussianRational}s.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class GaussianRationals extends AbstractComplexNumbers<GaussianRational, RationalNumber>
    implements Field<GaussianRational>, Streamable<GaussianRational> {

    public static final GaussianRationals INSTANCE = new GaussianRationals();

    GaussianRationals() {
        super(GaussianRational.class, RationalNumbers.INSTANCE);
    }

    @Override
    public Stream<GaussianRational> stream() {
        return Streams.diagonalStream(
            RationalNumbers.INSTANCE::reverseStream,
            RationalNumbers.INSTANCE::stream,
            this::of
        );
    }

    @Override
    GaussianRational of(RationalNumber real, RationalNumber imaginary) {
        return new GaussianRational(real, imaginary);
    }

    @Override
    public String toString() {
        return "\u1D410(i)";
    }

}
