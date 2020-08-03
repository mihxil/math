package org.meeuw.math.abstractalgebra;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.Provide;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AlgebraicStructureTheory<F extends AlgebraicElement<F>>  {


    @Provide
    Arbitrary<F> elements();
}
