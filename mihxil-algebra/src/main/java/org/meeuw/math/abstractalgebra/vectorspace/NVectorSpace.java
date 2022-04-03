package org.meeuw.math.abstractalgebra.vectorspace;

import lombok.EqualsAndHashCode;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumber;
import org.meeuw.math.abstractalgebra.rationalnumbers.RationalNumbers;
import org.meeuw.math.exceptions.NotStreamable;
import org.meeuw.math.streams.StreamUtils;
import org.meeuw.math.text.TextUtils;

/**
 * the space of n-dimensional vector
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class NVectorSpace<E extends FieldElement<E>>
    implements
    VectorSpace<E, NVector<E>>, Streamable<NVector<E>> {

    private static final Map<Key, NVectorSpace<?>> INSTANCES = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <E extends FieldElement<E>> NVectorSpace<E> of(int dimension, Field<E> field) {
        Key key = new Key(field.getElementClass(), dimension);
        return (NVectorSpace<E>) INSTANCES.computeIfAbsent(key, (k)  -> new NVectorSpace<>(dimension, field));
    }

    @Example(value = VectorSpace.class, prefix = "NVectorSpace ")
    public static NVectorSpace<RationalNumber> Q2 = NVectorSpace.of(2, RationalNumbers.INSTANCE);

    private final Field<E> field;
    private final NVector<E> zero;
    private final NVector<E> one;
    private final int dimension;

    @SuppressWarnings("unchecked")
    public NVectorSpace(int dimension, Field<E> field) {
        this.field = field;
        this.dimension = dimension;
        E[] zeroElement = (E[]) Array.newInstance(field.getElementClass(), dimension);
        for (int i = 0; i < dimension; i ++) {
            zeroElement[i] = field.zero();
        }
        this.zero = new NVector<>(zeroElement);
        E[] oneElement = (E[]) Array.newInstance(field.getElementClass(), dimension);
        for (int i = 0; i < dimension; i ++) {
            oneElement[i] = field.one();
        }
        this.one = new NVector<>(oneElement);
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public NVector<E> zero() {
        return zero;
    }

    @Override
    public Field<E> getField() {
        return field;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        int result = field.hashCode();
        result = 31 * result + dimension;
        return result;
    }

    @Override
    public String toString() {
        return getField() + TextUtils.superscript(3);
    }

    @Override
    public NVector<E> one() {
        return one;
    }

    @Override
    public Cardinality getCardinality() {
        return field.getCardinality();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<NVector<E>> getElementClass() {
        return (Class<NVector<E>>) one().getClass();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Stream<NVector<E>> stream() {
        if (getCardinality().compareTo(Cardinality.ALEPH_0) > 0) {
            throw new NotStreamable();
        } else {
            Streamable<E> streamable = (Streamable<E>) field;
            return StreamUtils.cartesianStream(streamable::stream, dimension)
                .map(NVector::new);
        }
    }

    @EqualsAndHashCode
    private static class Key {
        private final Class<?> elementClass;
        private final int dimension;

        private Key(Class<?> elementClass, int dimension) {
            this.elementClass = elementClass;
            this.dimension = dimension;
        }
    }
}
