package org.meeuw.math.abstractalgebra.gl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.abstractalgebra.*;

public class SpecialLinearGroup<E extends RingElement<E>> extends
    AbstractGeneralLinearGroup<
        SpecialLinearMatrix<E>,
        SpecialLinearGroup<E>,
        E,
        Ring<E>
        > {
    private static final Map<Key, SpecialLinearGroup<?>> INSTANCES = new ConcurrentHashMap<>();

    protected SpecialLinearGroup(@NonNull Ring<E> elementStructure, int dimension) {
        super(elementStructure, dimension);
    }

    @Override
    SpecialLinearMatrix<E> of(E[][] elements) {
        return new SpecialLinearMatrix<>(this, elements);
    }

    @SuppressWarnings("unchecked")
    public static <E extends RingElement<E>> SpecialLinearGroup<E> of(final int length, final Ring<E> elementStructure) {
        final Key key = new Key(length, elementStructure);
        return (SpecialLinearGroup<E>) INSTANCES.computeIfAbsent(key, k -> new SpecialLinearGroup<E>(
            elementStructure,
            k.dimension
        ));
    }
}

