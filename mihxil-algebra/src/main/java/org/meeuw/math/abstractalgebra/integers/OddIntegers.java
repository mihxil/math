package org.meeuw.math.abstractalgebra.integers;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

import static org.meeuw.math.abstractalgebra.integers.OddIntegerElement.ONE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class OddIntegers extends AbstractAlgebraicStructure<OddIntegerElement>
    implements MultiplicativeMonoid<OddIntegerElement>, Streamable<OddIntegerElement>  {

    public static final OddIntegers INSTANCE = new OddIntegers();

    private OddIntegers() {
        super(OddIntegerElement.class);
    }

    @Override
    public OddIntegerElement one() {
        return ONE;
    }


    @Override
    public Stream<OddIntegerElement> stream() {
        return Stream.iterate(one(), i -> i.signum() > 0 ? i.negation() : i.negation().plus(2));
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.ALEPH_0;
    }
}
