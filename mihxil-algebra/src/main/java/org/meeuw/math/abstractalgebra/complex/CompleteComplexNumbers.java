package org.meeuw.math.abstractalgebra.complex;

import lombok.extern.java.Log;

import org.meeuw.math.abstractalgebra.*;

/**

 *
 * @author Michiel Meeuwissen
 * @since 0.8
 * @param <S> structure element type
 * @param <E> type of real and imaginary fields
 */
@Log
public abstract class CompleteComplexNumbers<
    S extends CompleteComplexNumber<S, E, ES>,
    E extends CompleteScalarFieldElement<E>,
    ES extends CompleteScalarField<E>>
    extends AbstractComplexNumbers<S, E, ES>
    implements CompleteField<S> ,
    MetricSpace<S, E> {


    CompleteComplexNumbers(Class<S> elem, ES elementStructure) {
        super(elem, elementStructure);
    }

    abstract E atan2(E imaginary, E real);

    @Override
    public S pi() {
        return of(getElementStructure().pi(), getElementStructure().zero());
    }

    @Override
    public S e() {
        return null;
    }


}
