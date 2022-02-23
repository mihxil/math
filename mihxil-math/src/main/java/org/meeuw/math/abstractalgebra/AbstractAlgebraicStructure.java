package org.meeuw.math.abstractalgebra;

import lombok.Getter;

import java.lang.reflect.*;

/**
 * This abstract base class for {@link AlgebraicStructure}s takes care of the 'elementClass' property.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AbstractAlgebraicStructure<E extends AlgebraicElement<E>>
    implements AlgebraicStructure<E> {

    @Getter
    private final Class<E> elementClass;

    protected AbstractAlgebraicStructure(Class<E> elementClass) {
        this.elementClass = elementClass;
    }

    protected AbstractAlgebraicStructure() {
        this.elementClass = toClass(((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0]);
    }

    @SuppressWarnings("unchecked")
    private Class<E> toClass(Type type) {
        if (type instanceof Class) {
            return (Class<E>) type;
        } else if (type instanceof TypeVariable<?>) {
            return toClass(((TypeVariable) type).getBounds()[0]);
        } else {
            return toClass(((ParameterizedType) type).getRawType());
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
