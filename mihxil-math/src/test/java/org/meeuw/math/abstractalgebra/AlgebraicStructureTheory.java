package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicStructureTheory<F extends AlgebraicElement<F>>  {

    String ELEMENT = "element";
    String ELEMENTS = "elements";


    @Provide
    Arbitrary<F> elements();

    @Provide
    default Arbitrary<F> element() {
        return Arbitraries.of(elements().sample());
    }
}
