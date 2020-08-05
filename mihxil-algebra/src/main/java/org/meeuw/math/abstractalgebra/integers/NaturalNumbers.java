package org.meeuw.math.abstractalgebra.integers;

import java.util.*;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NaturalNumbers extends AbstractAlgebraicStructure<NaturalNumber> implements MultiplicativeMonoid<NaturalNumber>, AdditiveMonoid<NaturalNumber>, Streamable<NaturalNumber> {

    private static final Set<Operator> OPERATORS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.MULTIPLICATION, Operator.ADDITION)));

    public static final NaturalNumbers INSTANCE = new NaturalNumbers();

    protected NaturalNumbers() {
        super(NaturalNumber.class);
    }

    @Override
    public NaturalNumber zero() {
        return NaturalNumber.ZERO;
    }

    @Override
    public NaturalNumber one() {
        return NaturalNumber.ONE;
    }

    @Override
    public Set<Operator> supportedOperators() {
        return OPERATORS;
    }
    @Override
    public Stream<NaturalNumber> stream() {
        return  Stream.iterate(zero(), i -> i.plus(one()));
    }

    @Override
    public Cardinality cardinality() {
        return Cardinality.ALEPH_0;
    }
}
