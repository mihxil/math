package org.meeuw.math.abstractalgebra.vectorspace;

import java.util.*;
import java.util.stream.Collectors;

import org.meeuw.math.abstractalgebra.Vector;
import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class VectorImpl<E extends FieldElement<E>> implements Vector<E, VectorImpl<E>>, Iterable<E> {

    private final E[] values;

    VectorImpl(E[] values) {
        this.values = values;
    }

    @SafeVarargs
    public static <E extends FieldElement<E>> VectorImpl<E> of(E... values) {
        return new VectorImpl<>(values);
    }

    @Override
    public VectorImpl<E> times(E multiplier) {
        E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].times(multiplier);
        }
        return new VectorImpl<>(copy);
    }

    @Override
    public VectorImpl<E> plus(VectorImpl<E> summand) {
        E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].plus(summand.values[i]);
        }
        return new VectorImpl<>(copy);
    }

    @Override
    public VectorImpl<E> inverse() {
        E[] copy = Arrays.copyOf(values, values.length);
        for (int i = 0; i < copy.length; i ++) {
            copy[i] =  copy[i].negation();
        }
        return new VectorImpl<>(copy);
    }

    @Override
    public E get(int i) {
        return values[i];
    }

    @Override
    public VectorSpace<E, VectorImpl<E>> getSpace() {
        return VectorSpaceImpl.of(values.length, values[0].getStructure());
    }

    @Override
    public String toString() {
        return "(" + Arrays.stream(values).map(Object::toString).collect(Collectors.joining(", ")) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VectorImpl<?> vector = (VectorImpl<?>) o;
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
