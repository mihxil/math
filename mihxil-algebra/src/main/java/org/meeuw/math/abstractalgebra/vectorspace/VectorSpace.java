package org.meeuw.math.abstractalgebra.vectorspace;

import lombok.EqualsAndHashCode;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class VectorSpace<E extends FieldElement<E>> implements VectorSpaceInterface<E, Vector<E>> {

    private static final Map<Key, VectorSpace<?>> INSTANCES = new ConcurrentHashMap<>();

    private final Field<E> field;
    private final Vector<E> zero;
    private final int dimension;


    @SuppressWarnings("unchecked")
    public static <E extends FieldElement<E>> VectorSpace<E> of(int dimension, Field<E> field) {
        Key key = new Key(field.getElementClass(), dimension);
        return (VectorSpace<E>) INSTANCES.computeIfAbsent(key, (k)  -> new VectorSpace<>(dimension, field));
    }

    @SuppressWarnings("unchecked")
    private VectorSpace(int dimension, Field<E> field) {
        this.field = field;
        this.dimension = dimension;
        E[] zeroElement = (E[]) Array.newInstance(field.getElementClass(), dimension);
        for (int i = 0; i < dimension; i ++) {
            zeroElement[i] = field.zero();
        }
        this.zero = new Vector<E>(zeroElement);
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public Vector<E> zero() {
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
        return "VectorSpace of " + field + "[" + dimension + "]";
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
