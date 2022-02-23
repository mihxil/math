package org.meeuw.math.abstractalgebra.gl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.meeuw.math.Example;
import org.meeuw.math.abstractalgebra.*;

/**
 * A linear group on any {@link org.meeuw.math.abstractalgebra.Field}
 */
@Example(Field.class)
public class GeneralLinearGroup<E extends FieldElement<E>> extends
    AbstractGeneralLinearGroup<
        InvertibleMatrix<E>,
        GeneralLinearGroup<E>,
        E,
        Field<E>
        > {

    private static final Map<Key, GeneralLinearGroup<?>> INSTANCES = new ConcurrentHashMap<>();

    protected GeneralLinearGroup(@NonNull Field<E> elementStructure, int dimension) {
        super(elementStructure, dimension);
    }


    @SuppressWarnings("unchecked")
    public static <E extends FieldElement<E>> GeneralLinearGroup<E> of(final int length, final Field<E> elementStructure) {
        final Key key = new Key(length, elementStructure);
        return (GeneralLinearGroup<E>) INSTANCES.computeIfAbsent(key, k -> new GeneralLinearGroup<E>(
            elementStructure,
            k.dimension
        ));
    }

    @Override
    InvertibleMatrix<E> of(E[][] elements) {
        return new InvertibleMatrix<>(this, elements);
    }

    @SafeVarargs
    @Override
    public final InvertibleMatrix<E> newMatrix(E... matrix) {
        return InvertibleMatrix.of(this, matrix);
    }
}
