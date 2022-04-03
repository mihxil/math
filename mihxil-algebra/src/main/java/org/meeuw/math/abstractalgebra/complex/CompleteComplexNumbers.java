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
    S extends CompleteComplexNumber<S, E>,
    E extends CompleteScalarFieldElement<E>>
    extends AbstractComplexNumbers<S, E>
    implements CompleteField<S> ,
    MetricSpace<S, E> {


    CompleteComplexNumbers(Class<S> elem, CompleteScalarField<E> elementStructure) {
        super(elem, elementStructure);
    }

    abstract E atan2(E imaginary, E real);


}
