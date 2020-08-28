package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;

/**
 * Implementation of ℤ/nℤ
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class ModuloStructure<E extends ModuloElement<E, S>, S extends ModuloStructure<E, S>>  extends AbstractAlgebraicStructure<E>
    implements Ring<E>, Streamable<E> {

    @Getter
    final int divisor;
    @Getter
    final Cardinality cardinality;


    final E one = element(1);
    final E zero = element(0);

    ModuloStructure(Class<E> eClass, int divisor) {
        super(eClass);
        this.divisor = divisor;
        this.cardinality = new Cardinality(divisor);
    }

    @Override
    public E one() {
        return one;
    }

    @Override
    public E zero() {
        return zero;
    }

    @Override
    public Stream<E> stream() {
        return IntStream.range(0, divisor).mapToObj(this::element);
    }

    abstract E element(int v);
}
