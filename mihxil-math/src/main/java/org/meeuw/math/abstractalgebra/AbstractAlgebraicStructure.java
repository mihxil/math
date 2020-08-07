package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class AbstractAlgebraicStructure<F extends AlgebraicElement<F>> implements AlgebraicStructure<F> {

    private final Class<F> elementClass;

    protected AbstractAlgebraicStructure(Class<F> elementClass) {
        this.elementClass = elementClass;
    }

   @Override
    public Class<F> elementClass() {
        return elementClass;
    }
}
