package org.meeuw.math.abstractalgebra.integers;

import org.meeuw.math.abstractalgebra.AdditiveMonoid;
import org.meeuw.math.abstractalgebra.MultiplicativeMonoid;
import org.meeuw.math.abstractalgebra.Operator;
import org.meeuw.math.abstractalgebra.Streamable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

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
}
