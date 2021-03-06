package org.meeuw.math.abstractalgebra.integers;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.abstractalgebra.integers.EvenIntegerElement.ZERO;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class EvenIntegers extends AbstractAlgebraicStructure<EvenIntegerElement>
    implements Rng<EvenIntegerElement>, Streamable<EvenIntegerElement>  {

    public static final EvenIntegers INSTANCE = new EvenIntegers();

    private EvenIntegers() {
        super(EvenIntegerElement.class);
    }

    @Override
    public EvenIntegerElement zero() {
        return ZERO;
    }

    @Override
    public Stream<EvenIntegerElement> stream() {
        return Stream.iterate(zero(), i -> i.signum() > 0 ? i.negation() : i.negation().plus(EvenIntegerElement.of(2)));
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }
}
