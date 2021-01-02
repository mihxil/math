package org.meeuw.math.abstractalgebra.complex;

import lombok.Getter;
import lombok.extern.java.Log;

import org.meeuw.math.abstractalgebra.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log
public abstract class AbstractComplexNumbers<S extends AbstractComplexNumber<S, E>, E extends ScalarFieldElement<E>>
    extends AbstractAlgebraicStructure<S>
    implements Field<S> {

    @Getter
    private final ScalarField<E> elementStructure;

    private final S zero;
    private final S one;
    private final S i;

    AbstractComplexNumbers(Class<S> elem, ScalarField<E> elementStructure) {
        super(elem);
        this.elementStructure = elementStructure;
        this.zero = of(this.elementStructure.zero(), this.elementStructure.zero());
        this.one  = of(this.elementStructure.one(), this.elementStructure.zero());
        this.i    = of(this.elementStructure.zero(), this.elementStructure.one());
    }

    abstract S of(E real, E imaginary);

    @Override
    public S zero() {
        return zero;
    }

    @Override
    public S one() {
        return one;
    }

    public S i() {
        return i;
    }

    @Override
    public Cardinality getCardinality() {
        return getElementStructure().getCardinality();
    }
}
