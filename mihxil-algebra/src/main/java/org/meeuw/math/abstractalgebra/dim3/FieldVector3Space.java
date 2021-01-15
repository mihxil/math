package org.meeuw.math.abstractalgebra.dim3;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class FieldVector3Space<E extends ScalarFieldElement<E>>
    implements VectorSpaceInterface<E, FieldVector3<E>> {

    private static final Map<ScalarField<?>, FieldVector3Space<?>> INSTANCES = new ConcurrentHashMap<>();

    final ScalarField<E> scalarField;

    private FieldVector3Space(ScalarField<E> scalarField) {
        this.scalarField = scalarField;
    }

    public static <F extends ScalarFieldElement<F>> FieldVector3Space<F> of (ScalarField<F> field) {
        return (FieldVector3Space<F>) INSTANCES.computeIfAbsent(field, k -> new FieldVector3Space<>(field));
    }

    @Override
    public int getDimension() {
        return 3;
    }

    @Override
    public FieldVector3<E> zero() {
        return FieldVector3.of(scalarField.zero(), scalarField.zero(), scalarField.zero());
    }

    @Override
    public ScalarField<E> getField() {
        return scalarField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldVector3Space<?> that = (FieldVector3Space<?>) o;

        return scalarField.equals(that.scalarField);
    }

    @Override
    public int hashCode() {
        return scalarField.hashCode();
    }
}
