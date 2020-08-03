package org.meeuw.math.abstractalgebra.integers;

import java.util.*;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NaturalNumbers implements MultiplicativeMonoid<NaturalNumber>, AdditiveMonoid<NaturalNumber>, Streamable<NaturalNumber> {

    Set<Operator> operators = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Operator.MULTIPLICATION, Operator.ADDITION)));



    public static NaturalNumbers INSTANCE = new NaturalNumbers();

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
        return operators;
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
