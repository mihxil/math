package org.meeuw.math.abstractalgebra.integers;

import lombok.Getter;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.Randomizable;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InvalidStructureCreationException;

/**
 * Implementation of ℤ/nℤ
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Example(Ring.class)
public abstract class ModuloStructure<E extends ModuloElement<E, S>, S extends ModuloStructure<E, S>>
    extends AbstractAlgebraicStructure<E>
    implements Ring<E>, Streamable<E>, Randomizable<E> {

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

    @Override
    public E nextRandom(Random random) {
        return element(random.nextInt(divisor));
    }

    public Stream<String> multiplicationTable(String format) {
        return stream().flatMap(e1 ->
            stream().map(e2 -> String.format(format, e1, e2, e1.times(e2)))
        );
    }

    /**
     * Streams the entire multiplication table of the modulo structure
     */
    public Stream<String> multiplicationTable() {
        return multiplicationTable("%s x %s = %s");
    }


    abstract E element(int v);

    @Override
    public String toString() {
        return "ℤ/" + divisor + "ℤ";
    }

}
