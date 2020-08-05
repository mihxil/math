package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ElementTheory<E>  {

    String ELEMENT = "element";
    String ELEMENTS = "elements";

    @Provide
    Arbitrary<E> elements();

    @Provide
    default Arbitrary<E> element() {
        return Arbitraries.of(elements().sample());
    }
}
