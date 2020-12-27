package org.meeuw.math.abstractalgebra.vectorspace;

import java.util.*;
import java.util.stream.Collectors;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class Vector<E extends ScalarFieldElement<E>> implements VectorInterface<E, Vector<E>>, Iterable<E> {

    private final E[] values;

    Vector(E[] values) {
        this.values = values;
    }

    @SafeVarargs
    public static <E extends ScalarFieldElement<E>> Vector<E> of(E... values) {
        return new Vector<>(values);
    }

    @Override
    public Vector<E> times(E multiplier) {
        E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].times(multiplier);
        }
        return new Vector<>(copy);
    }

    @Override
    public Vector<E> dividedBy(E divisor) {
        E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].dividedBy(divisor);
        }
        return new Vector<>(copy);
    }

    @Override
    public Vector<E> plus(Vector<E> summand) {
        E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].plus(summand.values[i]);
        }
        return new Vector<>(copy);
    }

    @Override
    public Vector<E> inverse() {
        E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].negation();
        }
        return new Vector<>(copy);
    }

    @Override
    public E get(int i) {
        return values[i];
    }

    @Override
    public VectorSpace<E> getSpace() {
        return VectorSpace.of(values.length, values[0].getStructure());
    }

    @Override
    public String toString() {
        return "(" + Arrays.stream(values).map(Object::toString).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector<?> vector = (Vector<?>) o;
        return Arrays.equals(values, vector.values);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public E next() {
                return values[i++];
            }
        };
    }

    @Override
    public Spliterator<E> spliterator() {
        return Arrays.spliterator(values);
    }
}
