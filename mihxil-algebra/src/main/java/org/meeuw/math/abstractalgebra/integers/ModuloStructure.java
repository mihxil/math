package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidStructureCreationException;

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

    final E one;
    final E zero;

    ModuloStructure(Class<E> eClass, int divisor) {
        super(eClass);
        if (divisor <= 0) {
            throw new InvalidStructureCreationException("Divisor of modulo structure must be > 0");
        }
        this.divisor = divisor;
        this.cardinality = new Cardinality(divisor);
        one = element(1);
        zero = element(0);
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
