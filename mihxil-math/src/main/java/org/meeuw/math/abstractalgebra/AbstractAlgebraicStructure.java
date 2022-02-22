package org.meeuw.math.abstractalgebra;

import lombok.Getter;

import java.lang.reflect.ParameterizedType;

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

    @SuppressWarnings("unchecked")
    protected AbstractAlgebraicStructure() {
        this.elementClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
