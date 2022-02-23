package org.meeuw.math.abstractalgebra;

import lombok.Getter;

public abstract class AbstractAlgebraicElement<E extends AbstractAlgebraicElement<E, S>, S extends AlgebraicStructure<E>> implements AlgebraicElement<E> {

    @Getter
    protected final S structure;

    protected AbstractAlgebraicElement(S structure) {
        this.structure = structure;
    }

}
