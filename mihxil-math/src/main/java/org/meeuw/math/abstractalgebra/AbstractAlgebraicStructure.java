package org.meeuw.math.abstractalgebra;

import lombok.Getter;

/**
 * This abstract base class for {@link AlgebraicStructure}s takes care of the 'elementClass' property.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AbstractAlgebraicStructure<F extends AlgebraicElement<F>> implements AlgebraicStructure<F> {

    @Getter
    private final Class<F> elementClass;

    protected AbstractAlgebraicStructure(Class<F> elementClass) {
        this.elementClass = elementClass;
    }
}
